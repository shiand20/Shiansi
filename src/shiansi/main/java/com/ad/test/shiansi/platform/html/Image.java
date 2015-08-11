package com.ad.test.shiansi.platform.html;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

public class Image extends AbstractElement {
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	String currentPage;

	public Image(String locator) {
		super(locator);
	}

    public Image(String locator, String controlName) {
    	super(locator, controlName);
    }

    public Image(String locator, AbstractElement parent) {
        super(locator, parent);
    }
	
    public Image(String locator, AbstractElement parent, String controlName) {
        super(locator, parent, controlName);
    }

	public int getWidth(RemoteWebDriver driver) {
		try {
			return ((RemoteWebElement) getElement(driver)).getSize().width;
		} catch (NumberFormatException e) {
			throw new WebElementException("Attribute " + WIDTH + " not found for Image " + getLocator());
		}
	}
	
	public int getHeight(RemoteWebDriver driver) {
		try {
			return ((RemoteWebElement) getElement(driver)).getSize().height;
		} catch (NumberFormatException e) {
			throw new WebElementException("Attribute " + HEIGHT + " not found for Image " + getLocator());
		}
	}

}
