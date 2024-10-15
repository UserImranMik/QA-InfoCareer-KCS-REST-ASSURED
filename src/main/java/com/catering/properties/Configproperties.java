package com.catering.properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configproperties {
	
	private static Properties properties = new Properties();
    private static final String CONFIG_FILE_PATH = "D:/Kannan-Catering/Kannan-Catering/config.properties"; // Absolute path

    static {
       
    	try (FileInputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
        
    		properties.load(input);
        
    	} catch (IOException e) {
        
    		e.printStackTrace();
            
    		throw new RuntimeException("Failed to load config properties from " + CONFIG_FILE_PATH);
        }
    }

    public static String getProperty(String key) {

    	return properties.getProperty(key);
    }

    public static void setProperty(String key, String value) {

    	properties.setProperty(key, value);
    }

    public static void saveProperties() {

    	try (FileOutputStream outputStream = new FileOutputStream(CONFIG_FILE_PATH)) {
        
    		properties.store(outputStream, null);
        
    	} catch (IOException e) {
            e.printStackTrace();
        }
    }

}
