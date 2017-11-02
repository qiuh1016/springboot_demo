package com.cetcme.springBootDemo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

	public static Properties getResources(String fileName) {
		Properties prop = new Properties();

		File directory = new File("");
		try {
			FileInputStream fin;
			String filePath = directory.getCanonicalPath() + Constants.RESOURCES_PATH + fileName;
			fin = new FileInputStream(filePath);
			prop.load(fin);
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return prop;
	}
}
