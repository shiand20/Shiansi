package com.ad.test.shiansi.testcomponents;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.ad.test.shiansi.platform.dataprovider.DataProvider;
import com.ad.test.shiansi.platform.dataprovider.DataProviderFactory;
import com.ad.test.shiansi.platform.driverutilities.Driver;
import com.ad.test.shiansi.platform.html.AbstractElement;
import com.ad.test.shiansi.platform.html.Container;
import com.ad.test.shiansi.platform.html.WebPage;

/**
 * The Class BasePageImpl.
 */
public abstract class BasePageImpl implements WebPage {

	private static String UNKNOWN_PAGE_TITLE = "unknown-title";

	private static String SHIANSI_GUI_COMPONENTS = "com.ad.test.shiansi.platform.html";

	private static String SHIANSI_GUI_BASECLASS = "com.ad.test.shiansi.testcomponents.BasePageImpl";

	private String site;
	
	private boolean pageInitialized;
	
	private Queue<String[]> mapQueue;

	protected Map<String, String> objectMap;

    protected Map<String, Map<String, String>> objectContainerMap = new HashMap<String, Map<String, String>>();
	
	protected String pageTitle;

	protected BasePageImpl() {
		pageTitle = UNKNOWN_PAGE_TITLE;
		mapQueue = new LinkedList<String[]>();
		site = "IN";
		pageInitialized = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.paypal.test.bluefin.platform.html.WebPage#initPage(java.lang.String, java.lang.String)
	 */
	public void initPage(String pageDomain, String pageClassName) {
		// add the page domain and class name to the load queue
		mapQueue.add(new String[] { pageDomain, pageClassName });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.paypal.test.bluefin.platform.html.WebPage#initPage(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void initPage(String pageDomain, String pageClassName, String siteLocale) {
		site = siteLocale;
		initPage(pageDomain, pageClassName);
	}

	/**
	 * Load object map.
	 */
	protected void loadObjectMap() {
		while (mapQueue.size() > 0) {
			String[] map = mapQueue.poll();
			String pageDomain = map[0];
			String pageClassName = map[1];
			Map<String, String> currentObjectMap;
			try {

				DataProvider dataProvider = DataProviderFactory.getInstance(pageDomain, pageClassName);
				currentObjectMap = dataProvider.getDataProviderValues(site);

				pageTitle = currentObjectMap.get("pageTitle");

                for (String key : currentObjectMap.keySet()) {
                    if (key.endsWith("Container")) {
                        objectContainerMap.put(key, dataProvider.getDataProviderValuesForContainer(key, site));
                    }
                }
				
				if (objectMap != null) {
					objectMap.putAll(currentObjectMap);
				} else {
					objectMap = currentObjectMap;
				}
			} catch (Exception e) {
				throw new RuntimeException("Unable to initialize page data for " + pageDomain + "/" + pageClassName
						+ ". Root cause:" + e, e);
			}
		}
		pageInitialized = true;
	}

	/**
	 * Load object map. This method takes a HashMap<String, String> and uses it to populate the objectMap This is
	 * intended to allow for the use of programmatically generated locators in addition to the excel file format IDs and
	 * Locators
	 * 
	 * @param sourceMap
	 *            the source map
	 */
	//TODO: So what happens if the sourceMap object is null or is empty ? Do we still assume that the page has been initialized ?
	//Come back to this logic.
	protected void loadObjectMap(HashMap<String, String> sourceMap) {
		if (sourceMap.containsKey("pageTitle")) {
			pageTitle = sourceMap.get("pageTitle");
		}

		if (sourceMap != null) {
			if (objectMap == null) {
				objectMap = new HashMap<String, String>();
			}
			objectMap.putAll(sourceMap);
		}
		pageInitialized = true;
	}


	public String getExpectedPageTitle() {
		return getPage().pageTitle;
	}

	/**
	 * Validates whether the actual page title specified, equals to any of the titles represented by this page object.
	 * 
	 * @param actualPageTitle
	 *            - the actual page title as {@link String}
	 * @return  true if the actual page title is equal to any of the titles represented by this page otherwise returns false
	 */
	public boolean validatePageTitle(String actualPageTitle) {

		List<String> pageTitles = Arrays.asList(getPage().pageTitle.split("\\|"));
		return pageTitles.contains(actualPageTitle);
	}


	public boolean isInitialized() {
		return pageInitialized;
	}


	public String getSiteLocale() {
		return site;
	}

	

	public void waitForPage(RemoteWebDriver driver) {
		Driver.waitUntilPageTitlePresent(getExpectedPageTitle(), driver);
		validatePage();
		String script = "return window.document.readyState";
		long totalWaitTime = 0;
		long defaultTimeOut = 30000;
		String returnVal = null;
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while (((returnVal = (String) driver.executeScript(script, "")) != null)
				&& (returnVal.trim().equalsIgnoreCase("complete") != true)) {
			try {
				Thread.sleep(5000);
				totalWaitTime += 5000;
				if (totalWaitTime >= defaultTimeOut) {
					throw new RuntimeException("Timed-out waiting for the page to load");
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e.getLocalizedMessage());
			}
		}
		// Falling back to waiting for the page, incase the javascript returns a Null on the page
		if (returnVal == null) {
			Driver.waitUntilPageTitlePresent(getExpectedPageTitle(), driver);
		}
	}

	/**
	 * Perform page validations against the object map.
	 */
	// TODO :: Needs further work.
	private void validatePage() {
		// for (String key : objectMap.keySet() ) {
		// //ignore blank data from object map
		// if ( (key.equalsIgnoreCase("")) ||
		// (objectMap.get(key).equalsIgnoreCase("")) ) {
		// continue;
		// }
		//
		// if (! ( Grid.selenium().isElementPresent(objectMap.get(key)) ||
		// Grid.selenium().isTextPresent(objectMap.get(key)) ) ) {
		// throw new RuntimeException("Landed on a page which did not pass load validation... " +
		// this.getClass().getSimpleName() + " missing element " + objectMap.get(key) );
		// }
		// }
	}

	/**
	 * Require extended class to provide this implementation.
	 * 
	 * @return the page
	 */
	public abstract BasePageImpl getPage();

    /**
     * This method is responsible for automatically initializing the PayPal HTML Objects with their corresponding key
     * values obtained from the hash map.
     * 
     * @param whichClass
     *            Indicate for what object you want the initialization to be done for e.g., the GUI Page class name such
     *            as PayPalLoginPage, PayPalAddBankPage, etc
     * @param objectMap
     *            Pass the {@link Map} that contains the key, value pairs read from the yaml file or excel sheet
     */
	public void initializeHtmlObjects(Object whichClass, Map<String, String> objectMap) {

		ArrayList<Field> fields = new ArrayList<Field>();
		Class<?> incomingClass = whichClass.getClass();

		while (!incomingClass.getName().equals(SHIANSI_GUI_BASECLASS)) {
			fields.addAll(Arrays.asList(incomingClass.getDeclaredFields()));
			incomingClass = incomingClass.getSuperclass();
		}

		String errorDesc = " while initializaing HTML fields from the object map. Root cause:";
		try {
			for (Field field : fields) {
				// proceed further only if the data member and the key in the .xls file match with each other
				// below condition checks for this one to one mapping presence
				if (objectMap.containsKey(field.getName())) {
					field.setAccessible(true);

					String packageName = field.getType().getPackage().getName();

					// We need to perform initialization only for the paypal html objects and
					// We need to skip for any other objects such String, custom Classes etc.
                    if (field.getType().getSuperclass().equals(Container.class)) {

                        Class<?> dataMemberClass = Class.forName(field.getType().getName());
                        Class<?> parameterTypes[] = new Class[3];

                        parameterTypes[0] = dataMemberClass.getDeclaringClass();
                        parameterTypes[1] = String.class;
                        parameterTypes[2] = String.class;
                        Constructor<?> constructor = dataMemberClass.getDeclaredConstructor(parameterTypes);

                        Object[] constructorArgList = new Object[3];
                        String locatorValue = objectMap.get(field.getName());
                        if (locatorValue == null) {
                            continue;
                        }
                        constructorArgList[0] = whichClass;
                        constructorArgList[1] = new String(locatorValue);
                        constructorArgList[2] = new String(field.getName());
                        Object retobj = constructor.newInstance(constructorArgList);
                        field.set(whichClass, retobj);

                        initializeHtmlObjectsInContainer((Container) retobj, objectContainerMap.get(field.getName()));

                    } else if (packageName.equals(SHIANSI_GUI_COMPONENTS)) {

						Class<?> dataMemberClass = Class.forName(field.getType().getName());
						Class<?> parameterTypes[] = new Class[2];

						parameterTypes[0] = String.class;
						parameterTypes[1] = String.class;
						Constructor<?> constructor = dataMemberClass.getConstructor(parameterTypes);

						Object[] constructorArgList = new Object[2];
						String locatorValue = objectMap.get(field.getName());
						if (locatorValue == null) {
							continue;
						}
						constructorArgList[0] = new String(locatorValue);
						constructorArgList[1] = new String(field.getName());
						Object retobj = constructor.newInstance(constructorArgList);
						field.set(whichClass, retobj);
					}
				}
			}
		} catch (ClassNotFoundException exception) {
			throw new RuntimeException("Class not found" + errorDesc + exception, exception);
		} catch (IllegalArgumentException exception) {
			throw new RuntimeException("An illegal argument was encountered" + errorDesc + exception, exception);
		} catch (InstantiationException exception) {
			throw new RuntimeException("Could not instantantiate object" + errorDesc + exception, exception);
		} catch (IllegalAccessException exception) {
			throw new RuntimeException("Could not access data member" + errorDesc + exception, exception);
		} catch (InvocationTargetException exception) {
			throw new RuntimeException("Invocation error occured" + errorDesc + exception, exception);
		} catch (SecurityException exception) {
			throw new RuntimeException("Security error occured" + errorDesc + exception, exception);
		} catch (NoSuchMethodException exception) {
			throw new RuntimeException("Method specified not found" + errorDesc + exception, exception);
		}
	}
	
    /**
     * This method is responsible for automatically initializing the PayPal HTML Objects with their corresponding key
     * values obtained from the hash map. Objects initialized with this method will be instantiated with a parent
     * AbstractElement.
     * 
     * @param whichContainer
     *            Indicate for which Container you want the initialization to be done for e.g., a Container subclass
     * @param objectMap
     *            Pass the {@link Map} that contains the key, value pairs read from the yaml file.
     */
    public void initializeHtmlObjectsInContainer(Container whichContainer, Map<String, String> objectMap) {

        String errorDesc = " while initializaing HTML fields from the object map. Root cause:";
        try {
            for (Field field : whichContainer.getClass().getDeclaredFields()) {
                // proceed further only if the data member and the key in the .xls file match with each other
                // below condition checks for this one to one mapping presence
                if (objectMap.containsKey(field.getName())) {
                    field.setAccessible(true);

                    String packageName = field.getType().getPackage().getName();

                    // We need to perform initialization only for the paypal html objects and
                    // We need to skip for any other objects such String, custom Classes etc.
                    if (field.getType().getSuperclass().equals(Container.class)) {
                        throw new UnsupportedOperationException("Not supported, cannot define a Container within a Container.");
                    } else if (packageName.equals(SHIANSI_GUI_COMPONENTS)) {

                        Class<?> dataMemberClass = Class.forName(field.getType().getName());
                        Class<?> parameterTypes[] = new Class[3];

                        parameterTypes[0] = String.class;
                        parameterTypes[1] = AbstractElement.class;
                        parameterTypes[2] = String.class;
                        Constructor<?> constructor = dataMemberClass.getConstructor(parameterTypes);

                        Object[] constructorArgList = new Object[3];
                        String locatorValue = objectMap.get(field.getName());
                        if (locatorValue == null) {
                            continue;
                        }
                        constructorArgList[0] = new String(locatorValue);
                        constructorArgList[1] = (AbstractElement) whichContainer;
                        constructorArgList[2] = new String(field.getName());
                        Object retobj = constructor.newInstance(constructorArgList);
                        field.set(whichContainer, retobj);
                    }
                }
            }
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("Class not found" + errorDesc + exception, exception);
        } catch (IllegalArgumentException exception) {
            throw new RuntimeException("An illegal argument was encountered" + errorDesc + exception, exception);
        } catch (InstantiationException exception) {
            throw new RuntimeException("Could not instantantiate object" + errorDesc + exception, exception);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException("Could not access data member" + errorDesc + exception, exception);
        } catch (InvocationTargetException exception) {
            throw new RuntimeException("Invocation error occured" + errorDesc + exception, exception);
        } catch (SecurityException exception) {
            throw new RuntimeException("Security error occured" + errorDesc + exception, exception);
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException("Method specified not found" + errorDesc + exception, exception);
        }
    }

	/**
	 * This method takes a HashMap of expected content in which the keys are an identifier for the content (field name,
	 * id etc.) and the values are the expected strings. The Method iterates through the HashMap and verifies that the
	 * full page text contains each string. This method does not validate positioning on the page or association between
	 * elements. It only checks that the expected content strings appear somewhere on the page.
	 * 
	 * @param expectedStrings
	 *            the expected content strings
	 */
    //TODO: Revisit this method. We are referring to BluefinAsserts for doing hard asserts.
    //We should ideally speaking be using the TestNG asserts directly. But then again what happens
    //When the user attempts to leverage this class without TestNG as a Test Runner ?
    //Should we still use Assert ? or should we run a direct comparison and throw custom exceptions.
    //Vet out this part and stream line the approach here
	public static void validateExpectedContentOnPage(HashMap<String, String> expectedStrings, RemoteWebDriver driver) {
		Set<String> keys = expectedStrings.keySet();

		String actualText = driver.findElementByTagName("body").getText();
		for (String key : keys) {
			String expectedText = expectedStrings.get(key);

			boolean textPresent = actualText.contains(expectedText);
			String message = expectedText + " ";
			if (textPresent) {

				message += "found ";
			} else {
				message += "not found ";
			}
			message += "on the page";
			Assert.assertTrue(textPresent, message);
		}
	}

    /**
      * Get current page's name which is the unique identifier of each page in PayPal website.
      * @return current page's name
      */
	//TODO: Revisit this part. Is it a safe type cast or should we check if Grid.driver() is assignable to
	//JavascriptExecutor before we attempt to do this.
     public String getPageName(RemoteWebDriver driver) {
         JavascriptExecutor jse = (JavascriptExecutor) driver;
         String script = "return s.pageName;";
         try {
             return (String) jse.executeScript( script );
         } catch ( Exception e ) {
             return null;
         }
     }
}
