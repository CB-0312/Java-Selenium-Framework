package reports;

public interface ReporterConstants {
	String configReporterFile = "src\\test\\resources\\configfiles\\Config.properties";
	String qtestConfigFile = "resources/qTestcaseTestIDMappings.properties";

	/*Test case status as pass*/
	String TEST_CASE_STATUS_PASS = "PASS";
	/*Test case status as fail*/
	String TEST_CASE_STATUS_FAIL = "FAIL";
	/*Test case status as skipped*/
	String TEST_CASE_STATUS_SKIPPED = "SKIPPED";
	/*Test case status as browser failure*/
	String TEST_CASE_STATUS_BROWSER_FAILURE= "BROWSER FAILURE";
	/*Client Name Being Used In Report*/
	String CLIENT_NAME = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "clientName");
	/*Iteration Mode Being Used In Report*/
	String ITERAION_MODE = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "iterationMode");
	/*suite name */
	String SUITE_NAME = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "suiteName");
	/*on error action*/
	String ON_ERROR_ACTION = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "onErrorAction");
	/*Whether Being Run On Single Browser Or Multiple Browser In Multiple Threads*/
	Boolean BOOLEAN_SINGLE_THREAD = Boolean.parseBoolean(ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "singleThreadExecution"));
	/*application under test*/
	String APP_UNDER_TEST = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "AUT");
	/*base url of web application*/
	String APP_BASE_URL = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "baseUrl");
	
	String Timeout = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "Waittime");
	String DYNAMIC_TIMEOUT = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "DynamicWait");
	String MIN_TIMEOUT = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "MinWait");

	/*browserName*/
	String BROWSER_NAME = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "browserName");
	/*version*/
	String BROWSER_VERSION = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "version");
	/*platform*/
	String BROWSER_PLATFORM = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "platform");
	/*ReportFormat*/
	String REPORT_FORMAT = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "reportFormat");
	/*#location of result*/
	String LOCATION_RESULT = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "locationResult");
	/*screenshot folder*/
	String FOLDER_SCREENRSHOTS = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "folderScreenShot");
	/*client logo*/
	String LOCATION_CLIENT_LOGO = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "locationClientLogo");
	/*client logo*/
	String PURE_LOCATION_CLIENT_LOGO = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "ClientLogo");
	/*#company logo*/
	String LOCATION_COMPANY_LOGO = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "locationCompanyLogo");
	/*#failed logo*/
	String LOCATION_FAILED_LOGO = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "locationFailedLogo");
	/*#location minus logo*/
	String LOCATION_MINUS_LOGO = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "locationMinusLogo");
	/*#location passed logo*/
	String LOCATION_PASSED_LOGO = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "locationPassedLogo");
	/*#location plus logo*/
	String LOCATION_PLUS_LOGO = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "locationPlusLogo");
	/*#location warning logo*/
	String LOCATION_WARNING_LOGO = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "locationWarningLogo");
	/*screen shot to be taken if test step is pass */
	Boolean BOOLEAN_ONSUCCESS_SCREENSHOT = Boolean.parseBoolean(ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "onSuccessScreenshot"));
	/*css folder*/
	String LOCATION_JQUERY_CSS_FOLDER = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "locationCssFolder");
	/*js folder*/
	String LOCATION_JQUERY_JS_FOLDER = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "locationJsFolder");
	/*locationImagesFolder*/
	String LOCATION_JQUERY_IMAGES_FOLDER = ConfigFileReadWrite.read(ReporterConstants.configReporterFile, "locationImagesFolder");
}
