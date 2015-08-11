package com.ad.test.shiansi.platform.html;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import com.ad.test.shiansi.platform.driverutilities.Driver;


/**
 * This class is the web element CheckBox wrapper for handling infrastructure testing technology.
 * <p>
 * In this class, the method 'check' and 'uncheck' are encapsulated and invoke blufin session to do the check/uncheck
 * against the specified element. The method 'isChecked' is to verify whether this element is checked.
 * </p>
 * 
 */
public class CheckBox extends AbstractElement {
	String currentPage = "currentPage";

	public CheckBox(String locator) {
		super(locator);
	}
	
	
    public CheckBox(String locator, String controlName) {
    	super(locator, controlName);
    }


    public CheckBox(String locator, AbstractElement parent) {
        super(locator, parent);
    }
    
    public CheckBox(String locator, AbstractElement parent, String controlName) {
        super(locator, parent, controlName);
    }
	
	public void check(RemoteWebDriver driver) {
		RemoteWebElement e = (RemoteWebElement) getElement(driver);
		while (!e.isSelected())
			e.click();
	}
	
	public void check(String locator, RemoteWebDriver driver) {
		this.check(driver);
		validatePresenceOfAlert(driver);
		Driver.waitUntilElementPresent(locator, driver);
	}

	public void uncheck(RemoteWebDriver driver) {
		RemoteWebElement e = (RemoteWebElement) getElement(driver);
		while (e.isSelected()){
			e.click();
		}
	}
	
	public void uncheck(String locator, RemoteWebDriver driver) {
		this.uncheck(driver);
		validatePresenceOfAlert(driver);
		Driver.waitUntilElementPresent(locator, driver);
	}
	
	public void click(RemoteWebDriver driver) {
		getElement(driver).click();
	}
	
	public void click(String locator, RemoteWebDriver driver) {
		click(driver);
		validatePresenceOfAlert(driver);
		Driver.waitUntilElementPresent(locator, driver);
	}
	
	public boolean isChecked(RemoteWebDriver driver) {
		return getElement(driver).isSelected();
	}

	public boolean isEnabled(RemoteWebDriver driver) {
		return getElement(driver).isEnabled();
	}
}
