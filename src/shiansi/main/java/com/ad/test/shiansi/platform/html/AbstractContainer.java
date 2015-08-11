package com.ad.test.shiansi.platform.html;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

public abstract class AbstractContainer extends AbstractElement {

    private int index = 0;

    public AbstractContainer(String locator) {
        super(locator);
    }

    public AbstractContainer(String locator, String controlName) {
        super(locator, controlName);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public RemoteWebElement getElement(RemoteWebDriver driver) {
        List<WebElement> elements = locateElements(getLocator(), driver);
        if (index <= elements.size()) {
            return (RemoteWebElement) elements.get(index);
        } else {
            throw new NoSuchElementException("Cannot find Container at index: " + index);
        }

    }

    public int size(RemoteWebDriver driver) {
        int size = 0;

        try {
            size = AbstractElement.locateElements(getLocator(), driver).size();
        } catch (NoSuchElementException e) {
            // do nothing, let size be returned as 0
        }

        return size;
    }
    
    public WebElement locateElement(int index, String childLocator, RemoteWebDriver driver) {
        setIndex(index);
        return AbstractElement.locateElement(childLocator, this, driver);
    }

}