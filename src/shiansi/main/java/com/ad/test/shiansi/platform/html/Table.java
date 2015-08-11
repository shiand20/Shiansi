package com.ad.test.shiansi.platform.html;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.ad.test.shiansi.platform.driverutilities.Driver;


public class Table extends AbstractElement {
	
	public Table(String locator) {
		super(locator);
	}
	
	public Table(String locator, String controlName) {
		super(locator, controlName);
	}
	
    public Table(String locator, AbstractElement parent) {
        super(locator, parent);
    }

    public Table(String locator, AbstractElement parent, String controlName) {
        super(locator, parent, controlName);
    }
	
	public int getNumberOfRows(RemoteWebDriver driver) {
        String xPath = getXPathBase(driver) + "tr";
        return AbstractElement.locateElements(xPath, driver).size();
	}

	public int getNumberOfColumns(RemoteWebDriver driver) {
	    List<WebElement> cells;
        String xPath = getXPathBase(driver) + "tr";
        
        List<WebElement> elements = locateElements(xPath, driver);

        if(elements.size() > 0) {
            cells = elements.get(1).findElements(By.xpath("td"));
            return cells.size();
        }
        
        return 0;
	}

	public int getRowIndex(String[] searchKeys, RemoteWebDriver driver) {
	    int numKey = searchKeys.length;
        int rowCount = getNumberOfRows(driver);

        String xPathBase, xPath, value;
        xPathBase = getXPathBase(driver);

        int rowIndex = 0;
        for (int i = 1; i <= rowCount; i++) {
            xPath = xPathBase + "tr[" + i + "]";
            // get table row as text
            value = locateElement(xPath, driver).getText();

            // search the table row for the key words
            if (value.length() > 0) {
                for (int s = 0; s < numKey; s++) {
                    if (searchKeys[s] != null
                            && ((String) searchKeys[s]).length() > 0) {
                        if (value.contains((CharSequence) searchKeys[s])) {
                            rowIndex = i;
                        } else {
                            rowIndex = -1;
                            break;
                        }
                    }
                }
            }
            if (rowIndex > 0)
                break;
        }

        return rowIndex;
	}

	public String getCellValue(int row, int column, RemoteWebDriver driver) {
		List<WebElement> elements = locateElements(getXPathBase(driver) + "tr", driver);
		List<WebElement> cells = elements.get(row).findElements(By.xpath(".//td"));

		if (cells.size() > 0)
			return cells.get(column).getText();

		return null;
	}

    public void clickLinkInCell(int row, int column, RemoteWebDriver driver) {
        String xPath = getXPathBase(driver) + "tr[" + row + "]/td[" + column + "]/a";
        new Link(xPath).click(driver);
    }

	public String getXPathBase(RemoteWebDriver driver) {
		String xPathBase = "";
        if (this.getElement(driver) != null) {

            String locator = getLocator();
            if (!locator.startsWith("link=") && !locator.startsWith("xpath=")
                    && !locator.startsWith("/")) {
                if (locator.startsWith("id=") || locator.startsWith("name=")) {
                    String tmp = locator.substring(locator.indexOf("=", 1) + 1);
                    if (locator.startsWith("id=")) {
                        xPathBase = "//table[@id='" + tmp + "']/tbody/";
                    } else {
                        xPathBase = "//*[@name='" + tmp + "']/tbody/";
                    }

                } else {
                    xPathBase = "//*[@id='" + locator + "']/tbody/";
                }

            } else {
                if (locator.startsWith("xpath=")) {
                    locator = locator.substring(locator.indexOf("=", 1) + 1);
                }

                if (locateElements(locator + "/tbody", driver).size() > 0) {
                    xPathBase = locator + "/tbody/";
                } else {
                    xPathBase = locator + "//";
                }
            }
        } else {
            throw new NoSuchElementException("Table" + this.getLocator()
                    + " does not exist.");
        }
        return xPathBase;
	}

	public String getRowText(int rowIndex, RemoteWebDriver driver) {
        String rowText = null;
        String xPath = getXPathBase(driver) + "tr[" + rowIndex + "]";

        rowText = locateElement(xPath, driver).getText();

        return rowText;
	}

	public void checkCheckboxInCell(int row, int column, RemoteWebDriver driver) {
        String checkboxLocator = getXPathBase(driver) + "tr[" + row + "]/td[" + column + "]/input";
        CheckBox cb = new CheckBox(checkboxLocator);
        cb.check(driver);

        Driver.waitForPageToLoad(getWaitTime(), driver);
	}

    public void uncheckCheckboxInCell(int row, int column, RemoteWebDriver driver) {
        String checkboxLocator = getXPathBase(driver) + "tr[" + row + "]/td[" + column + "]/input";
        CheckBox cb = new CheckBox(checkboxLocator);
        cb.uncheck(driver);

        Driver.waitForPageToLoad(getWaitTime(), driver);
    }
}
