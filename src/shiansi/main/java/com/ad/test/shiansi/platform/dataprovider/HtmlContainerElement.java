package com.ad.test.shiansi.platform.dataprovider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HtmlContainerElement {
    private Map<String, String> locators = new HashMap<String, String>();

    public Map<String, String> getLocators() {
        return Collections.unmodifiableMap(locators);
    }

    public void setLocators(Map<String, String> locators) {
        this.locators = new HashMap<String, String>();
        this.locators.putAll(locators);
    }
}
