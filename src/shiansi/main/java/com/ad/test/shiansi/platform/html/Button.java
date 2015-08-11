package com.ad.test.shiansi.platform.html;

/**
 * This class is the web element Button wrapper for handling infrastructure testing technology.
 * <p>
 * In this class, the method 'click' is encapsulated and invoke bluefin session to do the click against the specified
 * element.
 * </p>
 * 
 */
public class Button extends AbstractElement {

	public Button(String locator) {
		super(locator);
	}

	public Button(String locator, String controlName) {
    	super(locator, controlName);
    }

    public Button(String locator, AbstractElement parent) {
        super(locator, parent);
    }

    public Button(String locator, AbstractElement parent, String controlName) {
        super(locator, parent, controlName);
    }

	@Override
    protected String fetchClassName(){
		return Thread.currentThread().getStackTrace()[6].getClassName();
    }

}