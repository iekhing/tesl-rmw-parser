import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import org.beanio.BeanIOConfigurationException;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.junit.Test;

import com.triquesta.tesl.rmw.parser.Employee;

public class BeanIoReaderTest {

	@Test
	public void ioReaderTest() {
		BeanReader ioReader = null;
		Reader csvReader = null;
		// create a StreamFactory
		StreamFactory factory = StreamFactory.newInstance();
		// load the setting file
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream("setting.xml");
		csvReader = new InputStreamReader(this.getClass().getClassLoader()
				.getResourceAsStream("employee.csv"));
		try {
			factory.load(in);
		
			ioReader = factory.createReader("employeeFile", csvReader);
			Employee employee;
		
			//header	
			Map header = (Map) ioReader.read();
			System.out.println(ioReader.getRecordName());
			System.out.println(header);
			
			//first record	
			employee = (Employee) ioReader.read();
			System.out.println(ioReader.getRecordName());
			System.out.println(employee.getFirstName());
			assertEquals("Joe", employee.getFirstName());

			//second record
			employee = (Employee) ioReader.read();
			System.out.println(ioReader.getRecordName());
			System.out.println(employee.getFirstName());
			assertEquals("Jane", employee.getFirstName());
			
			//third record
			employee = (Employee) ioReader.read();
			System.out.println(ioReader.getRecordName());
			System.out.println(employee.getFirstName());
			assertEquals("Jon", employee.getFirstName());
			
			//trailer	
			Map trailer = (Map) ioReader.read();
			System.out.println(ioReader.getRecordName());
			System.out.println(trailer);
	
		} catch (BeanIOConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (ioReader != null) {
				ioReader.close();

			}
			try {
				csvReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
