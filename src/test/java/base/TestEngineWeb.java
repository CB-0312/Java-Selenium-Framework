package base;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import reports.CReporter;
import reports.ConfigFileReadWrite;
import reports.ReporterConstants;

public class TestEngineWeb {
	public static final Logger LOG = LogManager.getLogger(TestEngineWeb.class);
	public static WebDriver driver;
	public static Properties prop = new Properties();
	public static FileReader fr;
	public CReporter reporter = null;

	public static String LOCATION_CLIENT_LOGO = null;
	public static String gTestCaseDesc = null;
	public static long startTime;
	public String APP_BASE_URL = null;
	public String SUMMARY_REPORTER_BASEURL = null;
	protected WebDriver WebDriver = null;

	public static String browser = null;
	public static String platformName = null;
	public static String environment = null;
	public static String version = null;
	public String suiteStartTime = null;

	@BeforeSuite
	public void setup() throws IOException {
		startTime = System.currentTimeMillis();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy hh mm ss SSS");
		String formattedDate = sdf.format(date);
		suiteStartTime = formattedDate.replace(":", "_").replace(" ", "_");
		if (driver == null) {
			fr = new FileReader(System.getProperty("user.dir") + "/src/test/resources/configfiles/Config.properties");
			prop.load(fr);
		}

		switch (prop.getProperty("Browser")) {
		case "Chrome":
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			browser="Chrome";
			break;
		case "FireFox":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			browser="FireFox";
			break;
		default:
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
		}
		platformName="Test";
		environment="UAT";
		version= "1";
		WebDriver=driver;
		driver.get(prop.getProperty("BaseUrlDemoQA"));
		driver.manage().window().maximize();
	}

	@BeforeClass(alwaysRun = true)
//	@Parameters({ "automationName", "browser", "browserVersion", "environment",
//			"platformName", "seleniumgridurl" })
	public void beforeClass()
			throws IOException, InterruptedException {
		// set and get system property at before class
//		String testName = iTestContext.getName();

		APP_BASE_URL = ConfigFileReadWrite.read(
				ReporterConstants.configReporterFile, "emBaseUrl");
		SUMMARY_REPORTER_BASEURL = APP_BASE_URL;

		LOG.info("---------------------");
		LOG.info("-----Before Class----");
		LOG.info("---------------------");
		LOG.info("Execution Start Time :: " + startTime);

		LOCATION_CLIENT_LOGO = ReporterConstants.PURE_LOCATION_CLIENT_LOGO;
		LOG.info("Assigning event firing webdriver instance to driver");
//		LOG.info("WebDriver ::: " + this.WebDriver.toString());
		LOG.info("Assigned event firing webdriver instance to driver");
		LOG.info("eventfiring driver ::: " + driver.toString());

		LOG.info("Creating reporter instance");
		reporter = CReporter.getCReporter(browser, platformName, environment,
				driver.toString());
		reporter.initTestCase(
				this.getClass()
						.getName()
						.substring(0,
								this.getClass().getName().lastIndexOf(".")),
				this.getClass()
						.getName()
						.substring(
								this.getClass().getName().lastIndexOf(".") + 1),
				null, true);
		LOG.info("Created reporter instance");

		reporter.calculateSuiteStartTime();
		LOG.info("---------------------");
		LOG.info("---End Before Class--");
		LOG.info("---------------------");

		// set test for ExtentReports
		//		ExtentReportThread.startTest(testName);
	}

	@AfterTest
	public void quit() {
		driver.quit();
	}

}
