package com.ad.test.shiansi.generic.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileHelper {
	
	public static InputStream loadFile(String fileName) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream iStream = loader.getResourceAsStream(fileName);
		if (iStream == null) {
			try {
				iStream = new FileInputStream(fileName);
			} catch (FileNotFoundException e) {
			}
		}
		if (iStream == null) {
			throw new RuntimeException("\"" + fileName + "\" is not a valid resource");
		}
		return iStream;
	}
	
	public static InputStream loadFile(File file) {
		return loadFile(file.getAbsolutePath());
	}

}
