package PageElements;

import base.ActionEngineWeb;
import org.openqa.selenium.WebDriver;
import reports.CReporter;

public class ELementsPageLib extends ActionEngineWeb {

	public ELementsPageLib(WebDriver webDriver, CReporter reporter) {
		this.driver = webDriver;
		this.reporter = reporter;
	}

	public void clickElementsTab() throws Throwable {
		click(ELementsPage.elementsTab, "Elements Tab");
	}
}