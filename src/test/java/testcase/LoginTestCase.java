package testcase;

import java.io.IOException;
import java.util.Hashtable;

import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTest;
import utilities.TestDataReader;

public class LoginTestCase extends BaseTest{

	
	private Object[][] getTestDataFor_Login() throws IOException {
		return TestDataReader.readTestData("Test7","Login", BaseTest.TestDataSheet1);
	}
	private Object[][] getTestDataFor_Sheet1() throws IOException {
		return TestDataReader.readTestData("Test7","Sheet1", BaseTest.TestDataSheet1);
	}
	private Object[][] getTestDataFor_Sheet2() throws IOException {
		return TestDataReader.readTestData("Test7","Sheet2", BaseTest.TestDataSheet1);
	}
	
	
	@DataProvider
	private Object[][] getTestData() throws IOException{
		System.out.println("===================================== Test data reading started ================================================");
		return TestDataReader.getAllData(getTestDataFor_Login(), getTestDataFor_Sheet1(),getTestDataFor_Sheet2());
	}
	
	@Test(dataProvider = "getTestData")
	public void LoginTest(Hashtable<String, String> testdata) throws InterruptedException {
		driver.findElement(By.linkText("Sign In")).click();;
		Thread.sleep(6000);
		driver.findElement(By.id("login_id")).sendKeys(testdata.get("Username"));
		driver.findElement(By.xpath("//span[.='Next']")).click();
		Thread.sleep(6000);
		driver.findElement(By.id("password")).sendKeys(testdata.get("Password"));
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[@id='nextbtn']")).click();
		Thread.sleep(6000);	
		System.out.println("=============== Test cases End ==============");
				
	}
	
}
