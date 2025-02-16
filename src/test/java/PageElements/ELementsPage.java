package PageElements;

import org.openqa.selenium.By;

public class ELementsPage {
	
	static By elementsTab;

	static{
		elementsTab = By.xpath("//*[text()='Elements']/../..");
	}
}