package com.ad.test.shiansi.platform.html;

import org.openqa.selenium.remote.RemoteWebDriver;


public class Label extends AbstractElement {

	public Label(String locator) {
		super(locator);
	}
	
    public Label(String locator, String controlName) {
    	super(locator, controlName);
    }

    public Label(String locator, AbstractElement parent) {
        super(locator, parent);
    }
	
    public Label(String locator, AbstractElement parent, String controlName) {
        super(locator, parent, controlName);
    }
	
	public boolean isTextPresent(String pattern, RemoteWebDriver driver) {
		String text = getElement(driver).getText();
		boolean textpresent =(text != null && (text.contains(pattern) || text
				.matches(pattern)));
		return textpresent;
	}

}
