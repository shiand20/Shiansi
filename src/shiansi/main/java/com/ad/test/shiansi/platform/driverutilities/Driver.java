package com.ad.test.shiansi.platform.driverutilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ad.test.shiansi.platform.html.AbstractElement;
import com.thoughtworks.selenium.Wait;

public class Driver {
	
	public static void waitUntilPageTitlePresent(final String pageTitle, final RemoteWebDriver driver) {
        String message = "Wait for title to appear " + pageTitle;
        new Wait() {
            @Override
            public boolean until() {
                return pageTitle.indexOf(driver.getTitle()) >= 0;
            }
        }.wait(message, 30000);

    }
	
	public static void waitUntilElementPresent(final String elementLocator, RemoteWebDriver driver) {
        WebDriverWait myWait = new WebDriverWait(driver, 30);
        myWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(elementLocator)));

    }
	
	public static void waitUntilElementVisible(final String elementLocator, final RemoteWebDriver driver ) {
        String message = "Wait for element to get visible " + elementLocator;
        new Wait() {
            @Override
            public boolean until() {
                WebElement element;
                try {
					element = AbstractElement.locateElement(elementLocator, driver);
                    return ((RemoteWebElement) element).isDisplayed();
                    // return (element.getAttribute("hidden") == "false");
                } catch (Exception e) {
                    return false;
                }
            }
        }.wait(message, 30000);
    }
	
	public static void waitForPageToLoad(String timeout, final RemoteWebDriver driver) {
        String message = "Waiting for page to load";
        new Wait() {
            @Override
            public boolean until() {
                Object returnValue = driver.executeScript("return document['readyState'] == 'complete'");
                return Boolean.parseBoolean(returnValue.toString());
            }
        }.wait(message, Long.parseLong(timeout));
    }

}






