package com.ad.test.shiansi.platform.html;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.ad.test.shiansi.platform.driverutilities.Driver;


public class DatePicker extends AbstractElement {
	
    private String prevMonthLocator;
    private String nextMonthLocator;
    private String dateTextLocator;
    private Calendar calendar;  
	
	public DatePicker(String datePickerLocator) {
		super(datePickerLocator);
		initDateWidgetLocators(datePickerLocator);
	}
	
    public DatePicker(String datePickerLocator, String controlName) {
    	super(datePickerLocator, controlName);
    	initDateWidgetLocators(datePickerLocator);
    }

    public DatePicker(String datePickerLocator, AbstractElement parent) {
        super(datePickerLocator, parent);
        initDateWidgetLocators(datePickerLocator);
    }
	
    public DatePicker(String datePickerLocator, AbstractElement parent, String controlName) {
        super(datePickerLocator, parent, controlName);
        initDateWidgetLocators(datePickerLocator);
    }
    
    private void initDateWidgetLocators(String datePickerLocator) {
        this.prevMonthLocator = datePickerLocator + "/div/div/a[1]/span";
        this.nextMonthLocator = datePickerLocator + "/div/div/a[2]/span";
        this.dateTextLocator = "//div[contains(@class,'ui-datepicker-title')]";
        this.calendar = Calendar.getInstance();
    }
	
    public DatePicker(String datePickerLocator, String prevMonthLocator, String nextMonthLocator, String dateTextLocator) {
        super(datePickerLocator);
		this.prevMonthLocator = prevMonthLocator;
        this.nextMonthLocator = nextMonthLocator;
        this.dateTextLocator = dateTextLocator;
        this.calendar = Calendar.getInstance();

	}
	
	public void clickNextMonth(RemoteWebDriver driver) {
	    AbstractElement.locateElement(nextMonthLocator, driver).click();
	}

	public void clickPrevMonth(RemoteWebDriver driver) {
	    AbstractElement.locateElement(prevMonthLocator, driver).click();
	}
	
    public void clickDay(int dayOfMonth, RemoteWebDriver driver) {
        String dayLocator = "//a[contains(text(),'" + dayOfMonth + "')]";
        AbstractElement.locateElement(dayLocator, driver).click();
    }
	
	public String getDateText(RemoteWebDriver driver) {
	    String value = null;
        value = AbstractElement.locateElement(dateTextLocator, driver).getText();
        return value;
	}
	
    private void navigateMonth(Calendar from, Calendar to, RemoteWebDriver driver) {
        int     monthNav;
        int     yearNav;
        int     monthCount;
    
        monthNav = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
        yearNav = to.get(Calendar.YEAR) - from.get(Calendar.YEAR);
        
        monthCount = (12 * yearNav) + monthNav;
        
        // if the month count is negative, the "to" date
        // is earlier than the "from" date, we have to go
        // back in time to arrive at the "to" date.
        if (monthCount < 0) {
            monthCount = Math.abs(monthCount);
            for (int i = 0; i < monthCount; i++) {
                clickPrevMonth(driver);
                Driver.waitUntilElementVisible(this.prevMonthLocator, driver);
            }
        } else {
            for (int i = 0; i < monthCount; i++) {
                clickNextMonth(driver);
               Driver.waitUntilElementVisible(this.nextMonthLocator, driver);
            }
        }
    }

	public void setDate(Calendar to, RemoteWebDriver driver) {		
	    // Navigate from the current date
        // to the new date
        navigateMonth(calendar, to, driver);
        
        // Select the day-of-month.
        clickDay(to.get(Calendar.DATE), driver);
        
        Calendar cal = calendar;
        cal.set(Calendar.YEAR, to.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, to.get(Calendar.MONTH));
        cal.set(Calendar.DATE, to.get(Calendar.DATE));
	}
	
	public void setDate(String date, RemoteWebDriver driver) {
	    if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Date cannot be null or empty.");
        }
        try {
            Date dateToSet = new SimpleDateFormat("MM/dd/yyyy").parse(date);
            Calendar to = Calendar.getInstance();
            to.setTime(dateToSet);
            setDate(to, driver);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
	}
	
	public void setDate(int year, int month, int day, RemoteWebDriver driver) {
	    Calendar to = Calendar.getInstance();
        to.set(year, month, day);
        setDate(to, driver);
    }
	
	public String getDate() {
	    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String date = formatter.format(calendar.getTime());
        
        return date;
    }
	
	public String getMonth() {
        return String.valueOf(calendar.get(Calendar.MONTH));
	}
	
	public String getYear() {
        return String.valueOf(calendar.get(Calendar.YEAR));
	}
	
	public String getDayOfMonth() {
        return String.valueOf(calendar.get(Calendar.DATE));
	}

    public void datePickerInit(String prevMonthLocator, String nextMonthLocator, String dateTextLocator) {
	    this.prevMonthLocator = prevMonthLocator;
        this.nextMonthLocator = nextMonthLocator;
        this.dateTextLocator = dateTextLocator;	
    }
	
	public void reset(RemoteWebDriver driver) {
	    this.getElement(driver).clear();
	    driver.findElement(By.tagName("html")).click();
        this.calendar = Calendar.getInstance();
        this.getElement(driver).click();	}
}
