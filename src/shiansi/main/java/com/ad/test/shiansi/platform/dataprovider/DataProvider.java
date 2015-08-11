package com.ad.test.shiansi.platform.dataprovider;

import java.util.Map;

public interface DataProvider {
	
	public final static String KEY = "Key";
    public final static String CONTAINER = "Container";
    public final static String ELEMENTS = "Elements";
    public final static String ELEMENTSv2 = "containerElements";
    public final static String DEFAULT_LOCALE = "IN";
    
	public Map<String, String> getDataProviderValues(String locale);
	
	public Map<String, String> getDataProviderValuesForContainer(String containerKey, String locale);

}
