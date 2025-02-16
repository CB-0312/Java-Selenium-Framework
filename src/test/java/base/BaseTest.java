package base;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

	public static WebDriver driver;
	public static Properties prop = new Properties();
	public static FileReader fr;

	
	
	public static String TestDataSheet1 = System.getProperty("user.dir") +"\\src\\test\\resources\\testdata\\TestData1.xlsx";
 
	@BeforeMethod
	public void setup() throws IOException {

		if (driver == null) {
			fr = new FileReader(System.getProperty("user.dir") + "/src/test/resources/configfiles/Config.properties");
			prop.load(fr);
		}

		switch (prop.getProperty("Browser")) {
		case "Chrome":
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
		case "FireFox":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			break;
		default:
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
		}
		driver.get(prop.getProperty("BaseUrl"));
	}

	@AfterMethod
	public void quit() {
		driver.quit();
	}

}
