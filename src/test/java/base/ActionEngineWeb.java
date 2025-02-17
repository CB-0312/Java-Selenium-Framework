package base;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.*;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import reports.ReporterConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;

import static java.time.Duration.ofSeconds;


public class ActionEngineWeb extends TestEngineWeb {

	public final Logger LOG = LogManager.getLogger(ActionEngineWeb.class);
	private static final Duration wait_Time = ofSeconds(100);
	private final String msgClickSuccess = "Successfully Clicked On ";
	private final String msgClickFailure = "Unable To Click On ";
	private final String msgTypeSuccess = "Successfully Entered value : ";
	private final String msgTypeFailure = "Unable To Type : ";
	private final String msgIsElementFoundSuccess = "Successfully Found Element ";

	protected boolean reportIndicator = true;

	private static final Duration DEFAULT_TIMEOUT_SEC =  ofSeconds(90);
	private static final Duration SLEEP_MILLI_SEC = ofSeconds(10);



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



	/**
	 * selectByIndex
	 *
	 * @param locator     of (By)
	 * @param index       of (int)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean selectByIndex(By locator, int index, String locatorName) throws Throwable {
		boolean flag = false;
		String selectedText = "";
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
			//			selectedText = new Select(driver.findElement(locator)).getFirstSelectedOption().getText();
			selectedText = new Select(driver.findElement(locator)).getOptions().get(index).getText();
			new Select(driver.findElement(locator)).selectByIndex(index);
			flag = true;
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			return true;
		} catch (Exception e) {
			LOG.info("++++++++++++++++++++++++++++Catch Block Start+++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info(e.getMessage());
			LOG.info("++++++++++++++++++++++++++++Catch Block End+++++++++++++++++++++++++++++++++++++++++++");
			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				reporter.failureReport("Select Value from the Dropdown :: " + locatorName,
						"Failed to select value from the DropDown :: " + locatorName, driver);
			} else {
				reporter.SuccessReport("Select Value from the Dropdown :: " + locatorName,
						"Selected  "+ selectedText +" from the DropDown :: " + locatorName);
			}
		}
	}

	/**
	 * assertTrue
	 *
	 * @param condition of (boolean)
	 * @param message   of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean assertTrue(boolean condition, String message) throws Throwable {
		if (!condition) {
			reporter.failureReport("Expected :: " + message, message + " is :: false" , driver);
		} else {
			if (!message.equals("")) {
				reporter.SuccessReport("Expected :: " + message, message + " is :: true");
			}
		}
		return condition;
	}

	/**
	 * assertFalse
	 *
	 * @param condition of (boolean)
	 * @param message   of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean assertFalse(boolean condition, String message) throws Throwable {
		try {
			return condition;
		} catch (Exception e) {
			LOG.info("++++++++++++++++++++++++++++Catch Block Start+++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("++++++++++++++++++++++++++++Catch Block End+++++++++++++++++++++++++++++++++++++++++++");
			return true;
		} finally {
			if (condition) {
				reporter.failureReport("Expected :: " + message, message + " is :: true", driver);
			} else {
				reporter.SuccessReport("Expected :: " + message, message + " is :: false");
			}
		}
	}

	/**
	 * dynamicWaitByLocator
	 *
	 * @param locator of (By)
	 * @param time    of (int)
	 * @throws InterruptedException the throwable
	 */
	public void dynamicWaitByLocator(By locator, Duration time) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, time);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
	}

	/**
	 * dynamicWaitByLocator
	 *
	 * @param locator of (By)
	 * @throws InterruptedException the throwable
	 */
	public void dynamicWaitByLocator(By locator) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT_SEC, SLEEP_MILLI_SEC);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
	}

	/**
	 * assertElementPresent
	 *
	 * @param by          of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean assertElementPresent(By by, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			Assert.assertTrue(isElementPresent(by, locatorName, true));
			flag = true;
		} catch (Exception e) {
			LOG.info("++++++++++++++++++++++++++++Catch Block Start+++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);

			LOG.info("++++++++++++++++++++++++++++Catch Block End+++++++++++++++++++++++++++++++++++++++++++");
		} finally {
			if (!flag) {
				reporter.failureReport("AssertElementPresent :: ", locatorName + " is not present in the page :: ", driver);
				//return false;
			} else {
				reporter.SuccessReport("AssertElementPresent :: ", locatorName + " present in the page :: ");
			}
		}
		return flag;
	}

	/**
	 * mouseHoverByJavaScript
	 *
	 * @param locator     of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean mouseHoverByJavaScript(By locator, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
			WebElement mo = driver.findElement(locator);
			String javaScript = "var evObj = document.createEvent('MouseEvents');"
					+ "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
					+ "arguments[0].dispatchEvent(evObj);";
			((JavascriptExecutor) WebDriver).executeScript(javaScript, mo);
			flag = true;
			LOG.info("MoveOver action is done on  :: " + locatorName);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			return true;
		} catch (Exception e) {
			LOG.info("++++++++++++++++++++++++++++Catch Block Start+++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("++++++++++++++++++++++++++++Catch Block End+++++++++++++++++++++++++++++++++++++++++++");
			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				reporter.failureReport("MouseOver :: ", "MouseOver action is not perform on :: " + locatorName, driver);
			} else {
				reporter.SuccessReport("MouseOver :: ", "MouserOver Action is Done on :: " + locatorName);
			}
		}
	}

	/**
	 * waitForVisibilityOfElement
	 *
	 * @param by          of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean waitForVisibilityOfElement(By by, String locatorName) throws Throwable {
		boolean flag = false;
		LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
		LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
		WebDriverWait wait = new WebDriverWait(driver, wait_Time);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			flag = true;
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			return true;
		} catch (Exception e) {
			LOG.info("++++++++++++++++++++++++++++Catch Block Start+++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());

			LOG.info("++++++++++++++++++++++++++++Catch Block End+++++++++++++++++++++++++++++++++++++++++++");
			return false;
		} finally {
			if (!flag) {
				reporter.failureReport("Visible of element " + locatorName, "Element :: " + locatorName + " is not visible", driver);
			} else {
				reporter.SuccessReport("Visible of element " + locatorName, "Element :: " + locatorName + "  is visible");
			}
		}
	}


	/**
	 * waitForInVisibilityOfElement
	 *
	 * @param by          of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean waitForInVisibilityOfElement(By by, String locatorName) throws Throwable {
		boolean flag = false;
		LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
		LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
		WebDriverWait wait = new WebDriverWait(driver, wait_Time);
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
			flag = true;
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			return true;
		} catch (Exception e) {
			LOG.info("++++++++++++++++++++++++++++Catch Block Start+++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());

			LOG.info("++++++++++++++++++++++++++++Catch Block End+++++++++++++++++++++++++++++++++++++++++++");
			return false;
		} finally {
			if (!flag) {
				reporter.failureReport("InVisible of element is false :: ", "Element :: " + locatorName + " is visible", driver);
			} else {
				reporter.SuccessReport("InVisible of element is true :: ", "Element :: " + locatorName + "  is not visible");
			}
		}
	}
	/**
	 * clickUsingJavascriptExecutor
	 *
	 * @param locator     of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean clickUsingJavascriptExecutor(By locator, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
			WebElement element = driver.findElement(locator);
			isElementPresent(locator, locatorName);
			((JavascriptExecutor) WebDriver).executeScript("arguments[0].click();", element);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			flag = true;
			LOG.info("clicked : " + locatorName);
		} catch (Exception e) {
			LOG.info("++++++++++++++++++++++++++++Catch Block Start+++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("++++++++++++++++++++++++++++Catch Block End+++++++++++++++++++++++++++++++++++++++++++");
			flag = false;
		} finally {
			if (!flag) {
				if (reportIndicator) {
					reporter.failureReport("Click : " + locatorName, msgClickFailure + locatorName, driver);
				}
			} else {
				reporter.SuccessReport("Click : " + locatorName, msgClickSuccess + locatorName);
			}
			reportIndicator = true;
		}
		return flag;
	}

	/**
	 * selectByValue
	 *
	 * @param locator     of (By)
	 * @param value       of (String)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean selectByValue(By locator, String value, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
			Select s = new Select(driver.findElement(locator));
			s.selectByValue(value);
			flag = true;
			LOG.info("Successfully selected the value" + locatorName);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				reporter.failureReport("Select",
						"Option with value attribute : " + value + " is Not Select from the DropDown : " + locatorName, driver);
			} else {
				reporter.SuccessReport("Select",
						"Option with value attribute : " + value + " is  Selected from the DropDown : " + locatorName);
			}
		}
	}

	public boolean optionAvailable(By locator, String valueToBeSelected){
		boolean status = false;
		List<WebElement> elems = driver.findElement(locator).findElements(By.tagName("option"));
		for(int i = 0; i < elems.size(); i++){
			LOG.info(i + " :: " + elems.get(i).getText());
			if(elems.get(i).getText().equalsIgnoreCase(valueToBeSelected)) status = true;
		}
		return status;
	}

	/**
	 * selectByVisibleText
	 *
	 * @param locator     of (By)
	 * @param visibleText of (String)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean selectByVisibleText(By locator, String visibleText, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element");
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOG.info("Locator is Visible :: " + locatorName);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			LOG.info("Locator is clickable :: " + locatorName);
			LOG.info("Available values:::");
			List<WebElement> elems = driver.findElement(locator).findElements(By.tagName("option"));
			if(elems.size() == 0)
				reporter.SuccessReport("No Values available for ", locatorName);
			for(int i = 0; i < elems.size(); i++){
				LOG.info(i + " :: " + elems.get(i).getText());
//				reporter.SuccessReport(locatorName +" available values", elems.get(i).getText());
			}
			Select s = new Select(driver.findElement(locator));
			s.selectByVisibleText(visibleText.trim());
			flag = true;
			return true;
		} catch (Exception e) {
			//return false;

			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				reporter.failureReport("Select", visibleText + " is Not Select from the DropDown" + locatorName, driver);
			} else {
				reporter.SuccessReport("Select", visibleText + "  is Selected from the DropDown" + locatorName);
			}
		}
	}

	/**
	 * isVisible
	 *
	 * @param locator     of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean isVisible(By locator, String locatorName) throws Throwable {
		boolean flag = false;

		try {
			//added loggers
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name :: " + getCallerClassName() + " Method name :: " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
			//value = driver.findElement(locator).isDisplayed();
			flag = driver.findElement(locator).isDisplayed();
			//value = true;
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (Exception e) {
			flag = false;
		} finally {
			if (!flag) {
				reporter.failureReport("IsVisible : ", locatorName + " Element is Not Visible : ", driver);
			} else {
				reporter.SuccessReport("IsVisible : ", locatorName + " Element is Visible : ");
			}
		}
		return flag;
	}

	/**
	 * getElementsSize
	 *
	 * @param locator of (By)
	 * @return int
	 */
	public int getElementsSize(By locator) {
		int a = 0;
		try {
			List<WebElement> rows = driver.findElements(locator);
			a = rows.size();
		} catch (Exception e) {
			e.getMessage();
		}
		return a;
	}

	/**
	 * assertTextMatching
	 *
	 * @param by          of (By)
	 * @param text        of (String)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean assertTextMatching(By by, String text, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
			String ActualText = getText(by, locatorName).trim();
			LOG.info("ActualText is : " + ActualText);

			if (ActualText.contains(text.trim())) {
				flag = true;
				LOG.info("String comparison with actual text :: " + "actual text is : " + ActualText + "And expected text is : " + text);
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				return true;
			} else {
				LOG.info("String comparison with actual text :: " + "actual text is : " + ActualText + "And expected text is : " + text);
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			if (!flag) {
				reporter.failureReport("Verify : " + locatorName, text + " is not present in the element : ", driver);
				//return false;
			} else {
				reporter.SuccessReport("Verify : " + locatorName, text + " is  present in the element : " + locatorName);
			}
		}
	}

	/**
	 * assertTextMatchingWithAttribute
	 *
	 * @param by          of (By)
	 * @param text        of (String)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean assertTextMatchingWithAttribute(By by, String text, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			// added loggers
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
			String ActualText = getAttributeByValue(by, text).trim();
			LOG.info("ActualText is" + ActualText);
			if (ActualText.contains(text.trim())) {
				flag = true;
				// added loggers
				LOG.info("String comparison with actual text :: " + "actual text is :" + ActualText + "And expected text is : " + text);
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			if (!flag) {
				reporter.failureReport("Verify : " + locatorName, text + " is not present in the element : ", driver);
				//return false;
			} else {
				reporter.SuccessReport("Verify : " + locatorName, text + " is  present in the element ");
			}
		}
	}

	/**
	 * assertTextStringMatching
	 *
	 * @param actText of (String)
	 * @param expText of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean assertTextStringMatching(String actText, String expText) throws Throwable {
		boolean flag = false;
		try {
			// added loggers
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			String ActualText = actText.trim();
			LOG.info("act - " + ActualText);
			LOG.info("exp - " + expText);
			if (ActualText.equalsIgnoreCase(expText.trim())) {
				LOG.info("in if loop");
				flag = true;
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				return true;
			} else {
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			if (!flag) {
				reporter.failureReport("Verify : " + expText, actText + " is not present in the element.", driver);
				//return false;
			} else {
				reporter.SuccessReport("Verify : " + expText, actText + " is  present in the element.");
			}
		}
	}

	/**
	 * getTimeStamp
	 *
	 * @return String
	 */
	public String getTimeStamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy hh mm ss SSS");
		String formattedDate = sdf.format(date);
		suiteStartTime = formattedDate.replace(":", "_").replace(" ", "_");
		return suiteStartTime;
	}

	/**
	 * isElementPresent
	 *
	 * @param by          of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */

	public boolean isElementPresent(By by, String locatorName, boolean expected) throws Throwable {
		boolean status = expected;
		String msgIsElementFoundFailure = "Unable To Found Element ";
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			waitTime();
			driver.findElement(by);
			status = true;
		} catch (Exception e) {
			status = false;
			LOG.info(e.getMessage());
		} finally {
			if (!status) {
				if (reportIndicator) {
					LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					reporter.failureReport("isElementPresent : ", msgIsElementFoundFailure + locatorName, driver);
				}
			} else {
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				this.reporter.SuccessReport("isElementPresent : " + locatorName,this.msgIsElementFoundSuccess + locatorName);
			}
			reportIndicator = true;
		}
		return status;
	}

	/**
	 * isElementPresent
	 *
	 * @param by          of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean isElementPresent(By by, String locatorName) throws Throwable {
		boolean status;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			dynamicWait(by);
			driver.findElement(by);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			status = true;
			reporter.SuccessReport("isElementPresent : " + locatorName,  locatorName+" is visible");
		} catch (Exception e) {
			status = false;
			LOG.info(e.getMessage());
		}
		return status;
	}

	/**
	 * isElementPresent
	 *
	 * @param by          of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean isElementNotPresent(By by, String locatorName) throws Throwable {
		boolean status;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			// dynamicWait(by);
			String time = ReporterConstants.MIN_TIMEOUT;
			Duration timevalue = ofSeconds(Integer.parseInt(time));
			WebDriverWait wait = new WebDriverWait(driver, timevalue);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			status = true;
			reporter.SuccessReport("IsElementNotPresent" ,  locatorName+" is not visible/present.");
		} catch (Exception e) {
			status = false;
			reporter.failureReport("IsElementNotPresent",  locatorName+" is visible/present.", driver);
			LOG.info(e.getMessage());
		}
		return status;
	}

	/**
	 * scroll
	 *
	 * @param by          of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean scroll(By by, String locatorName) throws Throwable {
		boolean status = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName());
			WebElement element = this.driver.findElement(by);
			Actions actions = new Actions(this.driver);
			actions.moveToElement(element);
			actions.build().perform();
			this.waitForMask();
			LOG.info("Scroll is performed : " + locatorName);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			status = true;
		} catch (Exception e) {
			e.getMessage();
		}
		return status;
	}

	/**
	 * JSScroll
	 *
	 * @param by          of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean JSScroll(By by, String locatorName) throws Throwable {
		boolean status = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName());
			WebElement element = driver.findElement(by);
			((JavascriptExecutor)WebDriver).executeScript("arguments[0].scrollIntoView(true);", element);
			this.waitForMask();
			LOG.info("Scroll is performed : " + locatorName);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			status = true;
		} catch (Exception e) {
			e.getMessage();
		}
		return status;
	}
	/**
	 * verifyElementEnable
	 *
	 * @param by          of (By)
	 * @param locatorName of (String)
	 * @param expected    of (boolean)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean verifyElementEnable(By by, String locatorName, boolean expected) throws Throwable {
		boolean status = expected;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName());
			if (this.driver.findElement(by).isEnabled()) {
				LOG.info("Element is enable :: " + locatorName);
				status = true;
			} else {
				status = false;
			}
		} catch (Exception e) {
			status = false;
			LOG.info(e.getMessage());
		}finally {
			if(status){
				if(expected){
					reporter.SuccessReport("verify Element Enable : ", "Element is enable: "+locatorName+" : true");
				}else{
					reporter.failureReport("verify Element Enable : ",  "Element is not enable: "+locatorName+" : false", driver);
				}
			}else{
				if(!expected){
					reporter.SuccessReport("verify Element Disable : ", "Element is disabled: "+locatorName+" : true");
				}else{
					reporter.failureReport("verify Element Disable : ",  "Element is disabled: "+locatorName+" : false", driver);
				}
			}
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		}
		return status;
	}

	/**
	 * verifyElementPresent
	 *
	 * @param by          of (By)
	 * @param locatorName of (String)
	 * @param expected    of (boolean)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean verifyElementPresent(By by, String locatorName, boolean expected) throws Throwable {
		boolean status = expected;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName());
			if (this.driver.findElement(by).isDisplayed()) {
				LOG.info("Element is available :: " + locatorName);
				status = true;
			} else {
				status = false;
			}
		} catch (Exception e) {
			status = false;
			LOG.info(e.getMessage());
		}finally {
			if(status){
				if(expected){
					reporter.SuccessReport("verifyElementPresent : ", "Element is present: "+locatorName+" : true");
				}else{
					reporter.failureReport("verifyElementPresent : ",  "Element is not present: "+locatorName+" : false", driver);
				}
			}else{
				if(!expected){
					reporter.SuccessReport("verifyElementPresent : ", "Element is not present: "+locatorName+" : true");
				}else{
					reporter.failureReport("verifyElementPresent : ",  "Element is present: "+locatorName+" : false", driver);
				}
			}
			reportIndicator = true;
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		}
		return status;
	}

	private void waitTime() throws Throwable {
		String time = ReporterConstants.Timeout;
		long timeValue = Long.parseLong(time);
		LOG.info("Time out value is : " + timeValue);
	}

	/**
	 * type
	 *
	 * @param locator     of (By)
	 * @param testData    of (String)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean type(By locator, String testData, String locatorName) throws Throwable {
		boolean status = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : Type  ::  Locator : " + locatorName + " :: Data :" + testData);
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element :: " + locator);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			this.bringToFocus(locator);
			LOG.info("Waiting for element to be clickable :: " + locator);
			this.waitForMask();
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			this.waitForMask();
			driver.findElement(locator).click();
			LOG.info("Clicked on the Locator : ");
			driver.findElement(locator).clear();
			LOG.info("Cleared the existing Locator data : ");
			driver.findElement(locator).sendKeys(testData);
			LOG.info("Typed the Locator data :: " + testData);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			status = true;
		}  catch (Exception e) {
			status = false;
			LOG.info(e.getMessage());
		}
		finally {
			if (!status) {
				if (reportIndicator) {
					LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					reporter.failureReport("Enter text in :: " + locatorName, msgTypeFailure + testData, driver);
				}
			} else {
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				reporter.SuccessReport("Enter text in :: " + locatorName, msgTypeSuccess + testData);
			}
			reportIndicator = true;
		}
		return status;
	}

	/**
	 * type
	 *
	 * @param locator     of (By)
	 * //@param testData    of (String)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean type(By locator, Keys keys, String locatorName) throws Throwable {
		boolean status = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : Type  ::  Locator : " + locatorName + " :: Data :" + keys.toString());
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element :");
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOG.info("Locator is Visible :: " + locator);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			driver.findElement(locator).sendKeys(keys);
			this.waitForMask();
			//			Thread.sleep(1000);//Inevitable
			LOG.info("Typed the Locator data :: " + keys.toString());
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			status = true;
		}  catch (Exception e) {
			status = false;
			LOG.info(e.getMessage());
			reporter.failureReport("Enter text in :: " + locatorName, msgTypeFailure + keys.toString() +" Due to: "+ e.getMessage(), driver);
		}
		/*finally {
			if (!status) {
				if (reportIndicator) {
					LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					reporter.failureReport("Enter text in :: " + locatorName, msgTypeFailure + keys.toString(), driver);
				}
			}
		}*/
		return status;
	}

	/**
	 * typeUntil
	 *
	 * @param locator     of (By)
	 * @param testData    of (String)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean typeUntil(By locator, String testData, String locatorName) throws Throwable {
		boolean status = false;
		boolean flag=false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : Type  ::  Locator : " + locatorName + " :: Data :" + testData);
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element :");
			//wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOG.info("Locator is Visible :: " + locator);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			driver.findElement(locator).click();
			LOG.info("Clicked on the Locator : ");
			int counter=0;
			do {
				counter = counter+1;
				driver.findElement(locator).clear();
				LOG.info("Cleared the existing Locator data : ");
				driver.findElement(locator).sendKeys(testData);
				if(counter>=4){
					break;
				}
				if(driver.findElement(locator).getAttribute("value").equalsIgnoreCase(testData)){
					flag=true;
					break;
				}
			} while (!flag);
			if(counter>=4){
				status=false;
			}else{
				LOG.info("Typed the Locator data :: " + testData);
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				status = true;
			}
		} catch (Exception e) {
			status = false;
			LOG.info(e.getMessage());
		}
		finally {
			if (!status) {
				if (reportIndicator) {
					LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					reporter.failureReport("Enter text in :: " + locatorName, msgTypeFailure + testData, driver);
				}
			} else {
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				reporter.SuccessReport("Enter text in :: " + locatorName, msgTypeSuccess + testData);
			}
			reportIndicator = true;
		}
		return status;
	}

	/**
	 * type text with out clear
	 *
	 * @param locator     of (By)
	 * @param testData    of (String)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean typeText(By locator, String testData, String locatorName) throws Throwable {
		boolean status;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : Type  ::  Locator : " + locatorName + " :: Data :" + testData);
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element :");
			//wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOG.info("Locator is Visible :: " + locator);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			driver.findElement(locator).sendKeys(testData);
			LOG.info("Typed the Locator data :: " + testData);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

			reporter.SuccessReport("Enter text in :: " + locatorName, msgTypeSuccess + testData);
			status = true;
		} catch (Exception e) {
			status = false;
			LOG.info(e.getMessage());
			reporter.failureReport("Enter text in :: " + locatorName, msgTypeFailure + testData, driver);
		}
		return status;
	}

	/**
	 * typeUsingJavaScriptExecutor
	 *
	 * @param locator     of (By)
	 * @param testData    of (String)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean typeUsingJavaScriptExecutor(By locator, String testData, String locatorName) throws Throwable {
		boolean status;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + locatorName);
			waitTime();
			WebElement searchbox = driver.findElement(locator);
			JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
			myExecutor.executeScript("arguments[0].value=' " + testData + "'; ", searchbox);
			reporter.SuccessReport("Enter text in :: " + locatorName, msgTypeSuccess + locatorName);
			LOG.info("Clicked on  : " + locatorName);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			status = true;
		} catch (Exception e) {
			status = false;
			LOG.info(e.getMessage());
			reporter.failureReport("Enter text in :: " + locatorName, msgTypeFailure + locatorName, driver);
		}
		return status;
	}

	/**
	 * Moves the mouse to the middle of the element. The element is scrolled
	 * into view and its location is calculated using getBoundingClientRect.
	 *
	 * @param locator : Action to be performed on element (Get it from Object
	 *                repository)
	 */
	private boolean waitForTitlePresent(By locator) throws Throwable {
		boolean flag = false;
		boolean bValue = false;
		try {
			for (int i = 0; i < 200; i++) {
				if (driver.findElements(locator).size() > 0) {
					flag = true;
					bValue = true;
					break;
				} else {
					driver.wait(50);
				}
			}
		} catch (Exception e) {

			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				reporter.failureReport("WaitForTitlePresent :: ", "Title is wrong : ", driver);
			} else {
				reporter.SuccessReport("WaitForTitlePresent :: ", "Launched successfully expected Title : ");
			}
		}
		return bValue;
	}

	/**
	 * getTitle
	 *
	 * @return String
	 * @throws Throwable the throwable
	 */
	public String getTitle() throws Throwable {
		String text = driver.getTitle();
		{
			reporter.SuccessReport("Title :: ", "Title of the page is :: " + text);
		}
		return text;
	}

	/**
	 * assertText
	 *
	 * @param by   of (By)
	 * @param text of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean assertText(By by, String text) throws Throwable {
		boolean flag = false;
		LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
		String actualText = getText(by, text).trim();
		try {
			Assert.assertEquals(actualText, text.trim());
			flag = true;
			return true;
		} catch (Exception e) {

			return false;
		} finally {
			if (!flag) {
				reporter.failureReport("AssertText: Expected: " + text,
						"ActualText: " + actualText + " is not matching.", driver);
			} else {
				reporter.SuccessReport("AssertText: Expected: " + text,
						"ActualText: " + actualText + " is  matching.");
			}
		}
	}

	/**
	 * assertTitle
	 *
	 * @param title of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean assertTitle(String title) throws Throwable {
		boolean flag = false;
		LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
		try {
			By windowTitle = By.xpath("//title[contains(text(),'" + title + "')]");
			if (waitForTitlePresent(windowTitle)) {
				Assert.assertEquals(getTitle(), title);
				flag = true;
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				return true;
			} else {
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (!flag) {
				reporter.failureReport("AsserTitle :: ", "Page title is not matched with : " + title, driver);
			} else {
				reporter.SuccessReport("AsserTitle :: ", " Page title is verified with : " + title);
			}
		}
	}

	/**
	 * getText
	 *
	 * @param locator     of (By)
	 * @param locatorName of (String)
	 * @return String
	 * @throws Throwable the throwable
	 */
	public String getText(By locator, String locatorName) throws Throwable {
		String text = "";
		boolean flag = false;
		LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
		LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
		try {
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element to be present in DOM");
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			text = driver.findElement(locator).getText().trim();
			LOG.info("Locator is Visible and text is retrieved :: " + text);
			reporter.SuccessReport("GetText :: " + locatorName, "" + locatorName + " is :" + text);
		} catch (Exception e) {
			LOG.info("Get text failed due to :: " + e.getMessage());
			reporter.failureReport("GetText :: " + locatorName, "Unable to get Text from :: " + locatorName, driver);
		}
		return text;
	}

	/**
	 * getTextOfSelectedOption -->
	 * selected dropdown item whose selected="selected".
	 * This is used when getText wont work here.
	 * @param locator     of (By)
	 * @param locatorName of (String)
	 * @return String
	 * @throws Throwable the throwable
	 */
	public String getTextOfSelectedOption(By locator, String locatorName) throws Throwable {
		String text = "";
		boolean flag = false;
		LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
		LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
		try {
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element to be present in DOM");
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			text = new Select(driver.findElement(locator)).getFirstSelectedOption().getText();
			text = text.trim();
			LOG.info("Locator is Visible and text is retrieved :: " + text);
			flag = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				reporter.failureReport("GetText :: ", "Unable to get Text from :: " + locatorName, driver);
				LOG.info("GetText :: Unable to get Text from :: " + locatorName);
			} else {
				reporter.SuccessReport("GetText :: " + locatorName, "" + locatorName + " is :" + text);
				LOG.info("Locator is Visible and text is retrieved :: " + text);
			}
		}
		return text;
	}
	public String[] getOptionList(By locator, String locatorName) throws Throwable {
		String[] text;
		boolean flag = false;
		LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
		LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
		try {
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element to be present in DOM");
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			List<WebElement> elms = new Select(driver.findElement(locator)).getOptions();
			text = new String[elms.size()];
			for(int counter = 0; counter <= elms.size()-1; counter++){
				text[counter] = elms.get(counter).getText();
			}
			flag = true;
		} catch (Exception e) {
			throw new Throwable(e.getMessage());
		}
		return text;
	}

	/**
	 * getAttributeByValue
	 *
	 * @param locator     of (By)
	 * @param locatorName of (String)
	 * @return String
	 * @throws Throwable the throwable
	 */
	protected String getAttributeByValue(By locator, String locatorName) throws Throwable {
		String text = "";
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName());
			waitTime();
			if (isElementPresent(locator, locatorName, true)) {
				text = driver.findElement(locator).getAttribute("value");
				LOG.info("Locator is Visible and attribute value is retrieved :: " + text);
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				flag = true;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				reporter.warningReport("GetAttribute :: ", "Unable to get Attribute value from :: " + locatorName);
				LOG.info("GetAttribute :: Unable to get Attribute value from :: " + locatorName);
			} else {
				reporter.SuccessReport("GetAttribute :: ", "Attribute value of " + locatorName + " is :" + text);
				LOG.info("Locator is Visible and attribute value is retrieved :: " + text);
			}
		}
		return text;
	}

	/**
	 * getAttributeByValue
	 *
	 * @param locator     of (By)
	 * @param locatorName of (String)
	 * @return String
	 * @throws Throwable the throwable
	 */
	protected String getAttributeByClass(By locator, String locatorName) throws Throwable {
		String text = "";
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName());
			waitTime();
			if (isElementPresent(locator, locatorName, true)) {
				text = driver.findElement(locator).getAttribute("class");
				LOG.info("Locator is Visible and attribute value is retrieved :: " + text);
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				flag = true;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				reporter.warningReport("GetAttribute :: ", "Unable to get Attribute value from :: " + locatorName);
				LOG.info("GetAttribute :: Unable to get Attribute value from :: " + locatorName);
			} else {
				reporter.SuccessReport("GetAttribute :: ", "" + locatorName + " is" + text);
				LOG.info("Locator is Visible and attribute value is retrieved :: " + text);
			}
		}
		return text;
	}

	/**
	 * Moves the mouse to the middle of the element. The element is scrolled
	 * into view and its location is calculated using getBoundingClientRect.
	 *
	 * @param locator     : Action to be performed on element (Get it from Object
	 *                    repository)
	 * @param locatorName : Meaningful name to the element (Ex:link,menus etc..)
	 */
	public boolean mouseHover(By locator, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Mouse over start :: " + locatorName);
			WebElement mo = this.driver.findElement(locator);
			new Actions(this.driver).moveToElement(mo).build().perform();
			flag = true;
			LOG.info("Mouse over End :: " + locatorName);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++");
			return true;
		} catch (Exception e) {
			//return false;
			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				this.reporter.failureReport("MouseOver :: ", "MouseOver action is not perform on ::" + locatorName,
						this.driver);
			} else {
				this.reporter.SuccessReport("MouseOver :: ", "MouserOver Action is Done on  :: " + locatorName);
			}
		}
	}

	/**
	 * JSClick
	 *
	 * @param locator     of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean JSClick(By locator, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			//added the loggers for click method

			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
			LOG.info("Method : click  ::  Locator : " + locatorName);
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			//internalServerErrorHandler();
			//wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			/*WebElement element = this.driver.findElement(locator);
			new Actions(driver).moveToElement(element).perform();
			LOG.info("Locator is Visible :: " + locator);*/
			this.bringToFocus(locator);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			((JavascriptExecutor)this.driver).executeScript("arguments[0].click();", driver.findElement(locator));
			this.waitForMask();
			flag = true;
		} catch (Exception e) {
			flag = false;
			LOG.info("JSClick failed due to " + e.getMessage());
			//System.out.println("=============================================================" + e.getMessage());
		} finally {
			if (!flag) {
				LOG.info("Inside Finally block");
				this.reporter.failureReport("Click : " + locatorName, "Click is not performed on : " + locatorName, driver);
			} else {
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				this.reporter.SuccessReport("Click : " + locatorName, "Successfully click on  : " + locatorName);
			}
		}
		return flag;
	}

	/**
	 * JSClickUntil
	 *
	 * @param locator     of (By)
	 * @param waitLocator of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean JSClickUntil(By locator, By waitLocator, String locatorName)
			throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			//wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			WebElement element = this.driver.findElement(locator);
			int icounter = 0;
			//((JavascriptExecutor) WebDriver).executeScript("arguments[0].click();", element);
			do {
				icounter = icounter + 1;
				try {
					/* wait = new WebDriverWait(driver, 10*icounter);
					 wait.until(ExpectedConditions.visibilityOfElementLocated(waitLocator));*/
					//wait.until(ExpectedConditions.visibilityOfElementLocated(waitLocator));
					if (isVisibleOnly(waitLocator, "Wait for Element : " + locatorName)) {
						flag = true;
						break;
					} else {
					}
					if (icounter >= 3) {
						flag = false;
						break;
					}
					if (isVisibleOnly(locator, "Wait for Element : " + locatorName)) {
						((JavascriptExecutor) WebDriver).executeScript("arguments[0].click();", element);
					}
				} catch (Exception e) {
					LOG.info("Retrying for the object :: " + waitLocator
							+ " :: Iteration : " + icounter);
				}
			} while (icounter <= 3);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				this.reporter.failureReport("Click : ",
						"Click action is not perform on : " + locatorName, driver);
				//return flag;
			} else {
				this.reporter.SuccessReport("Click : ", "Clicked : " + locatorName);
				//return flag;
			}
		}
		return flag;
	}

	/**
	 * clickUntil
	 *
	 * @param locator     of (By)
	 * @param waitLocator of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean clickUntil(By locator, By waitLocator, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method :" + getCallerMethodName() + "  ::  Locator : " + locatorName);
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element");
			//internalServerErrorHandler();
			//wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOG.info("Locator is Visible :: " + locator);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			LOG.info("Clicked on the Locator");
			int icounter = 0;
			//driver.findElement(locator).click();
			do {
				icounter = icounter + 1;
				try {
					if (isVisibleOnly(waitLocator, "Wait for Element : " + locatorName)) {
						flag = true;
						break;
					} else {
					}
					if (icounter >= 3) {
						flag = false;
						break;
					}
					driver.findElement(locator).click();
				} catch (Exception e) {
					LOG.info("Retrying for the object :: " + waitLocator
							+ " :: Iteration : " + icounter);
				}
			} while (icounter <= 3);

			LOG.info("identified the element :: " + locator);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			reporter.SuccessReport("Click : " + locatorName, msgClickSuccess + locatorName);
		} catch (Exception e) {
			flag = false;
			LOG.info(e.getMessage());
			reporter.failureReport("Click : " + locatorName, msgClickFailure + locatorName, driver);
		}
		return flag;
	}
	/**
	 * methodName: clickUntilByProperty
	 * description: Clicking web element until property value exists
	 * date: 06/21/2017
	 * param: click locator, propertyName, propertyValue for comparision,locatorName for reporting
	 * return: void
	 * author: GallopAuthor004
	 * Updated on:06/21/2017
	 * Updated By:GallopAuthor004
	 * throws: Throwable
	 */
	public boolean clickUntilByProperty(By locator, String propertyName, String propertyValue, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method :" + getCallerMethodName() + "  ::  Locator : " + locatorName);
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element");
			//internalServerErrorHandler();
			//wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOG.info("Locator is Visible :: " + locator);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			LOG.info("Clicked on the Locator");

			int icounter = 0;
			//driver.findElement(locator).click();
			do {
				icounter = icounter + 1;
				try {
					//verifying propertyName and propertyValue Exists
					if (driver.findElement(locator).getAttribute(propertyName).contains(propertyValue)) {
						flag = true;
						break;
					} else {
					}
					if (icounter >= 3) {
						flag = false;
						break;
					}
					//Trying best and worst case scenario
					switch(icounter){
						case 1:
							driver.findElement(locator).click();
							LOG.info("By Native Click");
							break;

						case 2:
							WebElement element = this.driver.findElement(locator);
							((JavascriptExecutor)WebDriver).executeScript("arguments[0].click();", element);
							LOG.info("By JSClick");
							break;
						case 3:
							icounter = 2;
							WebElement mo = this.driver.findElement(locator);
							new Actions(this.driver).click(mo).build().perform();
							LOG.info("By MouseClick");
							break;

					}
				} catch (Exception e) {
				}
			} while (icounter <= 3);

			LOG.info("identified the element :: " + locator);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			reporter.SuccessReport("clickUntilByProperty : " + locatorName, msgClickSuccess + locatorName);
		} catch (Exception e) {
			flag = false;
			LOG.info(e.getMessage());
			reporter.failureReport("clickUntilByProperty : " + locatorName, msgClickFailure + locatorName, driver);
		}
		return flag;
	}


	/**
	 * jsMouseHover
	 *
	 * @param locator     of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean jsMouseHover(By locator, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method :" + getCallerMethodName() + "  ::  Locator : " + locatorName);
			WebElement HoverElement = this.driver.findElement(locator);
			String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
			((JavascriptExecutor) this.WebDriver).executeScript(mouseOverScript, HoverElement);
			LOG.info("JSmousehover is performed  on :: " + locatorName);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (!flag) {
				this.reporter.failureReport("MouseOver : ", "MouseOver action is not perform on : " + locatorName, driver);
			} else {
				this.reporter.SuccessReport("MouseOver : ", "MouserOver Action is Done on" + locatorName);
			}
		}
		return flag;
	}

	/**
	 * getWebElementList
	 *
	 * @param by          of (By)
	 * @param locatorName of (String)
	 * @return List<WebElement>
	 * @throws Throwable the throwable
	 */
	public List<WebElement> getWebElementList(By by, String locatorName) throws Throwable {
		List<WebElement> elements = null;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method :" + getCallerMethodName() + "  ::  Locator : " + locatorName);
			WebDriverWait wait = new WebDriverWait(this.driver, wait_Time);
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
			elements = driver.findElements(by);
			LOG.info("Size of List ::" + elements.size());
			for (WebElement element:elements) {
				LOG.info("List value are :: " + element.getText());
			}
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (Exception e) {
			e.getMessage();
		}
		return elements;
	}

	/**
	 * elementVisibleTime
	 *
	 * @param locator of (By)
	 * @throws Throwable the throwable
	 */
	public void elementVisibleTime(By locator) throws Throwable {
		float timeTaken;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			long start = System.currentTimeMillis();
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			long stop = System.currentTimeMillis();
			timeTaken = (stop - start) / 1000;
			LOG.info("Took : " + timeTaken + " secs to display the results : ");
			reporter.SuccessReport("Total time taken for element visible :: ", "Time taken load the element :: " + timeTaken + " seconds");
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * Moves the mouse to the middle of the element. The element is scrolled
	 * into view and its location is calculated using getBoundingClientRect.
	 *
	 * @param destinationLocator : Action to be performed on element (Get it from Object
	 *                           repository)
	 * @param locatorName        : Meaningful name to the element (Ex:link,menus etc..)
	 */

	protected boolean dragAndDrop(By souceLocator, By destinationLocator, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
			Actions builder = new Actions(this.driver);
			WebElement souceElement = this.driver.findElement(souceLocator);
			WebElement destinationElement = this.driver.findElement(destinationLocator);
			/*Action dragAndDrop = builder.clickAndHold(souceElement).moveToElement(destinationElement)
					.release(destinationElement).build();
			dragAndDrop.perform();*/
			builder.dragAndDrop(souceElement, destinationElement).build().perform();
			flag = true;
			LOG.info("drag and drop performed ");
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				this.reporter.failureReport("DragDrop : ", "Drag and Drop action is not performed on : " + locatorName,
						this.driver);
			} else {
				this.reporter.SuccessReport("DragDrop : ", "Drag and Drop Action is Done on : " + locatorName);
			}
		}
	}

	/**
	 * navigateTo
	 *
	 * @param Url of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean navigateTo(String Url) throws Throwable {
		boolean flag = false;
		try {
			waitTime();
			WebDriver.navigate().to(Url);
			LOG.info("Navigated URL is : " + Url);
			flag = true;
		} catch (Exception e) {
			flag = false;
			LOG.info(e.getMessage());
		} finally {
			if (!flag) {
				reporter.failureReport("Unable to Open : ", Url, driver);
			} else {
				reporter.SuccessReport("Successfully Opened : ", Url);
			}
		}
		return flag;
	}

	/**
	 * generateRandomNumber
	 *
	 * @return int
	 * @throws Throwable the throwable
	 */
	public long generateRandomNumber() throws Throwable {
		/*Random generator = new Random();
		return generator.nextInt(9999) + 10000;*/
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyyhhmmss");
		String currentdate = sdf.format(date);
		long ran=Long.parseLong(currentdate);
		return ran;

	}

	/**
	 * rightClick
	 *
	 * @param locator     of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean rightClick(By locator, String locatorName) throws Throwable {
		boolean status;
		String msgRightClickSuccess = "Successfully Mouse Right Clicked On ";
		String msgRightClickFailure = "Unable To Right Click On ";
		try {
			//added loggers
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			Actions action = new Actions(driver);
			action.contextClick(driver.findElement(locator)).build().perform();
			driver.findElement(locator).click();
			reporter.SuccessReport("Click : " + locatorName, msgRightClickSuccess + locatorName);
			LOG.info("Right click performed  on :: " + locatorName);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			status = true;
		} catch (Exception e) {
			status = false;
			LOG.info(e.getMessage());
			reporter.failureReport("Click : " + locatorName, msgRightClickFailure + locatorName, driver);
		}
		return status;
	}

	/**
	 * dynamicWait
	 *
	 * @param locator of (By)
	 * @throws Throwable the throwable
	 */
	public void dynamicWait(By locator) throws Throwable {
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locator);
			String time = ReporterConstants.DYNAMIC_TIMEOUT;
			Duration timevalue = ofSeconds(Integer.parseInt(time));
			WebDriverWait wait = new WebDriverWait(driver, timevalue);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			LOG.info(locator + ":: displayed succussfully");
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (Exception e) {
			LOG.info(e.getMessage());
			//reporter.failureReport("Unable to find Element :: " + locator, msgIsElementFoundFailure + locator, driver);
			//throw new RuntimeException(e);

		}
	}
	/**
	 * Double click the mouse to the middle of the element. The element is scrolled
	 * into view and its location is calculated using getBoundingClientRect.
	 *
	 * @param locator     : Action to be performed on element (Get it from Object
	 *                    repository)
	 * @param locatorName : Meaningful name to the element (Ex:link,menus etc..)
	 */
	public boolean mouseDoubleClick(By locator, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Mouse Double Click start :: " + locatorName);
			WebElement mo = this.driver.findElement(locator);
			new Actions(this.driver).moveToElement(mo).doubleClick(mo).build().perform();
			flag = true;
			LOG.info("Mouse Double Click :: " + locatorName);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++");
			return true;
		} catch (Exception e) {
			//return false;
			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				this.reporter.failureReport("double Click :: ", "double Click action is not perform on ::" + locatorName,
						this.driver);
			} else {
				this.reporter.SuccessReport("double Click :: ", "double Click Action is Done on  :: " + locatorName);
			}
		}
	}

	/**
	 * click the mouse to the middle of the element. The element is scrolled
	 * into view and its location is calculated using getBoundingClientRect.
	 *
	 * @param locator     : Action to be performed on element (Get it from Object
	 *                    repository)
	 * @param locatorName : Meaningful name to the element (Ex:link,menus etc..)
	 */
	public boolean mouseClick(By locator, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Mouse Double Click start :: " + locatorName);
			WebElement mo = this.driver.findElement(locator);
			new Actions(this.driver).click(mo).build().perform();
			flag = true;
			LOG.info("Mouse Double Click :: " + locatorName);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++");
			return true;
		} catch (Exception e) {
			//return false;
			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				this.reporter.failureReport("Click :: ", "Click action is not perform on ::" + locatorName,
						this.driver);
			} else {
				this.reporter.SuccessReport(" Click :: ", " Click Action is Done on  :: " + locatorName);
			}
		}
	}

	/**
	 * getYear, Function to get required year e.g: 0-Current year, 1-Next year,
	 *
	 * @param number of (int) Number to get year (e.g: -1,0,1 etc)
	 * @return int
	 * @throws Throwable the throwable
	 */
	protected int getYear(int number) throws Throwable {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR) + number;
		LOG.info("Year is : " + year);
		return year;
	}

	/**
	 * dateFormatVerification, Function to verify date format by giving actual date
	 *
	 * @param actualDate     of (String) actual date e.g: 21-11-2015
	 * @param formatToVerify of (String) format type e.g: dd-MM-yyyy
	 * @return boolean
	 */
	protected boolean dateFormatVerification(String actualDate, String formatToVerify) {
		boolean flag = false;
		if (actualDate.toLowerCase().contains("am")) {
			flag = formatVerify(actualDate, formatToVerify);
		} else if (actualDate.toLowerCase().contains("pm")) {
			flag = formatVerify(actualDate, formatToVerify);
		} else if (!actualDate.toLowerCase().contains("am") || !actualDate.toLowerCase().contains("pm")) {
			flag = formatVerify(actualDate, formatToVerify);
		}
		return flag;
	}

	/**
	 * formatVerify, Reusable Function to verify date format by giving actual date
	 *
	 * @param actualDate     of (String)e.g: 21-11-2015
	 * @param formatToVerify of (String) type e.g: dd-MM-yyyy
	 * @return : boolean
	 */
	public boolean formatVerify(String actualDate, String formatToVerify) {
		boolean flag;
		try {
			SimpleDateFormat sdf;
			sdf = new SimpleDateFormat(formatToVerify);
			Date date = sdf.parse(actualDate);
			String formattedDate = sdf.format(date);
			flag = actualDate.equals(formattedDate);
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * replaceAll, Function to replace the regular expression values with client required values
	 *
	 * @param text        of (String)
	 * @param pattern     of (String), regular expression of actual value
	 * @param replaceWith of (String), value to replace the actual
	 * @return : String
	 */
	public String replaceAll(String text, String pattern, String replaceWith) {
		String flag = null;
		try {
			flag = text.replaceAll(pattern, replaceWith);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * subString, Function to get sub string of given actual string text
	 *
	 * @param text       of (String), Actual text
	 * @param startIndex of (int), Start index of sub string
	 * @param endIndex   of (int), end index of sub string
	 * @return : String
	 */
	protected String subString(String text, int startIndex, int endIndex) {
		String flag = null;
		try {
			flag = text.substring(startIndex, endIndex);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * getCssValue, Function to get the value of a given CSS property (e.g. width)
	 *
	 * @param locator  of (By)
	 * @param cssValue of (String), CSS property
	 * @return : String
	 */
	public String getCssValue(By locator, String cssValue) {
		String result = "";
		try {
			result = this.driver.findElement(locator).getCssValue(cssValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * getBackGroundColor, Function to get the background color of a given web element (e.g. background-color)
	 *
	 * @param locator  of (By)
	 * @param cssValue of (String), CSS property (e.g. background-color)
	 * @return : String
	 */
	public String getBackGroundColor(By locator, String cssValue) {
		String hexColor = "";
		try {
			String bColor = this.driver.findElement(locator).getCssValue(cssValue);
			hexColor = Color.fromString(bColor).asHex();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return hexColor;
	}

	/**
	 * switchToFrame, Function to switch to frame
	 *
	 * @param locator of (By)
	 */
	protected void switchToFrame(By locator) {
		LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
		WebDriverWait wait = new WebDriverWait(driver, wait_Time);
		LOG.info("Waiting for element");
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		LOG.info("Locator is Visible :: " + locator);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		driver.switchTo().frame(driver.findElement(locator));
	}

	/**
	 * getCurrentDateTime, Function to get current time in client required format
	 *
	 * @param dateTimeFormat of (String), format to get date and time (e.g: h:mm)
	 * @return : String
	 */
	public String getCurrentDateTime(String dateTimeFormat) throws Throwable {
		DateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * getFutureDateTime, Function to get future or past date in client required format
	 *
	 * @param dateTimeFormat of (String), format to get date and time (e.g: MM/dd/yyyy)
	 * @param days           of (int), number to get date E.g. 1:Tomorrow date, -1: Yesterday date
	 * @return : String
	 */
	public String getFutureDateTime(String dateTimeFormat, int days) throws Throwable {
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, days);
		Date tomorrow = calendar.getTime();
		return sdf.format(tomorrow);
	}

	/**
	 * getCountryDateTime, Function to get future or past date in client required format
	 *
	 * @param dateTimeFormat of (String), format to get date and time (e.g: MM/dd/yyyy)
	 * @param days           of (int), number to get date E.g. 1:Tomorrow date, -1: Yesterday date
	 * @param timeZone       of (String), time format to get date E.g. :America/New_York
	 * @return : String
	 */
	public String getCountryDateTime(String dateTimeFormat, int days, String timeZone) throws Throwable {
		Calendar calNewYork = Calendar.getInstance();
		calNewYork.add(Calendar.DAY_OF_YEAR, 0);
		Date date = calNewYork.getTime();
		DateFormat formatter = new SimpleDateFormat(dateTimeFormat);
		formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
		return formatter.format(date);
	}

	/**
	 * assertTextStringContains, Assert text string matching.
	 *
	 * @param actText of (String)
	 * @param expText of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean assertTextStringContains(String actText, String expText) throws Throwable {
		boolean flag = false;
		try {
			// added loggers
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			String ActualText = actText.trim();
			LOG.info("act - " + ActualText);
			LOG.info("exp - " + expText);
			if (ActualText.contains(expText.trim())) {
				LOG.info("in if loop");
				flag = true;
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				return true;
			} else {
				LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			if (!flag) {
				reporter.failureReport("Verify : " + expText, actText + " is not present in the element : ", driver);
			} else {
				reporter.SuccessReport("Verify : " + expText, actText + " is  present in the element : ");
			}
		}
	}

	/**
	 * deleteDirectory, Delete directory from local machine
	 *
	 * @param directoryPath of (String),  path for the directory to delete
	 */
	public void deleteDirectory(String directoryPath) throws IOException {
		FileUtils.deleteDirectory(new File(directoryPath));
	}

	/**
	 * getRandomString, Get random String
	 *
	 * @param noOfCharacters of (int), Number of characters to get randomly
	 * @return String
	 */
	public String getRandomString(int noOfCharacters) throws IOException {
		return RandomStringUtils.randomAlphabetic(noOfCharacters);
	}

	/**
	 * getRandomNumeric, Get random Numeric
	 *
	 * @param noOfCharacters of (int),  Number of characters to get randomly
	 * @return String
	 */
	protected String getRandomNumeric(int noOfCharacters) throws IOException {
		return RandomStringUtils.randomNumeric(noOfCharacters);
	}

	/**
	 * getAttributeValue, Function to get the value of a given attribute (e.g. class)
	 *
	 * @param locator       of (By)
	 * @param attributeName of (String)
	 * @return : String
	 * @throws Throwable
	 */
	public String getAttributeValue(By locator, String attributeName) throws Throwable {
		String result = "";
		try {
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			result = this.driver.findElement(locator).getAttribute(attributeName).trim();
			reporter.SuccessReport("Get attribute on element.", attributeName + " attribute in element: '" + result + "'");
		} catch (Exception ex) {
			reporter.failureReport("Get attribute on element failed.", "Get attribute of '" + attributeName + "' failed due to " + ex.getMessage(), driver);
		}
		return result;
	}

	/**
	 * refreshPage
	 */
	public void refreshPage() throws Throwable {
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName());
			driver.navigate().refresh();
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (Exception e) {
			LOG.info("++++++++++++++++++++++++++++Catch Block Start+++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());

			LOG.info("++++++++++++++++++++++++++++Catch Block End+++++++++++++++++++++++++++++++++++++++++++");
		}
	}

	/**
	 * clearData, Clear value from textBox
	 *
	 * @param locator of (By)
	 */
	protected void clearData(By locator) throws Throwable {
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName());
			WebElement element = driver.findElement(locator);
			element.sendKeys(Keys.CONTROL + "a");
			element.sendKeys(Keys.DELETE);
			element.clear();
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (Exception e) {
			LOG.info("++++++++++++++++++++++++++++Catch Block Start+++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());

			LOG.info("++++++++++++++++++++++++++++Catch Block End+++++++++++++++++++++++++++++++++++++++++++");
		}
	}

	/**
	 * keyBoardOperations
	 *
	 * @param locator     of (By)
	 * @param testData    of (Keys)
	 * @param locatorName of (String)
	 * @return boolean
	 */
	public boolean keyBoardOperations(By locator, Keys testData, String locatorName) throws Throwable {
		boolean status;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : Type  ::  Locator : " + locatorName + " :: Data :" + testData);
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element :");
			//wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOG.info("Locator is Visible :: " + locator);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			driver.findElement(locator).sendKeys(testData);
			LOG.info("Typed the Locator data :: " + testData);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

			reporter.SuccessReport("Enter text in :: " + locatorName, msgTypeSuccess + testData);
			status = true;
		} catch (Exception e) {
			status = false;
			LOG.info(e.getMessage());
			reporter.failureReport("Enter text in :: " + locatorName, msgTypeFailure + testData, driver);
		}
		return status;
	}

	/**
	 * Switch to frame using index value
	 *
	 * @param index of (int), frame number to switch
	 */
	public void switchToFrameByIndex(int index) {
		driver.switchTo().frame(index);
	}

	/**
	 * come out from frame
	 */
	public void comeOutFromFrame() {
		driver.switchTo().defaultContent();
	}

	/**
	 * Click on OK button on alert
	 */
	protected void acceptAlert() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.alertIsPresent());
			driver.switchTo().alert().accept();
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
	}

	/**
	 * findWebElement
	 *
	 * @param locator     of (By)
	 * @param locatorName of (String)
	 * @return WebElement
	 */
	protected WebElement findWebElement(By locator, String locatorName) throws Throwable {
		WebElement element;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : click  ::  Locator : " + locatorName);
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element");
			LOG.info("Locator is Visible :: " + locator);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			LOG.info("Clicked on the Locator");
			element = driver.findElement(locator);
			LOG.info("identified the element :: " + locator);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (Exception e) {
			LOG.info(e.getMessage());

			throw new RuntimeException(e);
		}
		return element;
	}

	/**
	 * checkBoxIsChecked
	 *
	 * @param by          of (By)
	 * @return boolean
	 */
	protected boolean checkBoxStatus(By by) throws Throwable {
		boolean status = false;
		try {
			status = driver.findElement(by).isSelected();
		} catch (Exception e) {
			LOG.info(e.getMessage());
		} finally {
			return status;
		}
	}

	public void checkBoxIsChecked(By by, String locatorName, boolean expected) throws Throwable {
		boolean status = false;
		try {
			status = driver.findElement(by).isSelected();
		} catch (Exception e) {
			status = false;
			LOG.info(e.getMessage());
		} finally {
			if(status == expected){
				reporter.SuccessReport("Checkbox status" , "Status of checkbox " + locatorName + " is " + expected);
			}else{
				reporter.failureReport("Checkbox status" , "Status of checkbox " + locatorName + " is " + status + " not " + expected, driver);
			}
		}
	}
	/**
	 * switchToWindow, Function to switch to latest window
	 */
	public void switchToWindow() {
		for (String handle : driver.getWindowHandles()) {
			driver.switchTo().window(handle);
		}
	}

	/**
	 * switchToWindow, Function to switch to latest window
	 */
	public void switchToWindow(String mainWindow) {
		for (String handle : driver.getWindowHandles()) {
			if(handle!=mainWindow)
				driver.switchTo().window(handle);
		}
	}
	/**
	 * switchToParentWindow, Function to switch to parent window
	 *
	 * @param handle of (String), window handle to switch
	 */
	public void switchToParentWindow(String handle) {
		driver.switchTo().window(handle);
	}

	/**
	 * closeWindow, Function to close the currently focused window
	 */
	protected void closeWindow() {
		driver.close();
	}

	/**
	 * getWindowHandle, Function to get the current window handle
	 *
	 * @return : String
	 */
	public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	/**
	 * scrollToWebElement, Function to scroll to a particular element
	 *
	 * @param element of (By)
	 */
	public void scrollToWebElement(By element){
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(element));
		}catch(Exception e){
			LOG.info("Scroll to focus failed due to ::: " + e.getMessage());
		}catch(Throwable t){
			LOG.info("Scroll to focus failed due to ::: " + t.getMessage());
		}
	}

	protected void deleteSpecificFile(String fileName) throws InterruptedException {
		try {
			File file = new File(fileName);
			if (file.delete()) {
				LOG.info(file.getName() + " is deleted!");
			} else {
				LOG.info("Delete operation is failed.");
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * findWebElementVisibility, Function returns WebElement
	 *
	 * @return : WebElement
	 */

	protected WebElement findWebElementVisibility(By locator, String locatorName) throws Throwable {
		WebElement element;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : click  ::  Locator : " + locatorName);
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element");
			LOG.info("Locator is Visible :: " + locator);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOG.info("Element Found on the Locator");
			element = driver.findElement(locator);
			LOG.info("identified the element :: " + locator);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (Exception e) {
			LOG.info(e.getMessage());

			throw new RuntimeException(e);
		}
		return element;
	}

	protected boolean isCheckBoxSelected(By locator) {
		return driver.findElement(locator).isSelected();
	}

	/**
	 * isVisible
	 *
	 * @param locator     of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	public boolean isVisibleOnly(By locator, String locatorName) throws Throwable {
		boolean flag;
		try {
			//added loggers
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name :: " + getCallerClassName() + " Method name :: " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
			flag = driver.findElement(locator).isDisplayed();
			//value = true;
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	protected long differenceBetweenTwoDates(String date1, String date2, String dateFormat) throws Throwable {
		long diffDays = 0;
		try {
			SimpleDateFormat format = new SimpleDateFormat(dateFormat);
			Date d1 = format.parse(date1);
			Date d2 = format.parse(date2);
			long diff = d2.getTime() - d1.getTime();
			diffDays = diff / (24 * 60 * 60 * 1000) + 1;
		} catch (Exception e) {
			e.getMessage();
		}
		return diffDays;
	}

	/**
	 * clickUntilElementNotVisiable
	 *
	 * @param locator     of (By)
	 * @param waitLocator of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean clickUntilElementNotVisible(By locator, By waitLocator, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method :" + getCallerMethodName() + "  ::  Locator : " + locatorName);
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element");
			//internalServerErrorHandler();
			//wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOG.info("Locator is Visible :: " + locator);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			LOG.info("Clicked on the Locator");
			int icounter = 0;
			do {
				icounter = icounter + 1;
				try {
					if (icounter > 0) {

						if (!isVisibleOnly(waitLocator, "Wait for Element : " + locatorName)) {
							flag = true;
							break;
						}
					}
					if (icounter >= 3) {
						flag = false;
						break;
					}
					/*
					 *WebElement element = this.driver.findElement(locator);
					 *((JavascriptExecutor)WebDriver).executeScript("arguments[0].click();", element);
					 */
					click(locator, locatorName);
				} catch (Exception e) {
					LOG.info("Retrying for the object :: " + waitLocator
							+ " :: Iteration : " + icounter);
				}
			} while (icounter <= 3);

			LOG.info("identified the element :: " + locator);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			reporter.SuccessReport("Click : " + locatorName, msgClickSuccess + locatorName);
		} catch (Exception e) {
			flag = false;
			LOG.info(e.getMessage());
			reporter.failureReport("Click : " + locatorName, msgClickFailure + locatorName, driver);
		}
		return flag;
	}

	/**
	 * convertDateFormatToAnotherDateFormat, Function to convert one date format to another date format
	 *
	 * @param actualDate        of (String), Actual date (e.g: Dec 5, 2017)
	 * @param sourceFormat      of (String), format of actualDate (e.g: MMM dd, yyyy)
	 * @param destinationFormat of (String), Format what we required (e.g: dd/MM/yyyy)
	 * @return : String
	 */
	protected String convertDateFormatToAnotherDateFormat(String actualDate, String sourceFormat, String destinationFormat) throws Throwable {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sourceFormat);
		SimpleDateFormat sdf = new SimpleDateFormat(destinationFormat);
		Date date = simpleDateFormat.parse(actualDate);
		return sdf.format(date);
	}

	/**
	 * clickUntil
	 *
	 * @param locator     of (By)
	 * @param waitLocator of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean mouseDoubleClickUntil(By locator, By waitLocator, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method :" + getCallerMethodName() + "  ::  Locator : " + locatorName);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			LOG.info("Waiting for element");
			//internalServerErrorHandler();
			//wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOG.info("Locator is Visible :: " + locator);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			LOG.info("Clicked on the Locator");

			int icounter = 0;
			do {
				icounter = icounter + 1;
				try {
					if (isVisibleOnly(waitLocator, "Wait for Element : " + locatorName)) {
						flag = true;
						break;
					} else {
					}
					if (icounter >= 3) {
						flag = false;
						break;
					}
					LOG.info("+++++++++++++++++++++++++++++++++++++++++++++");
					LOG.info("Mouse Double Click start :: " + locatorName);
					WebElement mo = this.driver.findElement(locator);
					new Actions(this.driver).moveToElement(mo).doubleClick(mo).build().perform();
					flag = true;
					LOG.info("Mouse Double Click :: " + locatorName);
					LOG.info("+++++++++++++++++++++++++++++++++++++++++++++");
					return true;
				} catch (Exception e) {
					LOG.info("Retrying for the object :: " + waitLocator
							+ " :: Iteration : " + icounter);
				}
			} while (icounter <= 3);

			LOG.info("identified the element :: " + locator);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			reporter.SuccessReport("Click : " + locatorName, msgClickSuccess + locatorName);
		} catch (Exception e) {
			flag = false;
			LOG.info(e.getMessage());
			reporter.failureReport("Click : " + locatorName, msgClickFailure + locatorName, driver);
		}
		return flag;
	}

	/**
	 * mouseJSDoubleClickUntil
	 *
	 * @param locator     of (By)
	 * @param waitLocator of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean mouseJSDoubleClickUntil(By locator, By waitLocator, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method :" + getCallerMethodName() + "  ::  Locator : " + locatorName);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			LOG.info("Waiting for element");
			//internalServerErrorHandler();
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOG.info("Locator is Visible :: " + locator);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			LOG.info("Clicked on the Locator");

			int icounter = 0;
			do {
				icounter = icounter + 1;
				try {
					if (isVisibleOnly(waitLocator, "Wait for Element : " + locatorName)) {
						flag = true;
						break;
					} else {
					}
					if (icounter >= 3) {
						flag = false;
						break;
					}
					LOG.info("+++++++++++++++++++++++++++++++++++++++++++++");
					LOG.info("Mouse Double Click start :: " + locatorName);
					WebElement mo = this.driver.findElement(locator);
					((JavascriptExecutor) driver).executeScript("var evt = document.createEvent('MouseEvents'); evt.initMouseEvent('dblclick',true, "
							+ "true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null); arguments[0].dispatchEvent(evt);",mo);
					flag = true;
					LOG.info("Mouse Double Click :: " + locatorName);
					LOG.info("+++++++++++++++++++++++++++++++++++++++++++++");
					return true;
				} catch (Exception e) {
					LOG.info("Retrying for the object :: " + waitLocator
							+ " :: Iteration : " + icounter);
				}
			} while (icounter <= 3);

			LOG.info("identified the element :: " + locator);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			reporter.SuccessReport("Click : " + locatorName, msgClickSuccess + locatorName);
		} catch (Exception e) {
			flag = false;
			LOG.info(e.getMessage());
			reporter.failureReport("Click : " + locatorName, msgClickFailure + locatorName, driver);
		}
		return flag;
	}

	/**
	 * Double click the mouse to the middle of the element. The element is scrolled
	 * into view and its location is calculated using getBoundingClientRect.
	 *
	 * @param locator     : Action to be performed on element (Get it from Object
	 *                    repository)
	 * @param locatorName : Meaningful name to the element (Ex:link,menus etc..)
	 */
	public boolean mouseJSDoubleClick(By locator, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Mouse Double Click start :: " + locatorName);
			WebElement mo = this.driver.findElement(locator);
			((JavascriptExecutor) driver).executeScript("var evt = document.createEvent('MouseEvents'); evt.initMouseEvent('dblclick',true, "
					+ "true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null); arguments[0].dispatchEvent(evt);",mo);
			flag = true;
			LOG.info("Mouse Double Click :: " + locatorName);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++");
			return true;
		} catch (Exception e) {
			//return false;
			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				this.reporter.failureReport("double Click :: ", "double Click action is not perform on ::" + locatorName,
						this.driver);
			} else {
				this.reporter.SuccessReport("double Click :: ", "double Click Action is Done on  :: " + locatorName);
			}
		}
	}

	/**
	 * mouseHoverUntil
	 *
	 * @param locator     of (By)
	 * @param waitLocator of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean mouseHoverUntil(By locator, By waitLocator, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method :" + getCallerMethodName() + "  ::  Locator : " + locatorName);
			LOG.info("Mouse Hover start :: " + locatorName);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			LOG.info("Waiting for element");
			//internalServerErrorHandler();
			//wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOG.info("Locator is Visible :: " + locator);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			LOG.info("Clicked on the Locator");

			int icounter = 0;
			do {
				icounter = icounter + 1;
				try {
					WebElement mo = this.driver.findElement(locator);
					new Actions(this.driver).moveToElement(mo).build().perform();
					if (isVisibleOnly(waitLocator, "Wait for Element : " + locatorName)) {
						flag = true;
						break;
					} else {
						LOG.info("Mouse Hover Attempt >>> " + icounter);
					}
					if (icounter >= 3) {
						flag = false;
						break;
					}

					LOG.info("+++++++++++++++++++++++++++++++++++++++++++++");
					return true;
				} catch (Exception e) {
					LOG.info("Retrying for the object :: " + waitLocator
							+ " :: Iteration : " + icounter);
				}
			} while (icounter <= 3);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (Exception e) {
			flag = false;
			LOG.info(e.getMessage());
		} finally {
			if (!flag) {//flag=true
				if (reportIndicator) {//yes
					reporter.failureReport("Mouse Hover :: " + locatorName, "Unable To Mouse Hover On " + locatorName, driver);
				}
			} else{
				reporter.SuccessReport("Mouse Hover :: " + locatorName, "Successfully Mouse Hover On" + locatorName);
			}
		}
		reportIndicator = true;

		return flag;
	}

	/**
	 * mouseHoverUntilGetProperty
	 *
	 * @param locator     of (By)
	 * @paramwaitLocator of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected String mouseHoverUntilGetProperty(By locator, By propertyLocator, String locatorName) throws Throwable {
		boolean flag = false;
		String text="";
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method :" + getCallerMethodName() + "  ::  Locator : " + locatorName);
			LOG.info("Mouse Hover start :: " + locatorName);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			LOG.info("Waiting for element");
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOG.info("Locator is Visible :: " + locator);
			LOG.info("Clicked on the Locator");

			int icounter = 0;
			do {
				icounter = icounter + 1;
				try {
					WebElement mo = this.driver.findElement(locator);
					new Actions(this.driver).moveToElement(mo).build().perform();
					if (isVisibleOnly(propertyLocator, "Wait for Element : " + locatorName)) {
						flag = true;
						text = driver.findElement(propertyLocator).getText().trim();
						break;
					} else {
						LOG.info("Mouse Hover Attempt >>> " + icounter);
					}
					if (icounter >= 3) {
						flag = false;
						break;
					}

					LOG.info("+++++++++++++++++++++++++++++++++++++++++++++");
					flag = true;
				} catch (Exception e) {
					LOG.info("Retrying for the object :: " + propertyLocator
							+ " :: Iteration : " + icounter);
				}
			} while (icounter <= 3);
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (Exception e) {
			flag = false;
			LOG.info(e.getMessage());
		} finally {
			if (!flag) {//flag=true
				if (reportIndicator) {//yes
					reporter.failureReport("Mouse Hover :: " + locatorName, "Unable To Mouse Hover On " + locatorName, driver);
				}
			} else{
				reporter.SuccessReport("Mouse Hover :: " + locatorName, "Successfully Mouse Hover On" + locatorName);
			}
		}
		reportIndicator = true;
		return text;
	}

	protected static float roundToDecimals(double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	/**
	 * Get text from PDF file.
	 *
	 * @return (String) text from PDF file
	 * @throws Throwable the throwable
	 * @param pdfFilePath (String) path of the PDF file
	 */
	public String getTextFromPDF(String pdfFilePath) throws Throwable {
		String parsedText = null;
		try{
			File pdfFile = new File(pdfFilePath);

			PDFParser parser = new PDFParser((RandomAccessRead) new FileInputStream(pdfFile));
			parser.parse();

			COSDocument cosDoc = parser.parse().getDocument();
			PDDocument pdDoc = new PDDocument(cosDoc);

			PDFTextStripper pdfStripper = new PDFTextStripper();
			parsedText = pdfStripper.getText(pdDoc);
			LOG.info("Text in PDF is: \n" + parsedText);
			parser.parse().getDocument().close();
		}catch(Exception e){
			e.printStackTrace();
			reporter.failureReport("Get text from PDF", "Exception while reading data from PDF file", driver);
		}
		return parsedText;
	}
	/**
	 * Get text from PDF file.
	 *
	 * @return (String) text from PDF file
	 * @throws Throwable the throwable
	 * @param pdfFilePath (String) path of the PDF file
	 */
	protected String getTextFromPDFWithWaitLocater(String pdfFilePath, By WaitLocater) throws Throwable {
		String parsedText = null;
		try{
			File pdfFile = new File(pdfFilePath);

			PDFParser parser = new PDFParser((RandomAccessRead) new FileInputStream(pdfFile));
			parser.parse();

			COSDocument cosDoc = parser.parse().getDocument();
			PDDocument pdDoc = new PDDocument(cosDoc);

			PDFTextStripper pdfStripper = new PDFTextStripper();
			dynamicWaitByLocator(WaitLocater, Duration.ofSeconds(4));
			parsedText = pdfStripper.getText(pdDoc);
			LOG.info("Text in PDF is: \n" + parsedText);
			parser.parse().getDocument().close();
		}catch(Exception e){
			reporter.failureReport("Get text from PDF", "Exception while reading data from PDF file", driver);
		}
		return parsedText;
	}
	/**
	 * Function to get attribute name from focused element
	 * If we use this method it will return required attribute value of focused element
	 *
	 * @throws Throwable the throwable
	 */
	protected String getAttributeFromFocusedElement(String attributeName) throws Throwable {
		WebElement activeElement = driver.switchTo().activeElement();
		return activeElement.getAttribute(attributeName);
	}

	/**
	 * Function to get highlighted text
	 * If we use this method it will return required text of highlighted element
	 *
	 * @throws Throwable the throwable
	 */
//	protected String getHighlightedText() throws Throwable {
//		return (String) driver.executeScript("return window.getSelection().toString();");
//	}

	/**
	 * Function to get value from XML file
	 * If we use this method it will return required value based on tag name
	 *
//	 * @param xmlFileName (String) path of the XML file
//	 * @param parentTag (String) Parent tag name
//	 * @param childTag (String) Child tag name
	 */
//	protected String readXML(String xmlFileName, String parentTag, String childTag){
//		String tagName = "";
//		try {
//			File fXmlFile = new File(xmlFileName);
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(fXmlFile);
//			doc.getDocumentElement().normalize();
//
//			NodeList nList = doc.getElementsByTagName(parentTag);
//			Node nNode;
//			Element eElement;
//			for (int temp = 0; temp < nList.getLength(); temp++) {
//				nNode = nList.item(temp);
//				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//					eElement = (Element) nNode;
//					tagName = eElement.getElementsByTagName(childTag).item(0).getTextContent();
//				}
//			}
//		} catch (Exception e) {
//		}
//		return tagName;
//	}

	protected void scrollBottom()
	{
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,1500)");
	}

	protected boolean contains(String value1, String value2) throws Throwable {
		boolean status = false;
		if(value1.indexOf(value2)>-1){
			reporter.SuccessReport("Expect to contain", "Verified \"" + value2 + "\" is present in \"" + value1 + "\".");
			status = true;
		}else{
			reporter.failureReport("Expect to contain", "Verification \"" + value2 + "\" is present in \"" + value1 + "\" failed.");
		}
		return status;
	};

	//Wait Until JQuery and JS Ready
	public void waitUntilJQueryReady() throws InterruptedException {
		JavascriptExecutor jsExec = (JavascriptExecutor) driver;

		//First check that JQuery is defined on the page. If it is, then wait AJAX
		Boolean jQueryDefined = (Boolean) jsExec.executeScript("return typeof jQuery != 'undefined'");
		if (jQueryDefined == true) {
			//Pre Wait for stability
			Thread.sleep(500);

			//Wait JQuery Load
			waitForJQueryLoad();

			//Wait JS Load
			waitUntilJSReady();

			//Post Wait for stability
			Thread.sleep(500);
		}  else {
			System.out.println("jQuery is not defined on this site!");
		}
	}

	private void waitForJQueryLoad() {
		JavascriptExecutor jsExec = (JavascriptExecutor) driver;
		//Wait for jQuery to load
		ExpectedCondition<Boolean> jQueryLoad = driver -> ((Long) jsExec.executeScript("return jQuery.active") == 0);

		//Get JQuery is Ready
		boolean jqueryReady = (Boolean) jsExec.executeScript("return jQuery.active==0");

		//Wait JQuery until it is Ready!
		if(!jqueryReady) {
			System.out.println("JQuery is NOT Ready!");
			//Wait for jQuery to load
			new WebDriverWait(driver, wait_Time).until(jQueryLoad);
		} else {
			System.out.println("JQuery is Ready!");
		}
	}
	//Wait Until JS Ready
	private void waitUntilJSReady() {
		WebDriverWait wait = new WebDriverWait(driver,wait_Time);
		JavascriptExecutor jsExec = (JavascriptExecutor) driver;

		//Wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) driver)
				.executeScript("return document.readyState").toString().equals("complete");

		//Get JS is Ready
		boolean jsReady =  (Boolean) jsExec.executeScript("return document.readyState").toString().equals("complete");

		//Wait Javascript until it is Ready!
		if(!jsReady) {
			System.out.println("JS in NOT Ready!");
			//Wait for Javascript to load
			wait.until(jsLoad);
		} else {
			System.out.println("JS is Ready!");
		}
	}
	//Function to wait for policy/endorsement submitted
	public void waitForBodyTag(){
		System.out.println("----------Inside wait for body tag-----------");
		//Wait for body tag to have x-reset attribute
		ExpectedCondition<Boolean> bodyAttribute = driver -> (Boolean)(this.getBodyTag());
		new WebDriverWait(driver,Duration.ofSeconds(600)).until(bodyAttribute);//600 seconds = 10 mins
		System.out.println("----------Exiting wait for body tag--------------");
	}
	private boolean getBodyTag(){
		String temp = (String) ((JavascriptExecutor) driver).executeScript("return document.querySelector('body').getAttribute('onload')");
		if(temp == null){return true;}else{return false;}
	}

	public void deleteDownloadedPdfs() throws IOException{
		String downloadDirectory = System.getProperty("user.dir") + "\\Downloads";
		File file = new File(downloadDirectory);
		if(file.exists()){
			FileUtils.cleanDirectory(file);
		}
	}

	public String downloadedFile() throws Throwable{
		String filename = "";
		String downloadDirectory = System.getProperty("user.dir") + "\\Downloads";
		File uploadDirectory = new File(downloadDirectory);
		File[] downloadedFiles = uploadDirectory.listFiles();
		Arrays.sort(downloadedFiles, new Comparator<File>() {
			@Override
			public int compare(File fileOne, File fileTwo) {
				return Long.valueOf(fileOne.lastModified()).compareTo(fileTwo.lastModified());
			}
		});
		filename = downloadedFiles[downloadedFiles.length -1].getPath();
		return filename;
	}

	private boolean waitForFileToDownload(){
		boolean flag = false;
		String downloadDirectory = System.getProperty("user.dir") + "\\Downloads";
		File[] downloadedFiles = new File(downloadDirectory).listFiles();
		for(File file : downloadedFiles){
			if(file.getName().endsWith(".pdf")) flag = true;
		}
		return flag;
	}

	private boolean waitForWindowsToBeLoaded(int numberOfWindows){
		return (driver.getWindowHandles().size() == numberOfWindows);
	}

	public void waitForNumberOfWindowsToEqual(final int numberOfWindows) {
		ExpectedCondition<Boolean> windowsToBeLoaded = driver -> (waitForWindowsToBeLoaded(numberOfWindows));
		new WebDriverWait(driver,wait_Time).until(windowsToBeLoaded);
	}

	/**
	 * isEnabled
	 *
	 * @param locator     of (By)
	 * @param locatorName of (String)
	 * @return boolean
	 * @throws Throwable the throwable
	 */
	protected boolean isEnabled(By locator, String locatorName) throws Throwable {
		boolean flag = false;

		try {
			//added loggers
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name :: " + getCallerClassName() + " Method name :: " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
			//value = driver.findElement(locator).isDisplayed();
			flag = driver.findElement(locator).isEnabled();
			//value = true;
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (Exception e) {
			flag = false;
		} finally {
			if (!flag) {
				reporter.failureReport("IsEnabled : ", locatorName + " Element is Not enable", driver);
			} else {
				reporter.SuccessReport("IsEnabled : ", locatorName + " Element is enabled");
			}
		}
		return flag;
	}

	public void waitUntilLoadingDisappear() throws Throwable{
		if(isVisibleOnly(By.id("div[id='processing']"), "Processing icon")){
			ExpectedCondition<Boolean> waitingIcon = driver -> !((Boolean) driver.findElement(By.id("div[id='processing']")).isDisplayed());
			new WebDriverWait(driver, wait_Time).until(waitingIcon);
			LOG.info("loading icon disappeared");
		}
	}

	public void waitUntilLoadingMaskDisappear() throws Throwable {
		LOG.info("Wait for loading mask icon to disappear");
		ExpectedCondition<Boolean> bodyTag = driver -> !((Boolean) driver.findElement(By.xpath("//div[contains(@id,'loadmask') and contains(@style,'display: none')]")).isDisplayed());
		new WebDriverWait(driver, wait_Time).until(bodyTag);
		LOG.info("loading mask icon disappeared");
	}


	public void printPageTitle(By locator, String locatorName) throws Throwable{
		String text = "";
		boolean flag = false;
		LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
		LOG.info("Class name" + getCallerClassName() + "Method name : " + getCallerMethodName());
		try {
			WebDriverWait wait = new WebDriverWait(driver, wait_Time);
			LOG.info("Waiting for element to be present in DOM");
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			text = driver.findElement(locator).getText().trim();
			LOG.info("Locator is Visible and text is retrieved :: " + text);
			flag = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (!flag) {
				reporter.warningReport("GetText :: ", "Unable to get Text from :: " + locatorName);
				LOG.info("GetText :: Unable to get Text from :: " + locatorName);
			} else {
				reporter.SuccessReport("Navigated to Page :", text);
				LOG.info("Locator is Visible and text is retrieved :: " + text);
			}
		}

	}

	/**
	 * assertMatchingTextInList
	 * --> Assert the expectedText from the list of elements which have text in them
	 * @param by          of (By)
	 * by = parent containing multiple elements which have text in them
	 * @param expectedText        of (String)
	 * @param locatorName of (String)
	 * @return boolean
	 * returns true only if any child in the parent has matching text.
	 * @throws Throwable the throwable
	 */
	public boolean assertMatchingTextInList(By by, String expectedText, String locatorName) throws Throwable {
		boolean flag = false;
		try {
			LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			LOG.info("Class name : " + getCallerClassName() + "Method name : " + getCallerMethodName());
			LOG.info("Method : " + getCallerMethodName() + "  ::  Locator : " + locatorName);
			List<WebElement> elems = driver.findElements(by);
			for(WebElement elem:elems){
				String ActualText = elem.getText();
				if(ActualText.trim().contains(expectedText.trim())){
					flag = true;
					LOG.info("ActualText is : " + ActualText);
					LOG.info("String comparison with actual text :: " + "actual text is : " + ActualText + "And expected text is : " + expectedText);
					LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				}else {
					LOG.info("String comparison with actual text :: " + "actual text is : " + ActualText + "And expected text is : " + expectedText);
					LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				}
			}

		} catch (Exception e) {
			return false;
		} finally {
			if (!flag) {
				reporter.failureReport("Verify : " + locatorName, expectedText + " is not present in the list of elements : ", driver);
				return false;
			} else {
				reporter.SuccessReport("Verify : " + locatorName, expectedText + " is  present in the list of elements : " + locatorName);
				return true;
			}
		}
	}

	/*Function to create dynamic xpath*/
	public String xpathUpdator(By locator, String value, String...strings ){
		if(strings.length == 0){
			return locator.toString().split("By.xpath:")[1].trim().replace("$", value);
		}else{
			return locator.toString().split("By.xpath:")[1].trim().replace(strings[0], value);
		}
	}
	/**
	 *
//	 * @param number in string format ex="1234"
	 * @return int
	 * @throws
	 */
	public int convertStringToInt(String $num){
		int intOut = 0;
		try{
			intOut=Integer.parseInt($num.replace(",",""));
		}catch(Exception e){
		}
		return intOut;
	}
	/**
	 * Input DOB or just YEAR to get age.
	 * @param dob in string format ie.12/31/2017 or only year i.e 2001
	 * @return age in years in int
	 */
	public int convertDOBtoAge(String dob){
		int age;
		LocalDate currentDate = LocalDate.now();//currentDate = YYYY-MM-DD
		//-- If only year is passed
		//get the today and month from current DATE, append these to Year passed
		if(!dob.contains("/")){
			String $day=currentDate.toString().split("-")[2];
			String $month= currentDate.toString().split("-")[1];
			dob = $month+"/"+$day+"/"+dob;
		}
		int day;
		Month month=null;

		day=Integer.parseInt(dob.split("/")[1]);
		try{
			month= Month.of(Integer.parseInt(dob.split("/")[0])-1);
		}catch(Exception e){
		}
		int year=Integer.parseInt(dob.split("/")[2]);
		//------------------
		LocalDate birthDate=LocalDate.of(year, month, day);
		if ((birthDate != null) && (currentDate != null)) {
			age = Period.between(birthDate, currentDate).getYears();
		} else {
			age = 0;
		}
		System.out.println(age);
		return age;
	}

	private void bringToFocus(By locator) throws Throwable{
		scrollToWebElement(locator);
	}
}
