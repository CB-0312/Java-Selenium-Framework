package testcase;

import PageElements.ELementsPageLib;
import base.ActionEngineWeb;
import base.TestEngineWeb;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utilities.TestDataReader;

import java.io.IOException;
import java.util.Hashtable;

public class DemoQA1 extends TestEngineWeb {

	
	private Object[][] getTestDataFor_Login() throws IOException {
		return TestDataReader.readTestData("Test1","Login", ActionEngineWeb.TestDataSheet1);
	}
	private Object[][] getTestDataFor_DemoQa() throws IOException {
		return TestDataReader.readTestData("Test1","DEMOQA", ActionEngineWeb.TestDataSheet1);
	}

	@DataProvider
	private Object[][] getTestData() throws IOException{
		System.out.println("====================== Test data reading started ===============================");
		return TestDataReader.getAllData(getTestDataFor_Login(),getTestDataFor_DemoQa());
	}
	
	@Test(dataProvider = "getTestData")
	public void elementsTest(Hashtable<String, String> testdata) throws Throwable {

		ELementsPageLib eLementsPageLib = new ELementsPageLib(driver, reporter);
		eLementsPageLib.clickElementsTab();
		System.out.println("=============== Test cases End ==============");
				
	}
	
}
