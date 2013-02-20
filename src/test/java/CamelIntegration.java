import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.beanio.BeanIODataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.DataFormat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.triquesta.tesl.rmw.parser.Employee;

class LoggingProcessor implements Processor {

	private static final Logger logger = LoggerFactory
			.getLogger(LoggingProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("Received Order: "
				+ exchange.getIn().getBody(String.class));
		List list = exchange.getIn().getBody(List.class);
		logger.info("Body class type {}", list.get(0).getClass());
		if (list.get(0).getClass() == Employee.class) {
			Employee employee = (Employee) list.get(0);
			logger.info("Employee first name: " + employee.getFirstName());
		}
	}
}

class LoggingError implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("ERRORORORORORORO");
	}

}

public class CamelIntegration {

	@Test
	public void test() {

	}

	public static void main(String[] args) {
		CamelContext context = new DefaultCamelContext();
		try {
			final DataFormat format = new BeanIODataFormat("setting.xml",
					"employeeFile");
			((BeanIODataFormat) format).setIgnoreInvalidRecords(Boolean.FALSE);

			context.addRoutes(new RouteBuilder() {
				public void configure() {
					onException(org.beanio.InvalidRecordException.class)
							.handled(Boolean.FALSE).process(new LoggingError());
					from("direct:start").to("file:C:/iekhing/incoming/");
					from("file:C:/iekhing/incoming/").split().tokenize("\n")
							.unmarshal(format).process(new LoggingProcessor())
							.to("mock:result");

				}
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			context.start();
			Thread.sleep(1000);
			context.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
