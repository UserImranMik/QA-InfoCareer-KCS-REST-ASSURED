package com.catering.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ooxml.POIXMLException;
//import org.apache.poi.ss.usermodel.*;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.testng.annotations.BeforeClass;

import org.testng.annotations.Test;

import com.catering.api.Endpoints;
import com.catering.utility.Apiutils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ProductAdd extends Apiutils {

    private String authToken;
    
    private Workbook workbook = null;
    
    private static Logger logger = LogManager.getLogger(ProductAdd.class);      
    
    @BeforeClass
    public void setup() throws Exception {
      
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream("config.properties");
        try {
			properties.load(inputStream);
		} catch (IOException e) {
			
			e.printStackTrace();
		}

        String baseURI = properties.getProperty("baseURI");
        if (baseURI != null && !baseURI.isEmpty()) {
            RestAssured.baseURI = baseURI;
            logger.info("Base URI for Admin is set to: " + baseURI);
        } else {
        	 logger.error("Exception occured", new Exception("Base URI is missing in the config.properties file."));
        }

        inputStream.close();
    }
    
      @Test
      public void adminLogin() throws Exception {
    	  
    	     String basePath = Endpoints.loginUser;
    	     logger.info("Base Path: " + basePath);
    	     
    	     Properties properties = new Properties();
    	     FileInputStream inputStream = null;
    	  
    	  try {
    		  
    		  inputStream = new FileInputStream("config.properties");
    		  properties.load(inputStream);

    		  Map<String, String> adminCredentials = new HashedMap<String, String>();
    		  adminCredentials.put("phone", "8888888888");
    		  adminCredentials.put("password", "1234");
    		  logger.info("Attempting to login with: " + adminCredentials);
    		  
    		Response response = loginRequest(basePath, adminCredentials);
    		
    		authToken = response.getHeader("Authorization");
    		logger.info("The admin token is : " + authToken);
    		  
    		if (authToken == null || authToken.isEmpty()) {
                throw new Exception("Authorization token is missing or empty for user " + adminCredentials);
            }
    		
    		logger.info("adminLogin Test passed : All assertions are successful."); 
            logger.info("Admin logged in");
            logger.info("The response body for Login is : " + response.prettyPrint());
    		  
    	  } catch (AssertionError e) {
    		  
    		  logger.error("Assertion failed adminLogin : " + e.getMessage());
  			  throw e;			
    	 
    	  } catch (Exception e) {
  			
  			logger.error("Test failed adminLogin : " + e.getMessage());
  			throw e;
  		}
    	  
    	  finally {
      	    
  	        if (inputStream != null) {
  	            inputStream.close();
  	        }
  	    }
    	  	  
      }
      
      
      @Test
      public void addProduct() {
    	  
    	  String basePath = Endpoints.saveItem;
    	  
    	  try {  		  
    		  
    		  Map<String, Object> addingProducts = new HashMap<>();
    		  	  
    		addingProducts.put("productName", "Idlies");
  			addingProducts.put("productDescription", "Idly is a South Indian food");
  			addingProducts.put("productPrice", 20);
  			addingProducts.put("productGST", 1);
  			addingProducts.put("productActive", true);
  			addingProducts.put("location.locationId", 1);
  			addingProducts.put("productUpdatedBy", 2);
  			
  			File file = new File("C:\\Users\\DELL\\OneDrive\\Pictures\\Idly.jpg");
  			
  			Response response = addProduct(basePath, authToken, file, addingProducts);
  					
  			if(response.getStatusCode() == 200) {
  				
  				logger.info("Product added successfully with response : " + response.asPrettyString());
  			}
    		  
    	  } catch (AssertionError e) {
      		  
      		  logger.error("Assertion failed addProducts : " + e.getMessage());
      	  
      	  } catch (Exception e) {
      			
      			logger.error("Test failed addProducts : " + e.getMessage());
      			throw e;
      		}  	  
         }
   
  	    @Test
        public void addMultipleProduct() throws IOException {
      	  
      	  String basePath = Endpoints.saveItem;
      	  
      	 File filePath = new File("D:\\Kannan-Catering\\Kannan-Catering\\src\\main\\resources\\foodproducts.xlsx");
      	 logger.info("File path: " + filePath.getAbsolutePath());
      	
      	if (!filePath.exists() || !filePath.canRead()) {
      	    logger.error("File does not exist or cannot be read.");
      	    throw new IOException("File does not exist or cannot be read.");
      	}

      	FileInputStream inputStream = null;
      	XSSFWorkbook workbook = null;
      	  
      	  try {
      		  
      		  inputStream = new FileInputStream(filePath);
      		      
      		    // Attempt to create the workbook from the InputStream.
				workbook = new XSSFWorkbook(inputStream);
				logger.info("Workbook created successfully.");
      		    
             Sheet sheet = workbook.getSheet("Product");
      		  
          	for(int i=1; i< sheet.getLastRowNum(); i++) {
          		  
          	Map<String, Object> addProduct = new HashMap<>();
          		  
          		String productName = sheet.getRow(i).getCell(0).toString();
          		logger.info("The product name is : " + productName);
          		String productDescription = sheet.getRow(i).getCell(1).toString();
          		logger.info("The product description is : " + productDescription);
          		String productPrice = sheet.getRow(i).getCell(2).toString();
          		logger.info("The product price is : " + productPrice);
          		String productActive = sheet.getRow(i).getCell(3).toString();
          		logger.info("The product active is : " + productActive);
          		String location = sheet.getRow(i).getCell(4).toString();
          		logger.info("The product location is : " + location);
          		String productUpdatedBy = sheet.getRow(i).getCell(5).toString();
          		logger.info("The product update by is : " + productUpdatedBy);
          		String imageFile = sheet.getRow(i).getCell(6).toString();
          		logger.info("The image file is : " + imageFile);
          		
          	  addProduct.put("productName", productName);
          	  addProduct.put("productDescription", productDescription);
          	  addProduct.put("productPrice", Integer.parseInt(productPrice.replace(".0", "")));
          	  addProduct.put("productActive", productActive.toLowerCase());
          	  addProduct.put("location.locationId", Integer.parseInt(location.replace(".0", "")));
          	  addProduct.put("productUpdatedBy", Integer.parseInt(productUpdatedBy.replace(".0", "")));
          	  addProduct.put("productImg", imageFile);
          		  
          	  logger.info(addProduct);
          	  
         	  Response response = addMultipleProduct(basePath, authToken, imageFile, addProduct);
          	  
         	  if(response.statusCode() == 200) { 
          		  
         		  logger.info("Product added successfully with response : " + response.asPrettyString());  
         	  } 	  
         }
      		   		  
      	  } catch (FileNotFoundException e) {
      	        logger.error("Excel file not found: " + e.getMessage());
      	    } catch (IOException e) {
      	        logger.error("I/O error occurred while processing the Excel file: " + e.getMessage());
      	    } catch (Exception e) {
      	        logger.error("Test failed addMultipleProduct : " + e.getMessage());
      	        throw e;
      	    } 
      
        }
    
	
}
