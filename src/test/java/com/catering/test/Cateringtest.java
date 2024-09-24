package com.catering.test;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.map.HashedMap;
import org.testng.Assert;
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
public final class Cateringtest extends Apiutils {
	
	private static final String SECRET_ID = "rzp_test_fvYUjpmIHqB5nx";
	   
	private static final String SECRET_KEY = "j7d8lkf1RqAs3ni8Ngt4g8bC";
	
	
	@FrameworkAnnotation(authors = Authors.USER_1, category = {CategoryType.Black_Box_Testing})
	@Test(description = "Validate the login response for POST Request")
	public void loginUser() throws Exception {
		
		String basePath = Endpoints.loginUser;
		
		Properties properties = new Properties();
        FileInputStream inputStream = null;       
		
		try {
		
	        inputStream = new FileInputStream("config.properties");
            properties.load(inputStream);
           
			Map<String, String> loginCredentials = new HashedMap<String, String>();
			loginCredentials.put("phone", "9944194693");
			loginCredentials.put("password", "1111");
			
			Response response = loginRequest(basePath, loginCredentials);
			
			response.then().statusCode(200);
			
			String tokens = response.getHeader("Authorization");
			System.out.println("The token is : " + tokens);
			
            properties.setProperty("authToken", tokens);
            
            FileOutputStream outputStream = new FileOutputStream("config.properties");
			properties.store(outputStream, null);
			outputStream.close();
			
			System.out.println("loginUser Test passed : All assertions are successful.");
			
		} catch (AssertionError e) {
			
			System.out.println("Assertion failed loginUser : " + e.getMessage());
			throw e;			
		
		} catch (Exception e) {
			
			System.out.println("Test failed loginUser : " + e.getMessage());
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

	
	 @FrameworkAnnotation(authors = Authors.USER_1, category = CategoryType.Black_Box_Testing)
	 @Test(description = "Validate the JSON response body for POST Request")
	 public void createOrderRequest() throws Exception {
		 
		 Properties properties = new Properties();
	     FileInputStream inputStream = null;
		
		try {
			
			String basePath = Endpoints.createOrder;
			
			Map<String, Object> user = new HashedMap<String, Object>();
			user.put("id", "3");
			user.put("oid", "156");
			
			inputStream = new FileInputStream("config.properties");
            properties.load(inputStream);
 		
 		    String authToken = properties.getProperty("authToken");
			
			Response response = getRequestWithAuth(basePath, user, authToken);
			response.then().statusCode(200);
			
			//razorpayDetailsId, razorpayOrderId, notes
			
			/*
			 * response.then() .body("applicationFee", equalTo("13000")) .body("theme",
			 * equalTo("#F37254")) .body("secretId", equalTo("rzp_test_fvYUjpmIHqB5nx"))
			 * .body("merchantName", equalTo("Test")) .body("purchaseDescription",
			 * equalTo("TEST PURCHASES")) .body("customerName", equalTo("ANU"))
			 * .body("customerEmail", equalTo(null)) .body("customerContact",
			 * equalTo(9944194693L)) .body("orders.id", equalTo(1)) .body("orders.orderId",
			 * equalTo("ORDER#20240830-0001")) .body("orders.orderAmount", equalTo(130.0f))
			 * .body("orders.gst", equalTo(5.0f)) .body("orders.gstAmount", equalTo(0.0f))
			 * .body("orders.totalAmount", equalTo(130.0f)) .body("orders.address",
			 * equalTo("Dummy")) .body("orders.location.locationId", equalTo(1))
			 * .body("orders.location.locationName", equalTo("OMR"))
			 * .body("orders.location.companyName", equalTo("IBM")) // Excluding 'qrCode',
			 * 'lastUpdatedBy', and 'lastUpdatedDt' fields .body("orders.paymentStatus",
			 * equalTo("PAY_SUCCESS")) .body("orders.userLogin.userId", equalTo(3))
			 * .body("orders.userLogin.userName", equalTo("ANU"))
			 * .body("orders.userLogin.phone", equalTo(9944194693L))
			 * .body("orders.userLogin.emailId", equalTo(null))
			 * .body("orders.userLogin.password", equalTo("phElbKW8zjIyhpg1pWJmLw==")) //
			 * Excluding 'key', 'phoneOtp', 'emailOtp', 'phoneVerified', 'emailVerified',
			 * and 'key' .body("orders.userLogin.role.roleId", equalTo(2))
			 * .body("orders.userLogin.role.roleName", equalTo("USER")) // Excluding nested
			 * 'location' fields like 'qrCode' and 'lastUpdatedDt' .body("orders.shipped",
			 * equalTo(false)) .body("orders.delivered", equalTo(false))
			 * .body("orders.deliveryStatus", equalTo(null));
			 */	
			
			String paymentStatus = response.jsonPath().getString("orders.paymentStatus");
			
			if(paymentStatus.equals("PAY_PENDING")) {
				
				String responseBody = response.getBody().asPrettyString();
				System.out.println("The create prodcut is : " + responseBody);	
			}
			
			System.out.println("createOrderRequest Test passed : All assertions are successful.");
			
		} catch (AssertionError e) {
			
			System.out.println("Assertion failed createOrderRequest : " + e.getMessage());
            throw e;
		
		} catch (Exception e) {
			
			System.out.println("Test failed getAllLocation : " + e.getMessage());
            throw e;
		}
	}
	 
	 @FrameworkAnnotation(authors = Authors.USER_1, category = CategoryType.Black_Box_Testing)
	 @Test(description = "")
	 public void paymentGateway() throws Exception {
		 
		    String basePath = Endpoints.payment;
		 
		 try {
			 
            Map<String, Object> payLoad = new HashedMap<String, Object>();
			 
			 payLoad.put("amount", "100");  
			 payLoad.put("currency", "INR");         // Currency type
			 payLoad.put("email", "abc@gmail.com");  // Customer's email address
			 payLoad.put("name", "Test");
			 payLoad.put("desscription", "Test");
			 payLoad.put("order_id", "order_Oy9dDxnGbeBBB8");
			 payLoad.put("contact", "9944194693");   // Customer's phone number
			 payLoad.put("method", "upi");           // Payment method (UPI)
			 payLoad.put("vpa", "test@axisbank");     // Virtual Payment Address (VPA)
			 			
		Response response = paymentGatewayRequest(basePath, payLoad, SECRET_ID, SECRET_KEY);
			 
		System.out.println(response.getBody().asPrettyString());
		
		response.then().statusCode(200);
		
		/*
		 * String body = response.getBody().asPrettyString();
		 * System.out.println("The response body for payment is : " + body);
		 */
			 
		 } catch (AssertionError e) {
	            
	            System.out.println("Assertion failed paymentGateway : " + e.getMessage());
	            throw e; 
	            
	        } catch (Exception e) {
	           
	            System.out.println("Test failed paymentGateway : " + e.getMessage());
	            throw e; 
	        }
		 
	 }
	 
	 @Test
	 public void getPayments() {
	        // Base URL for Razorpay API
	        String basePath = "https://api.razorpay.com/v1/payments/";

	        // Key ID and Secret Key for Basic Authentication
	        String keyId = "rzp_test_fvYUjpmIHqB5nx";
	        String keySecret = "j7d8lkf1RqAs3ni8Ngt4g8bC";

	        // Perform GET request
	        Response response = RestAssured.given()
	            .auth()
	            .preemptive()
	            .basic(keyId, keySecret)  // Basic authentication
	            .get(basePath)            // GET request to Razorpay endpoint
	            .then()
	            .statusCode(200)          // Expecting HTTP 200 OK
	            .extract()
	            .response();

	        // Print the response body
	        String body = response.getBody().asPrettyString();
	        System.out.println("Response Body: " + body);
	    }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	
	
}
