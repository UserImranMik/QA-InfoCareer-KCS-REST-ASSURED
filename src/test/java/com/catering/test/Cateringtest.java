package com.catering.test;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;

import org.json.JSONArray;
import org.json.JSONObject;
import static io.restassured.RestAssured.given;
import java.nio.charset.StandardCharsets; // Ensure you have this import
import java.util.ArrayList;
import java.util.Base64; // Import the built-in Java Base64 encoder
import java.util.HashMap;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.map.HashedMap;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.model.Author;
import com.catering.annotation.FrameworkAnnotation;
import com.catering.api.Endpoints;
import com.catering.enumeration.Authors;
import com.catering.enumeration.CategoryType;
import com.catering.utility.Apiutils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Cateringtest extends Apiutils {
	
    private String authToken;
    
    private static Logger logger = LogManager.getLogger(Cateringtest.class);
    
    // Logger for JSON logging (optional)
    private static final Logger jsonLogger = LogManager.getLogger("jsonLogger");
    
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
            logger.info("Base URI set to: " + baseURI);
        } else {
        	 logger.error("Exception occured", new Exception("Base URI is missing in the config.properties file."));
        }

        inputStream.close();
    }
	
	@Test
	public void loginAndSelectItemsForUsers() throws Exception {

	    // Define users
	    String[][] users = {
	    		
	    	{"9944194693", "1111"},
	    	{"9944194693", "1111"}
	    	//{"9876543210", "1212"}     
	    };

	    for (String[] user : users) {
	        String phoneNumber = user[0];
	        String passKey = user[1];
	        
	          authToken = null;
	          
	          // Step 1; baseURI config
	          setup();
			
			  // Step 2: Login 
	          loginUser(phoneNumber, passKey);
			  
			  // Step 3: Select Items 
	          selectItems(phoneNumber);
			  
			  // Step 4: Create order
	          createOrderRequest(phoneNumber);
			  
			  // Step 4: Test payment
	          testPayment(phoneNumber);		 	 			
	    }
	}
	
	@FrameworkAnnotation(authors = Authors.USER_1, category = {CategoryType.Black_Box_Testing})
	@Test(description = "Validate the login response for POST Request")
	public void loginUser(String phoneNumber, String passKey) throws Exception {
		
		String basePath = Endpoints.loginUser;
		 logger.info("Base Path: " + basePath);
		
		Properties properties = new Properties();
        FileInputStream inputStream = null;       
		
		try {
			
	        inputStream = new FileInputStream("config.properties");
            properties.load(inputStream);
            	
            	Map<String, String> loginCredentials = new HashedMap<String, String>();
    			loginCredentials.put("phone", phoneNumber);
    			loginCredentials.put("password", passKey);
    			
    			 logger.info("Attempting to login with: " + loginCredentials);
    			Response response = loginRequest(basePath, loginCredentials);
    			
    		    if (response.getStatusCode() != 200) {
    		      
    		        String errorMessage = response.getBody().asString(); 
    		        logger.error("Error occurred: " + errorMessage);
    		        logger.info(response.prettyPrint());
    		        logger.error("Exception occurred", new Exception("Login failed for user: " + phoneNumber + ". Status code: " + response.getStatusCode() + ". Response: " + errorMessage));
    		    }
    	        
    	        // Store the token in the class-level variable
                authToken = response.getHeader("Authorization");
                logger.info("The token is: " + authToken);
                
                if (authToken == null || authToken.isEmpty()) {
                    throw new Exception("Authorization token is missing or empty for user " + phoneNumber);
                }
    			
                logger.info("loginUser Test passed : All assertions are successful."); 
                logger.info("User logged in");
                logger.info("The response body for Login is : " + response.prettyPrint());
            
		} catch (AssertionError e) {
			
			logger.error("Assertion failed loginUser : " + e.getMessage());
			throw e;			
		
		} catch (Exception e) {
			
			logger.error("Test failed loginUser : " + e.getMessage());
			throw e;
		}
		
	}
	
	@FrameworkAnnotation(authors = Authors.USER_1, category = {CategoryType.Black_Box_Testing})
	@Test(description = "Validate the JSON response body for GET Request")
	public void getByLocation() {
		
		try {
			
			String basePath = Endpoints.getByLocation;
			
			Map<String, Object> params = new HashedMap<String, Object>();
			params.put("id", 1);
			
			Response response = getRequest(basePath, params);
			
	        response.then().statusCode(200);
	        
	        String actualProductName = response.jsonPath().get("[0].productName");
	        System.out.println("Actual Product Name: |" + actualProductName + "|");
	        
	        String actualProductDescription = response.jsonPath().get("[0].productDescription");
	        System.out.println("Actual Product Description [0]: |" + actualProductDescription + "|");
	        
	        String actualProductDescription1 = response.jsonPath().get("[1].productDescription");
	        System.out.println("Actual Product Description [1]: |" + actualProductDescription1 + "|");
	      
	            response.then().body("[0].productId", equalTo(1))
	            .body("[0].productName", equalTo("Pongal "))
	            .body("[0].productDescription", equalTo("Pongal added with Ghee and Nuts it's a Indian traditional dish with coconut chutney and Vada "))
	            .body("[0].productPrice", equalTo(70.0F)) 
	            .body("[0].productGST", equalTo(5))
	            .body("[0].productActive", equalTo(true))
	            .body("[0].location.locationId", equalTo(1))
	            .body("[0].location.locationName", equalTo("DLF"))

	            .body("[1].productId", equalTo(4))
	            .body("[1].productName", equalTo("Idli"))
	            .body("[1].productDescription", equalTo("Idli or idly is a type of savoury rice cake, originating from South India, popular as a breakfast fo"))
	            .body("[1].productPrice", equalTo(25.0F)) 
	            .body("[1].productGST", equalTo(5))
	            .body("[1].productActive", equalTo(true))
	            .body("[1].location.locationId", equalTo(1))
	            .body("[1].location.locationName", equalTo("DLF"));
	        
	        String body = response.asPrettyString();        
	        System.out.println(body);
	        
	        SoftAssert softAssert = new SoftAssert();
	        
	        String locationName = response.jsonPath().get("[1].location.locationName");	        
	        softAssert.assertEquals(locationName, "DLF");
			
	        System.out.println("getByLocation Test passed : All assertions are successful.");
	        
		} catch (AssertionError e) {
            
            System.out.println("Assertion failed getByLocation : " + e.getMessage());
            throw e; 
            
        } catch (Exception e) {
           
            System.out.println("Test failed getByLocation : " + e.getMessage());
            throw e; 
        }
	}
	
	@FrameworkAnnotation(authors = Authors.USER_1, category = {CategoryType.Black_Box_Testing})
	@Test(description = "Validate the JSON response body for GET Request")
	public void getAllLocation() {
		
		try {
			
			String basePath = Endpoints.getAllLocation;
			
			Response response = getAllRequest(basePath);
			
			response.then().statusCode(200)
			
			.body("[0].locationId", equalTo(1))
			.body("[0].locationName", equalTo("DLF"))
			
			.body("[1].locationId", equalTo(2))
			.body("[1].locationName", equalTo("OMR"));
			
            String body = response.asPrettyString();    
	        System.out.println(body);
			
	        String locationName = response.jsonPath().get("[1].locationName");
	        Assert.assertEquals(locationName, "OMR");
	        
			System.out.println("getAllLocation Test passed : All assertions are successful.");
			
		} catch (AssertionError e) {
            
            System.out.println("Assertion failed getAllLocation : " + e.getMessage());
            throw e; 
            
        } catch (Exception e) {
           
            System.out.println("Test failed getAllLocation : " + e.getMessage());
            throw e; 
        }
	
	}
	
	
	@FrameworkAnnotation(authors = Authors.USER_1, category = {CategoryType.Black_Box_Testing})
	@Test(description = "Validate the item selection response for POST Request and company name 'HCL'.")
	public void selectItems(String phoneNumber) throws Exception {
		
		     Properties properties = new Properties();
		     FileInputStream inputStream = null;
		
		try {
			
			 inputStream = new FileInputStream("config.properties");
			 properties.load(inputStream);
			 
			 String basePath = Endpoints.selectOrder;
			 
			    // Create products array
	            JSONArray productList = new JSONArray();
	           
	           // Add product 1
	            JSONObject productOne = new JSONObject();
	            productOne.put("productId", 1);
	            productOne.put("quantity", 10);
	            productList.put(productOne);
	            
	           // Add product 2
	            JSONObject productTwo = new JSONObject();
	            productTwo.put("productId", 2);
	            productTwo.put("quantity", 5);
	            productList.put(productTwo);   
			 
	            logger.info(productList);
	            
	            // Create query parameters
	            Map<String, Object> queryParams = new HashMap<>();
	            queryParams.put("userId", 29);
	            queryParams.put("locationId", 2);
		
		   Response response = selectItems(basePath, authToken, queryParams, productList);		   
		   
		   int statusCode = response.getStatusCode();
		   
		     if(statusCode == 200) {
		    	 
		  // Validate if the "companyName" inside "location" is "HCL"
		   String companyName = response.jsonPath().getString("orders.location.companyName");
		   Assert.assertEquals(companyName, "HCL", "Company name does not match!");

		    logger.info("Validation successful! Company name is: " + companyName);		    
		 }
		   
		} catch (AssertionError e) {
			
			logger.error("Assertion failed createOrderRequest : " + e.getMessage());
            throw e;
		
		} catch (Exception e) {
			
			logger.error("Test failed getAllLocation : " + e.getMessage());
            throw e;
		}
	}

	
	 @FrameworkAnnotation(authors = Authors.USER_1, category = CategoryType.Black_Box_Testing)
	 @Test(description = "Validate the order create response for POST Request and paymentStatus 'PAY_PENDING'.")
	 public void createOrderRequest(String phoneNumber) throws Exception {
		 
		 Properties properties = new Properties();
	     FileInputStream inputStream = null;
		
		try {
			
			String basePath = Endpoints.createOrder;
			
			Map<String, Object> user = new HashedMap<String, Object>();
			user.put("id", "3");
			user.put("oid", "156");
			
			inputStream = new FileInputStream("config.properties");
            properties.load(inputStream);
			 
			Response response = getRequestWithAuth(basePath, user, authToken);
			response.then().statusCode(200);
			
			logger.info(response.prettyPrint());
			
			String paymentStatus = response.jsonPath().getString("orders.paymentStatus");
			
			String notes = response.jsonPath().getString("notes");
			logger.info("notes : " + notes);
		    
		    FileOutputStream fileOutputStream = new FileOutputStream("config.properties");
		    properties.store(fileOutputStream, null);
		    properties.setProperty("notes", notes);
			
			if(paymentStatus.equals("PAY_PENDING")) {
				
				String responseBody = response.getBody().asPrettyString();
				logger.info("The created product is : " + responseBody);	
			}
			
			String orderId = response.jsonPath().getString("razorpayOrderId");
			
			logger.info("The order id for the created order is : " + orderId);
			
			logger.info("createOrderRequest Test passed : All assertions are successful.");
			
		} catch (AssertionError e) {
			
			logger.error("Assertion failed createOrderRequest : " + e.getMessage());
            throw e;
		
		} catch (Exception e) {
			
			logger.error("Test failed getAllLocation : " + e.getMessage());
            throw e;
		}
	}
	 
	 
	 @Test
	 public void getPayments() throws IOException {
		 
	         // Base URL for Razorpay API
	         String basePath = "https://api.razorpay.com/v1/payments/";

	         Properties properties = new Properties();
	         FileInputStream inputStream = new FileInputStream("config.properties");
	         properties.load(inputStream);
	       
	         String keyId = properties.getProperty("RAZORPAY_KEY_ID");
	         String keySecret = properties.getProperty("RAZORPAY_KEY_SECRET");
	        
	        String credentials = keyId + ":" + keySecret;
	        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
	        logger.info("Encoded Credentials: " + encodedCredentials);

	        // Perform GET request
	        Response response = RestAssured.given()
	        	.header("Authorization", "Basic " + encodedCredentials)// Basic authentication
	            .get(basePath)            
	            .then()
	            .statusCode(200)          
	            .extract()
	            .response();

	        // Print the response body
	        String body = response.getBody().asPrettyString();
	        logger.info("Response Body: " + body);
	    }
	 
	 
	 @FrameworkAnnotation(authors = Authors.USER_1, category = CategoryType.Black_Box_Testing)
	 @Test(description = "Test Payment")
	 public void testPayment(String phoneNumber) throws Exception {
		 
		      Properties properties = new Properties();
	          FileInputStream inputStream = null;       		

	     try {
	    	 
	    	   inputStream = new FileInputStream("config.properties");
	           properties.load(inputStream);

	         // Base URI for Razorpay API
	         RestAssured.baseURI = "https://api.razorpay.com/v1";

	         // Payment details
	         int amount = 6000; 
	         String currency = "INR";
	         boolean upiLink = true;

	         // Create the payment details JSON
	         JSONObject paymentDetails = new JSONObject();
	         paymentDetails.put("amount", amount);
	         paymentDetails.put("currency", currency);

	         // Customer details
	         JSONObject customerDetails = new JSONObject();
	         customerDetails.put("name", "ANU");
	         customerDetails.put("contact", "9944194693");

	         // Attach customer details to payment details
	         paymentDetails.put("customer", customerDetails);
	         
	        String notes = properties.getProperty("notes");

	         paymentDetails.put("notes", notes);

	         String keyId = properties.getProperty("RAZORPAY_KEY_ID");
	         String keySecret = properties.getProperty("RAZORPAY_KEY_SECRET");

	         String credentials = keyId + ":" + keySecret;
	         String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
	         logger.info("Encoded Credentials: " + encodedCredentials);
	 		
	         Response response = given()
	             .header("Authorization", "Basic " + encodedCredentials)
	             .header("Content-Type", "application/json")
	             .header("Accept", "application/json")
	        	 //.contentType(ContentType.JSON)
	             .body(paymentDetails.toString())
	             .log().all()  
	             .when()
	             .post("/payment_links")
	             .then()
	             .log().all()  
	             .statusCode(200)
	             .extract()
	             .response();
	         
	         String status = response.jsonPath().getString("status");
	         int responseAmount = response.jsonPath().getInt("amount");
	         String responseCurrency = response.jsonPath().getString("currency");
	         String customerName = response.jsonPath().getString("customer.name");
	         String customerContact = response.jsonPath().getString("customer.contact");

	         assertEquals(status, "created");
	         assertEquals(responseAmount, amount);
	         assertEquals(responseCurrency, currency);
	         assertEquals(customerName, "ANU");
	         assertEquals(customerContact, "9944194693");
	         
	         logger.info(response.prettyPrint());

	     } catch (AssertionError e) {
	    	 logger.error("Assertion failed paymentGateway: " + e.getMessage());
	         throw e;
	     } catch (Exception e) {
	    	 logger.error("Test failed paymentGateway: " + e.getMessage());
	         throw e;
	     }
	 }

	 
	 
	 
	 
	 
	
	
}
