package reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author in01518
 */
public class BrowserContext {
	private static final Logger LOG = LogManager.getLogger(BrowserContext.class);
	private String browserName = null;
	private String version = null;
	private String platform = null;
	private String driverID = null;
	private static Map<BrowserContext, BrowserContext> mapBrowserContext = new HashMap<BrowserContext, BrowserContext>();
	private static List<BrowserContext> listBrowserContext = new ArrayList<BrowserContext>();

	/**
	 * No Argument Constructor
	 */
	private BrowserContext() {

	}

	/**
	 * Consutructor With Arguments
	 *
	 * @param browserName
	 * @param version
	 * @param platform
	 */
	private BrowserContext(String browserName, String version, String platform, String driverID) {
		this.browserName = browserName;
		this.version = version;
		this.platform = platform;
		this.driverID = driverID;
		LOG.debug("BrowserContext Object Initialized With browserName ( " + this.browserName + " ) , browserVersion  ( " + this.getBrowserVersion() + " , browserPlatform ( " + this.getBrowserPlatform() + " ), driverID (" + driverID + " )");
	}

	/**
	 * @param browserName
	 */
	public void setBrowserName(String browserName) {
		this.browserName = browserName;
		LOG.debug("BrowserContext Object Set With browserName ( " + this.browserName + " ) ");
	}

	/**
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
		LOG.debug("BrowserContext Object Set With browserVersion ( " + this.version + " ) ");
	}

	/**
	 * @param platform
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
		LOG.debug("BrowserContext Object Set With browserVersion ( " + this.version + " ) ");
	}
	
	public void setDriverID(String driverID) {
		this.driverID = driverID;
	}
	
	public String getDriverID() {
		return driverID;
	}

	/**
	 * @return browserName
	 */
	public String getBrowserName() {
		LOG.debug("Being returned : " + this.browserName);
		return this.browserName;
	}

	/**
	 * @return version
	 */
	public String getBrowserVersion() {
		LOG.debug("Being returned : " + this.version);
		return this.version;
	}

	/**
	 * @return platform
	 */
	public String getBrowserPlatform() {
		LOG.debug("Being returned : " + this.platform);
		return this.platform;
	}

	@Override
	public int hashCode() {
		int length = 0;
		
		/*
		 * get length of browserName
		 */
		length = this.getBrowserName() == null ? 0 : this.getBrowserName().length();
		LOG.debug("length of BrowserName = " + length);
		/**
		 * add length of browser version to existing length
		 */
		length = length + (this.getBrowserVersion() == null ? 0 : this.getBrowserName().length());
		LOG.debug("length of BrowserName + version = " + length);

		/**
		 * add length of platform to existing length
		 */
		length = length + (this.getBrowserPlatform() == null ? 0 : this.getBrowserPlatform().length());
		
		/**
		 * add length of driverID to existing length
		 */
		length = length + (this.getDriverID() == null ? 0 : this.getDriverID().length());
		
		
		LOG.debug("length of BrowserName + version + platform = " + length);
		return length;
	}

	@Override
	public boolean equals(Object o) {
		boolean isEquals = false;

		if (o instanceof BrowserContext) {
			/**
			 * get browserName from Object o
			 */
			String objObrowserName = ((BrowserContext) o).getBrowserName();

			/**
			 * get version from Object o
			 */
			String objOBrowserVer = ((BrowserContext) o).getBrowserVersion();

			String objOPlatform = ((BrowserContext) o).getBrowserPlatform();
			String objODriverID = ((BrowserContext) o).getDriverID();
			if (this.browserName.equals(objObrowserName) && this.version.equals(objOBrowserVer) && this.platform.equals(objOPlatform) && this.driverID.equals(objODriverID)) {
				isEquals = true;
			}
		}
		return isEquals;
	}

	@Override
	public String toString() {
		String browserInfo = "<BrowserContext><Name> " + this.browserName + "</Name><Version> " + this.version + "</Version><Platform>" + this.platform + "</Platform><DriverID>"+this.driverID+"</DriverID></BrowserContext>";
		LOG.info(browserInfo);
		return browserInfo;
	}

	/**
	 * synchronized static method which gets unique BrowserContext with respect to given
	 * browserName,version and platform
	 */
	public synchronized static BrowserContext getBrowserContext(String browserName, String version, String platform, String driverID) {
		BrowserContext browserContext = new BrowserContext(browserName, version, platform, driverID);
		if (BrowserContext.mapBrowserContext.get(browserContext) == null) {
			String logInfo = "New BrowserContext Instance Was Created And Placed In Map : BrowserContext.mapBrowserContext";
			LOG.info(logInfo);
			BrowserContext.mapBrowserContext.put(browserContext, browserContext);
			BrowserContext.listBrowserContext.add(browserContext);
		}
		browserContext = BrowserContext.mapBrowserContext.get(browserContext);
		String logInfo = browserContext.toString();
		LOG.info("BrowserContext Info Being returned: " + logInfo);
		return browserContext;
	}

	public static List<BrowserContext> getBrowserContextList() {
		return BrowserContext.listBrowserContext;
	}
}
