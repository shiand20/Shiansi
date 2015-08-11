package com.ad.test.shiansi.platform.html;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;


public class SelectList extends AbstractElement {

    public SelectList(String locator) {
        super(locator);
    }
    
    public SelectList(String locator, String controlName) {
        super(locator, controlName);
    }

    public SelectList(String locator, AbstractElement parent) {
        super(locator, parent);
    }

    public SelectList(String locator, AbstractElement parent, String controlName) {
        super(locator, parent, controlName);
    }

    public void select(String optionLocator, RemoteWebDriver driver) {
        if (optionLocator == null || optionLocator.trim().isEmpty()){
            throw new IllegalArgumentException("Locator cannot be null or empty.");
        }
        if (optionLocator.split("=").length != 2){
            StringBuffer errMsg = new StringBuffer("Invalid locator specified :"); 
            errMsg.append(optionLocator);
            errMsg.append(". Locator should be of the form label=<value> (or) ");
            errMsg.append("value=<value> (or) ");
            errMsg.append("index=<value> (or) ");
            errMsg.append("id=<value>.");
            throw new IllegalArgumentException(errMsg.toString());
        }
        String locatorToUse = optionLocator.split("=")[1].trim();
        String tLocator = optionLocator.toLowerCase().split("=")[0].trim();
        if (tLocator.indexOf("label") >= 0) {
            // label was given
            new Select(getElement(driver)).selectByVisibleText(locatorToUse);
        } else if (tLocator.indexOf("value") >= 0) {
            // value was given
        } else if (tLocator.indexOf("index") >= 0) {
            // index was given
            new Select(getElement(driver)).selectByIndex(Integer.parseInt(locatorToUse));
        } else if (tLocator.indexOf("id") >= 0) {
            // id was given
            getElement(driver).findElementById(locatorToUse).click();
        } else {
            throw new NoSuchElementException("Unable to find " + optionLocator);
        }
    }

    public void select(String[] optionLocators, RemoteWebDriver driver) {
        for (int i = 0; i < optionLocators.length; i++) {
            select(optionLocators[i], driver);
        }
    }

    public void addSelection(String optionLocator, RemoteWebDriver driver) {
        select(optionLocator, driver);
    }

    public void selectByValue(String value, RemoteWebDriver driver) {
        new Select(getElement(driver)).selectByValue(value);
    }

    public void selectByLabel(String label, RemoteWebDriver driver) {
        new Select(getElement(driver)).selectByVisibleText(label);
    }

    public void selectByIndex(int index, RemoteWebDriver driver) {
        new Select(getElement(driver)).selectByIndex(index);
    }

    public void selectByValue(String[] values, RemoteWebDriver driver) {
        for (int i = 0; i < values.length; i++) {
            selectByValue(values[i], driver);
        }
    }

    public void selectByLabel(String[] labels, RemoteWebDriver driver) {
        for (int i = 0; i < labels.length; i++) {
            selectByLabel(labels[i], driver);
        }
    }

    public void selectByIndex(String[] indexes, RemoteWebDriver driver) {
        for (int i = 0; i < indexes.length; i++) {
            selectByIndex(Integer.parseInt(indexes[i]), driver);
        }
    }

    public void addSelectionByValue(String value, RemoteWebDriver driver) {
        selectByValue(value, driver);
    }

    public void addSelectionByLabel(String label, RemoteWebDriver driver) {
        selectByLabel(label, driver);
    }

    public void addSelectionByIndex(String index, RemoteWebDriver driver) {
        selectByIndex(Integer.parseInt(index), driver);
    }

    public String[] getSelectOptions(RemoteWebDriver driver) {
        List<WebElement> optionList = getElement(driver).findElements(By.tagName("option"));
        String[] optionArray = new String[optionList.size()];
        for (int i=0; i<optionList.size(); i++) {
            optionArray[i] = optionList.get(i).getText();
        }
        return optionArray;
    }

    public String getSelectedLabel(RemoteWebDriver driver) {
        List<WebElement> options = getElement(driver).findElements(By.tagName("option"));
        for (WebElement option : options) {
            if (option.isSelected()) {
                return option.getText();
            }
        }
        return null;
    }

    public String getSelectedValue(RemoteWebDriver driver) {
        List<WebElement> options = getElement(driver).findElements(By.tagName("option"));
        for (WebElement option : options) {
            if (option.isSelected()) {
                return option.getAttribute("value");
            }
        }
        return null;
    }

    public String[] getSelectedLabels(RemoteWebDriver driver) {
        List<WebElement> options = getElement(driver).findElements(By.tagName("option"));
        List<String> selected = new ArrayList<String>();
        for (WebElement option : options) {
            if (option.isSelected())
                selected.add(option.getText());
        }
        return (String[]) selected.toArray(new String[selected.size()]);
    }

    public String[] getSelectedValues(RemoteWebDriver driver) {
        List<WebElement> options = getElement(driver).findElements(By.tagName("option"));
        List<String> selected = new ArrayList<String>();
        for(WebElement option : options) {
            if (option.isSelected()) {
                selected.add(option.getAttribute("value"));
            }
        }
        return (String[]) selected.toArray(new String[selected.size()]);
    }

    public String[] getContentLabel(RemoteWebDriver driver) {
        List<WebElement> options = getElement(driver).findElements(By.tagName("option"));
        List<String> contents = new ArrayList<String>();
        
        for(WebElement option : options) {
            contents.add(option.getText());
        }
        
        return (String[]) contents.toArray(new String[contents.size()]);
    }

    public String[] getContentValue(RemoteWebDriver driver) {
        List<WebElement> options = getElement(driver).findElements(By.tagName("option"));
        List<String> contents = new ArrayList<String>();
        
        for(WebElement option : options) {
            contents.add(option.getAttribute("value"));
        }
        
        return (String[]) contents.toArray(new String[contents.size()]);
    }

    public void deselectAll(RemoteWebDriver driver) {
        new Select(getElement(driver)).deselectAll();
    }

    public void deselectByValue(String value, RemoteWebDriver driver) {
        new Select(getElement(driver)).deselectByValue(value);
    }

    public void deselectByIndex(int index, RemoteWebDriver driver) {
        new Select(getElement(driver)).deselectByIndex(index);
    }

    public void deselectByLabel(String label, RemoteWebDriver driver) {
        new Select(getElement(driver)).deselectByVisibleText(label);
    }
}
