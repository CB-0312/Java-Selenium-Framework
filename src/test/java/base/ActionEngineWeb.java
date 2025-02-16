package base;

import org.apache.logging.log4j.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static java.time.Duration.ofSeconds;


public class ActionEngineWeb extends TestEngineWeb {

	public final Logger LOG = LogManager.getLogger(ActionEngineWeb.class);
	private static final Duration wait_Time = ofSeconds(100);
	private final String msgClickSuccess = "Successfully Clicked On ";
	private final String msgClickFailure = "Unable To Click On ";
	private final String msgTypeSuccess = "Successfully Entered value : ";
	private final String msgTypeFailure = "Unable To Type : ";
	private final String msgIsElementFoundSuccess = "Successfully Found Element ";


	public static String TestDataSheet1 = System.getProperty("user.dir") +"\\src\\test\\resources\\testdata\\TestData1.xlsx";

	public void waitForMask() throws Throwable {

			LOG.info("Wait for loading icon to disappear");
			ExpectedCondition<Boolean> bodyTag = driver -> !((Boolean) driver.findElement(By.tagName("body")).getAttribute("class").contains("x-masked"));
			new WebDriverWait(driver, wait_Time).until(bodyTag);
			LOG.info("loading icon disappeared");
	}

	/**
	 * getCallerClassName
	 *
	 * @return String
	 */
	protected static String getCallerClassName() {
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		return stElements[3].getClassName();
	}

	/**
	 * getCallerMethodName
	 *
	 * @return String
	 */
	protected static String getCallerMethodName() {
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		return stElements[3].getMethodName();
	}


	/**
	 * click
	 *
	 * @param locator     of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean click(By locator, String locatorName) throws Throwable {
		boolean status = false;
		//isElementPresent(locator, locatorName);
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : click  ::  Locator : " + locatorName);
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element");
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOG.info("Locator is Visible :: " + locator);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			this.waitForMask();
			driver.findElement(locator).click();
			this.waitForMask();
			LOG.info("Clicked on the Locator");
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			status = true;
		} catch (Exception e) {
			status = false;
			LOG.info(e.getMessage());
		} finally {
			if (!status) {
				reporter.failureReport("Click : " + locatorName, msgClickFailure + locatorName, driver);
			} else {
				reporter.SuccessReport("Click : " + locatorName, msgClickSuccess + locatorName);
			}
		}
		return status;
	}
}
