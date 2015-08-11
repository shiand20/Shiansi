package com.ad.test.shiansi.platform.html;

import org.openqa.selenium.remote.RemoteWebDriver;


public class Form extends AbstractElement {

	public Form(String locator) {
		super(locator);
	}
	
    public Form(String locator, String controlName) {
    	super(locator, controlName);
    }

    public Form(String locator, AbstractElement parent) {
        super(locator, parent);
    }
	
    public Form(String locator, AbstractElement parent, String controlName) {
        super(locator, parent, controlName);
    }
	
	public void submit(RemoteWebDriver driver) {
		getElement(driver).submit();
	}
}
