package com.ad.test.shiansi.platform.dataprovider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HtmlElement {
    private Map<String, String> locators = new HashMap<String, String>();
    private Map<String, HtmlContainerElement> containerElements = new HashMap<String, HtmlContainerElement>();

    public Map<String, String> getLocators() {
        return Collections.unmodifiableMap(locators);
    }

    public void setLocators(Map<String, String> locators) {
        this.locators = new HashMap<String, String>(locators);
    }
    
    public Map<String, HtmlContainerElement> getContainerElements() {
        return Collections.unmodifiableMap(containerElements);
    }

    public void setContainerElements(Map<String, HtmlContainerElement> containerElements) {
        this.containerElements = new HashMap<String, HtmlContainerElement>(containerElements);
    }
}
