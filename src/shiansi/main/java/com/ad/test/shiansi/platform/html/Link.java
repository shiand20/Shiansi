package com.ad.test.shiansi.platform.html;


public class Link extends AbstractElement {

	public Link(String locator) {
		super(locator);
	}

	public Link(String locator, String controlName) {
    	super(locator, controlName);
    }

    public Link(String locator, AbstractElement parent) {
        super(locator, parent);
    }

    public Link(String locator, AbstractElement parent, String controlName) {
        super(locator, parent, controlName);
    }
}
