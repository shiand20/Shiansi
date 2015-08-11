package com.ad.test.shiansi.platform.html;

public interface WebPage {

	public void waitForPage();
	
	public void initPage(String pagePath, String pageClassName);
	
	public void initPage(String pagePath, String pageClassName, String siteLocale);
	
	public boolean isInitialized();
	
	public String getExpectedPageTitle();
	
	public String getSiteLocale();
	
	public abstract WebPage getPage();
		
}
