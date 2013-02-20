package com.triquesta.tesl.rmw.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class FlatFileParserTest {

	@Test
	public void testFlatFileParser() {
		FlatFileParser parser = new FlatFileParser();
		Assert.assertNotNull(parser);
	}

	@Test
	public void testLoadConfigFile() {
		Exception exc = null;
		try {
			FlatFileParser parser = new FlatFileParser();
			parser.loadConfigFile("setting.xml");
		} catch (Exception e) {
			e.printStackTrace();
			exc = e;
		}
		Assert.assertNull(exc);

	}

	@Test
	public void testUnmarshall() {
		List<String> list = new ArrayList<String>();
		list.add("Header,01012011");
		list.add("Joe,Smith,Developer,75000,10012009");
		list.add("Jane,Doe,Architect,80000,01152008");
		list.add("Jon,Anderson,Manager,85000,03182007");
		list.add("Trailer,3");
		FileUtils.deleteQuietly(new File("c:/iekhing/incoming/inbox/test.csv"));

		Collection<File> files = FileUtils.listFiles(new File(
				"c:/iekhing/incoming/backup/"), null, false);
		for (File file : files) {
			if (StringUtils
					.contains(file.getAbsolutePath(), "test.csv")) {
				System.out.println ("delete " + file.getAbsolutePath());
				FileUtils.deleteQuietly(file);
				
			}
		}

		// FileUtils.deleteQuietly(new
		// File("c:/iekhing/incoming/backup/test.csv"));

		try {
			FileUtils.writeLines(
					new File("c:/iekhing/incoming/inbox/test.csv"), list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final List<Object> csvRecords = new ArrayList<Object>();
		FlatFileParser parser = new FlatFileParser("c:/iekhing/incoming", ".*",
				"setting.xml", "employeeFile");
		parser.unmarshall(new FlatFileProcess() {

			@Override
			public void process(Object record) {
				csvRecords.add(record);
			}
		});

		Assert.assertEquals(HashMap.class, csvRecords.get(0).getClass());
		// Assert.assertEquals(Employee.class, csvRecords.get(0));
		Assert.assertEquals(Employee.class, csvRecords.get(1).getClass());
		Assert.assertEquals(Employee.class, csvRecords.get(2).getClass());
		Assert.assertEquals(Employee.class, csvRecords.get(3).getClass());
		Assert.assertEquals(HashMap.class, csvRecords.get(4).getClass());

		Assert.assertEquals("Joe",
				((Employee) csvRecords.get(1)).getFirstName());
		Assert.assertEquals("Jane",
				((Employee) csvRecords.get(2)).getFirstName());
		Assert.assertEquals("Jon",
				((Employee) csvRecords.get(3)).getFirstName());
		
		Collection<File> backupFiles = FileUtils.listFiles(new File(
				"c:/iekhing/incoming/backup/"), null, false);
		File backupFile = null;
		for (File file : backupFiles) {
			if (StringUtils
					.contains(file.getAbsolutePath(), "test.csv.")) {
				backupFile = file;
			}
		}

		Assert.assertTrue(backupFile.exists());

	}

	@Test
	public void testUnmarshallInvalidRecord() {
		List<String> list = new ArrayList<String>();
		list.add("Header,01012011");
		list.add("Joe,Smith,Developer,75000,10012009");
		list.add("Jane,Doe,Architect,80000,asdfsdf");
		list.add("Jon,Anderson,Manager,85000,03182007");
		list.add("Trailer,3");
		FileUtils.deleteQuietly(new File(
				"c:/iekhing/incoming/inbox/testInvalid.csv"));
		Collection<File> files = FileUtils.listFiles(new File(
				"c:/iekhing/incoming/backup/"), null, false);
		for (File file : files) {
			if (StringUtils
					.contains(file.getAbsolutePath(), "testInvalid.csv")) {
				FileUtils.deleteQuietly(file);
			}
		}
		files = FileUtils.listFiles(new File("c:/iekhing/incoming/error/"),
				null, false);
		for (File file : files) {
			if (StringUtils.contains(file.getAbsolutePath(),
					"testInvalid.csv.error")) {
				FileUtils.deleteQuietly(file);
			}
		}

		try {
			FileUtils.writeLines(new File(
					"c:/iekhing/incoming/inbox/testInvalid.csv"), list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final List<Object> csvRecords = new ArrayList<Object>();
		FlatFileParser parser = new FlatFileParser("c:/iekhing/incoming", ".*",
				"setting.xml", "employeeFile");
		parser.unmarshall(new FlatFileProcess() {

			@Override
			public void process(Object record) {
				csvRecords.add(record);
			}
		});

		Assert.assertEquals(HashMap.class, csvRecords.get(0).getClass());
		// Assert.assertEquals(Employee.class, csvRecords.get(0));
		Assert.assertEquals(Employee.class, csvRecords.get(1).getClass());
		Assert.assertEquals(Employee.class, csvRecords.get(2).getClass());
		Assert.assertEquals(HashMap.class, csvRecords.get(3).getClass());

		Assert.assertEquals("Joe",
				((Employee) csvRecords.get(1)).getFirstName());
		Assert.assertEquals("Jon",
				((Employee) csvRecords.get(2)).getFirstName());

		File backupFile = null;
		Collection<File> backupFiles = FileUtils.listFiles(new File(
				"c:/iekhing/incoming/backup/"), null, false);
		for (File file : backupFiles) {
			if (StringUtils.contains(file.getAbsolutePath(), "testInvalid.csv")) {
				backupFile = file;
			}
		}
		Assert.assertTrue(backupFile.exists());

		File errorFile = null;
		Collection<File> errorFiles = FileUtils.listFiles(new File(
				"c:/iekhing/incoming/error/"), null, false);
		for (File file : errorFiles) {
			if (StringUtils.contains(file.getAbsolutePath(),
					"testInvalid.csv.error")) {
				errorFile = file;
			}
		}
		Assert.assertTrue(errorFile.exists());

		try {
			Assert.assertEquals("Jane,Doe,Architect,80000,asdfsdf",
					StringUtils.trim(FileUtils.readFileToString(errorFile)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testMarshall() {
		FlatFileParser parser = new FlatFileParser("c:/iekhing/incoming", ".*",
				"setting.xml", "employeeFile");
		List<Object> record = new ArrayList<Object>();
		File file = new File("C:/iekhing/incoming/outbox/testMarshall.csv");
		FileUtils.deleteQuietly(file);

		Map<String, String> header = new HashMap<String, String>();
		header.put("id", "Header");
		header.put("date", "01012011");
		record.add(header);

		Employee employee = new Employee();
		employee.setFirstName("John");
		employee.setLastName("Doe");
		employee.setHireDate(new Date());
		employee.setSalary(1);
		employee.setTitle("Manager");
		record.add(employee);

		Map<String, String> trailer = new HashMap<String, String>();
		trailer.put("id", "Trailer");
		trailer.put("count", Integer.toString(record.size() - 1));
		record.add(trailer);
		parser.marshall("testMarshall.csv", record);

		Assert.assertTrue(file.exists());

		try {
			List<String> list = FileUtils.readLines(file);
			Assert.assertEquals("Header,01012011", list.get(0));
			Assert.assertEquals("John,Doe,Manager,1,02192013", list.get(1));
			Assert.assertEquals("Trailer,1", list.get(2));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testCreateFolder() {
		FlatFileParser parser = new FlatFileParser();
		
		
		File file = parser.createFolder("c:/iekhing/incoming", "backup2");
		Assert.assertTrue(file.exists());
		Assert.assertTrue(file.isDirectory());
		Assert.assertEquals("c:\\iekhing\\incoming\\backup2",
				file.getAbsolutePath());

		Exception exc = null;
		try {
			file = parser.createFolder("c:/iekhing/incoming", "backup2");
		} catch (Exception e) {
			e.printStackTrace();
			exc = e;
		}
		Assert.assertNull(exc);
		Assert.assertTrue(file.exists());
		Assert.assertTrue(file.isDirectory());
		Assert.assertEquals("c:\\iekhing\\incoming\\backup2",
				file.getAbsolutePath());

		try {
			FileUtils.deleteDirectory(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
