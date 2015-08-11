package com.ad.test.shiansi.platform.dataprovider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.Reporter;


public class DataProviderFactory {
	
	private DataProviderFactory() {

	}

	public static DataProvider getInstance(String pageDomain, String pageClassName) throws IOException {
		DataProvider dataProvider = null;
		String guiDataDir = "src/main/resources/GUIData";
		String rawDataFile = guiDataDir + "/" + pageDomain + "/" + pageClassName;
		String yamlFile = rawDataFile + ".yaml";

		if (getFilePath(yamlFile) != null) {
			dataProvider = new GUIMapYamlDataProvider(yamlFile);
		}  else {
		    FileNotFoundException e =  new FileNotFoundException("Data File does not exist for "+rawDataFile+". Supported file extensions: yaml");
			throw e;
		}
		return dataProvider;
	}

	private static String getFilePath(String file) {
		String filePath = null;
		
		try {
			File filelocation = new File(file);
			filePath = filelocation.getPath();
		} catch (NullPointerException e) {
			Reporter.log("Cannot find absolute path of the file" + file);
		}
		return filePath;

	}

}

