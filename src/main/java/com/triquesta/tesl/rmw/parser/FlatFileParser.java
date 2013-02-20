package com.triquesta.tesl.rmw.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.beanio.BeanIOConfigurationException;
import org.beanio.BeanReader;
import org.beanio.BeanReaderErrorHandlerSupport;
import org.beanio.BeanWriter;
import org.beanio.InvalidRecordException;
import org.beanio.StreamFactory;
import org.beanio.UnidentifiedRecordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlatFileParser {

	private static final Logger logger = LoggerFactory
			.getLogger(FlatFileParser.class);

	private String directoryName;
	private String fileNamePattern;
	private String settingXml;
	private StreamFactory factory;
	private String streamName;
	private File errorDir;
	private File inboxDir;
	private File outboxDir;
	private File backupDir;

	public FlatFileParser(String directoryName, String fileNamePattern,
			String settingXml, String streamName) {
		super();
		this.directoryName = directoryName;
		this.fileNamePattern = fileNamePattern;
		this.settingXml = settingXml;
		this.streamName = streamName;
		factory = loadConfigFile(this.settingXml);
	}

	FlatFileParser() {
		super();
	}

	protected StreamFactory loadConfigFile(String settingXml) {
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream(settingXml);
		logger.info("load {} file from classpath", settingXml);
		factory = StreamFactory.newInstance();
		try {
			factory.load(in);
		} catch (BeanIOConfigurationException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return factory;
	}

	public void unmarshall(FlatFileProcess process) {
		IOFileFilter fileFilter = new RegexFileFilter(this.fileNamePattern);
		this.backupDir = this.createFolder(this.directoryName, "backup");
		this.errorDir = this.createFolder(this.directoryName, "error");
		this.inboxDir = this.createFolder(this.directoryName, "inbox");
	
		for (File file : FileUtils.listFiles(this.inboxDir, fileFilter, null)) {
			//backupFileToAvoidOverwrite(file);
			List<String> invalidRecords = unmarshallFile(file, process);
			String timestamp = "."+System.currentTimeMillis();
			generateErrorFile(
					new File(this.errorDir, file.getName() + ".error" + timestamp),
					invalidRecords);
			moveFileToBackupWhenCompleted(file, timestamp);

		}
	}

	private void generateErrorFile(File file, List<String> invalidRecord) {

		if (invalidRecord.size() > 0) {
			try {
				logger.info("error records size {} error file {}",invalidRecord.size(),file.getAbsolutePath());
				FileUtils.writeLines(file, invalidRecord);
			} catch (IOException e) {
				logger.info(e.getMessage(), e);
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	public void moveFileToBackupWhenCompleted(File file, String timestamp) {
		try {
			logger.info("move file from {} to {}", file.getAbsolutePath(), (new File(this.backupDir, file.getName()+ timestamp)).getAbsolutePath());
			FileUtils.moveFile(file, new File(this.backupDir, file.getName()+ timestamp));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

//	public void backupFileToAvoidOverwrite(File file) {
//		try {
//			if ((new File(this.backupDir, file.getName()).exists())) {
//				FileUtils.moveFile(
//						new File(this.backupDir, file.getName()),
//						new File(this.backupDir, file.getName() + "."
//								);
//
//			}
//		} catch (IOException e) {
//			logger.error(e.getMessage(), e);
//			throw new RuntimeException(e.getMessage(), e);
//		}
//	}

	public void marshall(String fileName, List list) {
		this.outboxDir = this.createFolder(this.directoryName, "outbox");
		BeanWriter out = factory.createWriter(this.streamName, new File(
				this.outboxDir, fileName));
		try {
			for (Object object : list) {
				out.write(object);
			}
		} finally {
			out.flush();
			out.close();
		}
	}

	private List<String> unmarshallFile(File file, FlatFileProcess process) {
		logger.info("unmarshallFile {} ", file.getAbsoluteFile());
		BeanReader flatFileReader = factory.createReader(this.streamName, file);
		Object record = null;
		List<String> invalidRecords = new ArrayList<String>();
		try {
			errorHandler(flatFileReader, invalidRecords);
			while ((record = flatFileReader.read()) != null) {
				try {
					logger.info("process {} ", record);
					process.process(record);
				} catch (UnidentifiedRecordException e) {
					logger.info(e.getMessage(), e);
				} catch (Exception e) {
					logger.info(e.getMessage(), e);
					// throw new RuntimeException(e.getMessage(), e);
				}
			}
		} catch (Exception e){
			logger.error(e.getMessage(), e);
		} 
		finally {
			flatFileReader.close();
		}
		return invalidRecords;

	}

	private void errorHandler(BeanReader beanReader,
			final List<String> invalidRecords) {
		beanReader.setErrorHandler(new BeanReaderErrorHandlerSupport() {
			public void invalidRecord(InvalidRecordException ex)
					throws Exception {
				logger.debug(ex.getMessage(), ex);
				for (int i = 0, j = ex.getRecordCount(); i < j; i++) {
					logger.warn("invalid record {} :: {}  :: {} ", ex
							.getRecordContext(i).getRecordText(), ex
							.getMessage(), ex.getRecordContext(i)
							.getFieldErrors());
					invalidRecords.add(ex.getRecordContext(i).getRecordText());
				}
			}
			
			 public void unidentifiedRecord(UnidentifiedRecordException ex) throws Exception {
				 logger.debug(ex.getMessage(), ex);
					for (int i = 0, j = ex.getRecordCount(); i < j; i++) {
						logger.warn("invalid record {} :: {}  :: {} ", ex
								.getRecordContext(i).getRecordText(), ex
								.getMessage(), ex.getRecordContext(i)
								.getFieldErrors());
						invalidRecords.add(ex.getRecordContext(i).getRecordText());
					}
			    }
		});
	}

	protected File createFolder(String baseDir, String child) {
		File dir = new File(baseDir + "/" + child);
		try {
			logger.info("backup folder is {} ", dir.getAbsoluteFile());
			FileUtils.forceMkdir(dir);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
		return dir;
	}

}
