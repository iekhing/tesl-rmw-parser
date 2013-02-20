package com.triquesta.tesl.rmw.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BankDetailParserTest {
	
	@Test
	public void parseTest(){
		final List<Object> csvRecords = new ArrayList<Object>();
		FlatFileParser parser = new FlatFileParser("c:/iekhing/incoming", "updateBankRecord.csv",
				"setting.xml", "updateBankRecord");
		parser.unmarshall(new FlatFileProcess() {

			@Override
			public void process(Object record) {
				csvRecords.add(record);
			}
		});
		for (Object record : csvRecords){
			System.out.println(record);
		}
		

	}

}
