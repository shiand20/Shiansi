package com.ad.test.shiansi.platform.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.record.formula.functions.T;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ad.test.shiansi.platform.driverutilities.Driver;
import com.ad.test.shiansi.platform.html.support.ByOrOperator;


public abstract class AbstractElement {
    private String locator;
    private String controlName;
    private AbstractElement parent;
    private Map<String, String> propMap = new HashMap<String, String>();
    protected static final String LOG_DEMARKER = "&#8594;";


    public static RemoteWebElement locateElement(String locator, RemoteWebDriver driver) {
        validateLocator(locator);
        By locatorBy = resolveByType(locator);

        RemoteWebElement element = (RemoteWebElement) driver.findElement(locatorBy);

        return element;
    }
    
    public static RemoteWebElement locateElement(String locator, AbstractElement parent, RemoteWebDriver driver) {
        validateLocator(locator);
        By locatorBy = resolveByType(locator);
        RemoteWebElement element = null;
        
        if (parent != null && parent instanceof AbstractContainer) {
            validateChildLocator(locator);
            AbstractContainer container = (AbstractContainer) parent;
            element = (RemoteWebElement) container.getElement(driver).findElement(locatorBy);
        } else {
            throw new UnsupportedOperationException(generateUnsupportedParentMsg(parent));
        }
        
        return element;
    }

    public static List<WebElement> locateElements(String locator, RemoteWebDriver driver) {
        validateLocator(locator);
        By locatorBy = resolveByType(locator);
    
        List<WebElement> webElementsFound = driver.findElements(locatorBy);
        if (webElementsFound.isEmpty()) {
            NoSuchElementException e = new NoSuchElementException(generateUnsupportedLocatorMsg(locator));
            throw e;
        }
        return webElementsFound;
    }

    public static List<WebElement> locateElements(String locator, AbstractElement parent, RemoteWebDriver driver) {
        validateLocator(locator);
        By locatorBy = resolveByType(locator);
        List<WebElement> webElementsFound = null; 
        
        if (parent != null && parent instanceof AbstractContainer) {
            validateChildLocator(locator);
            AbstractContainer container = (AbstractContainer) parent;
            webElementsFound = container.getElement(driver).findElements(locatorBy);
        } else {
            throw new UnsupportedOperationException(generateUnsupportedParentMsg(parent));
        }

        // if element is empty list then throw exception since unlike
        // findElement() findElements() always returns a list
        // irrespective of whether an element was found or not
        if (webElementsFound.isEmpty()) {
            NoSuchElementException e = new NoSuchElementException(generateUnsupportedLocatorMsg(locator));
            throw e;
        }
        return webElementsFound;
    }
    
    private static String generateUnsupportedParentMsg(AbstractElement parent) {
        return "Cannot search for an element from "
                + parent.getClass().getSimpleName() + " parent object type. Create a Container subclass instead.";
    }
    
    private static String generateUnsupportedLocatorMsg(String locator) {
        return "Could not find any elements with the locator " + locator
                + ". Locator has to be either a name, id, link text, xpath, or css selector.";
    }

    protected static void validateLocator(String locator) {
        if (locator == null) {
            throw new NoSuchElementException("locator cannot be null");
        } else if (locator.trim().isEmpty()) {
            throw new NoSuchElementException("locator cannot be empty");
        }
    }
    
    private static void validateChildLocator(String locator) {
        if (locator.startsWith("xpath=/") || locator.startsWith("/")) {
            throw new UnsupportedOperationException(
                    "Use xpath dot notation to search for Container descendant elements. Example: \".//myLocator\". ");
        }
    }
    
    private static By resolveByType(String locator) {
        By locatorBy = null;
        locator = locator.trim();

        if (locator.indexOf("|") == -1) {
            locatorBy = getFindElementType(locator);
        } else {
            String[] locators = locator.split("\\Q|\\E");
            List<By> result = new ArrayList<By>();
            for (String temp : locators) {
                result.add(getFindElementType(temp));
            }
            locatorBy = new ByOrOperator(result);
        }

        return locatorBy;
    }

    public static By getFindElementType(String locator) {
        By valueToReturn = null;
        locator = locator.trim();
        if (locator.startsWith("id=")) {
            valueToReturn = By.id(locator.substring("id=".length()));
        } else if (locator.startsWith("name=")) {
            valueToReturn = By.name(locator.substring("name=".length()));
        } else if (locator.startsWith("link=")) {
            valueToReturn = By.linkText(locator.substring("link=".length()));
        } else if (locator.startsWith("xpath=")) {
            valueToReturn = By.xpath(locator.substring("xpath=".length()));
        } else if (locator.startsWith("/") || locator.startsWith("./")) {
            valueToReturn = By.xpath(locator);
        } else if (locator.startsWith("css=")) {
            valueToReturn = By.cssSelector(locator.substring("css=".length()));
        } else {
            valueToReturn = new ByIdOrName(locator);
        }
        
        return valueToReturn;
    }

    public RemoteWebElement getElement(RemoteWebDriver driver) {
        if (parent == null) {
            return locateElement(getLocator(), driver);
        }
        return locateElement(getLocator(), parent, driver);
    }  
    
    public List<WebElement> getElements(RemoteWebDriver driver) {
        if (parent == null) {
            return locateElements(getLocator(), driver);
        }
        return locateElements(getLocator(), parent, driver);
    }

    public AbstractElement(String locator) {
        this(locator, null, null);
    }
    
    public AbstractElement(String locator, AbstractElement parent) {
        this(locator, parent, null);
    }

    public AbstractElement(String locator, String controlName) {
        this(locator, null, controlName);
    }
    
    public AbstractElement(String locator, AbstractElement parent, String controlName) {
        this.locator = locator;
        this.parent = parent;
        this.controlName = controlName;
    }

    public String getLocator() {
        return locator;
    }

    public String getControlName() {
        return controlName;
    }
    
    public AbstractElement getParent() {
        return parent;
    }

    public String getText(RemoteWebDriver driver) {
        return getElement(driver).getText();
    }

    public boolean isElementPresent(RemoteWebDriver driver) {
        boolean returnValue = false;
        try {
            if (getElement(driver) != null) {
                returnValue = true;
            } else {
                // the control is not expected to come here. getElement() throws
                // an exception if the element doesnt exist
            }
        } catch (NoSuchElementException e) {
            returnValue = false;
        }
        
        return returnValue;
    }

    public boolean isVisible(RemoteWebDriver driver) {
        return ((RemoteWebElement) getElement(driver)).isDisplayed();
    }

    public boolean isEnabled(RemoteWebDriver driver) {
        return getElement(driver).isEnabled();
    }

    public String getAttribute(String attributeName, RemoteWebDriver driver) {
        return getElement(driver).getAttribute(attributeName);
    }

    public String getValue(RemoteWebDriver driver) {
        return getAttribute("value", driver);
    }

    public String getProperty(String key) {
        return propMap.get(key);
    }

    public void setProperty(String key, String value) {
        propMap.put(key, value);
    }

    protected String getWaitTime() {
        return "30000";
    }

    protected String fetchClassName() {
        return Thread.currentThread().getStackTrace()[4].getClassName();
    }

    protected String resolvePageNameToUseForLogs() {
        String pageName = fetchClassName();
        int firstChar = pageName.lastIndexOf('.') + 1;
        pageName = pageName.substring(firstChar);
        if (!pageName.toLowerCase().contains("page")) {
            pageName = "";
        }
        if (!pageName.isEmpty()) {
            pageName = " in " + pageName;
        }
        return pageName;
    }

    protected String resolveControlNameToUseForLogs() {
        String resolvedName = getControlName();
        if (resolvedName == null) {
            return getLocator();
        }
        return resolvedName;
    }


    private void processAlerts(String browser, RemoteWebDriver driver) {
        try {
        	driver.switchTo().alert();
            return;
        } catch (NoAlertPresentException exception) {
        }

    }

    protected void validatePresenceOfAlert(RemoteWebDriver driver) {
        try {
        	driver.switchTo().alert();
            String errorMsg = "Encountered an alert. Cannot wait for an element when an operation triggers an alert.";
            throw new InvalidElementStateException(errorMsg);
        } catch (NoAlertPresentException exception) {
        }
    }

    public void click(RemoteWebDriver driver) {
        clickonly(driver);
    }

    public void clickonly(RemoteWebDriver driver) {
        click(driver, new Object[] {} );
    }

    public void click(RemoteWebDriver driver, Object... expected) {
        getElement(driver).click();
        
        if (expected == null || expected.length == 0) {
            return;
        }
        validatePresenceOfAlert(driver);
        try{
            for (Object expect : expected) {
                if (expect instanceof AbstractElement) {
                    AbstractElement a = (AbstractElement) expect;
                    Driver.waitUntilElementPresent(a.getLocator(), driver);
                } else if (expect instanceof String) {
                    String s = (String) expect;
                    Driver.waitUntilElementPresent(s, driver);
                } else if (expect instanceof WebPage) {
                    WebPage w = (WebPage) expect;
                    w.waitForPage();
                }
            }
        }finally{
            //Attempt at taking screenshots even when there are time-outs triggered from the wait* methods.
        }
    }

    @Deprecated
    public T click(ExpectedCondition<T> expectedCondition, RemoteWebDriver driver) {
        return (T) clickAndExpect(expectedCondition, driver);
    }

    public Object clickAndExpect(ExpectedCondition<?> expectedCondition, RemoteWebDriver driver){
        getElement(driver).click();
        validatePresenceOfAlert(driver);
        long timeout = 30000;
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        Object variable = wait.until(expectedCondition);
        
        return variable;
    }

}
