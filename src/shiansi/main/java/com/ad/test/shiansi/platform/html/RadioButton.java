package com.ad.test.shiansi.platform.html;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import com.ad.test.shiansi.platform.driverutilities.Driver;


public class RadioButton extends AbstractElement {

	public RadioButton(String locator) {
		super(locator);
	}
	
    public RadioButton(String locator, String controlName) {
        super(locator, controlName);
    }
	
    public RadioButton(String locator, AbstractElement parent) {
        super(locator, parent);
    }
	
    public RadioButton(String locator, AbstractElement parent, String controlName) {
        super(locator, parent, controlName);
    }
	
	public void check(RemoteWebDriver driver) {
		if (!isChecked(driver)) {
			this.click(driver);
		}
	}

	public void click(RemoteWebDriver driver) {
		getElement(driver).click();
	}

	public void click(String locator, RemoteWebDriver driver) {
		getElement(driver).click();
		validatePresenceOfAlert(driver);
		Driver.waitUntilElementPresent(locator, driver);
	}

	public boolean isChecked(RemoteWebDriver driver) {
		return  ((RemoteWebElement) getElement(driver)).isSelected();
	}
}
