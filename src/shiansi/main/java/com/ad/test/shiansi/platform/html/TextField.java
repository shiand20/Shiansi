package com.ad.test.shiansi.platform.html;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;


public class TextField extends AbstractElement {

	public TextField(String locator) {
		super(locator);
	}
	
    public TextField (String locator, String controlName) {
    	super(locator, controlName);
    }

    public TextField(String locator, AbstractElement parent) {
        super(locator, parent);
    }
	
	public TextField(String locator, AbstractElement parent, String controlName) {
        super(locator, parent, controlName);
    }
	
	public void type(String value, RemoteWebDriver driver) {
		RemoteWebElement element = getElement(driver);
		element.clear();
		element.sendKeys(value);
	}
	
	public void type(String value, boolean isKeepExistingText, RemoteWebDriver driver) {
	    if(isKeepExistingText == true) {
            getElement(driver).sendKeys(value);
        }
        else {
            type(value, driver);
        }
	}
	
	public void clear(RemoteWebDriver driver) {
	    getElement(driver).clear();
	}
	
	public boolean isEditable(RemoteWebDriver driver) {
	    return ((RemoteWebElement) getElement(driver)).isEnabled();
	}
	
	public String getText(RemoteWebDriver driver) {
	    String text = getElement(driver).getText();
        if(text.isEmpty()) {
            text = getValue(driver);
        }
        return text;
	}
}
