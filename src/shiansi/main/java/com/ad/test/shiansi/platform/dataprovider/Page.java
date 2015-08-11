package com.ad.test.shiansi.platform.dataprovider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page {
    private Map<String, String> pageTitle = null;
    private String baseClass = null;
    private Map<String, HtmlElement> elements = null;
    private String defaultLocale = "US";
    private List<String> pageValidators = null;
    
    public void setDefaultLocale(String defaultLocale){
        this.defaultLocale = defaultLocale;
    }
    
    public String getDefaultLocale(){
        return this.defaultLocale;
    }
    
    public void setPageValidators(List<String> pageValidators){
        this.pageValidators = new ArrayList<String>();
        this.pageValidators.addAll(pageValidators);
    }
    
    public List<String> getPageValidators(){
        return Collections.unmodifiableList(this.pageValidators);
    }
    
    
    public void setElements(Map<String, HtmlElement> elements){
        this.elements = new HashMap<String, HtmlElement>();
        this.elements.putAll(elements);
    }
    
    public Map<String, HtmlElement> getElements(){
        return Collections.unmodifiableMap(this.elements);
    }
    
    public void setPageTitle(Map<String, String> pageTitle){
        this.pageTitle = new HashMap<String, String>();
        this.pageTitle.putAll(pageTitle);
    }
    
    public void setBaseClass(String baseClass){
        this.baseClass = baseClass;
    }
    
    public String getBaseClass(){
        return this.baseClass;
    }
    
    public Map<String,String> getAllPageTitles(){
        return Collections.unmodifiableMap(this.pageTitle);
    }
    

    String getPageTitle() {
        return getPageTitle(this.defaultLocale);
    }

    String getPageTitle(String locale) {
        return this.pageTitle.get(locale);
    }
	
    @Override
	public String toString() {
		return "pageTitle: " + this.pageTitle.get(this.defaultLocale) + "\n" +
				"baseClass: " + this.baseClass + "\n" +
				"elements: " + this.elements.size() + "\n" +
				"pageValidators: " + this.pageValidators.size() + "\n" +
				"defaultLocale: " + this.defaultLocale; 
	}
}