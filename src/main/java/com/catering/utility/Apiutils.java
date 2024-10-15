package com.catering.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.catering.properties.Configproperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Apiutils {
	

  protected Response getRequest(String basePath, Map<String, Object> params) {

        return RestAssured.given()
                .queryParams(params)
                .basePath(basePath)
                .when()
                .get()
                .then()
                .extract()
                .response();
    }
  
  protected Response getAllRequest(String basePath) {
	    return RestAssured.given()
	            .basePath(basePath)
	            .when()
	            .get()
	            .then()
	            .extract()
	            .response();
	}
  
  
  protected Response loginRequest(String basePath, Map<String, String> loginCredentials) {
	  
	  return RestAssured.given()
			  .contentType(ContentType.JSON)
			  .body(loginCredentials)
			  .when()
			  .post(basePath)
			  .then()		  
			  .extract()
			  .response();
  }
  
  protected Response getRequestWithAuth(String basePath, Map<String, Object> params, String token) {

      return RestAssured.given()
    		  .header("Authorization", token)
              .queryParams(params)
              .basePath(basePath)
              .when()
              .post()
              .then()
              .extract()
              .response();
  }
  

  protected Response paymentGatewayRequest(String basePath, Map<String, Object> params, String keyId, String keySecret) {
	  
	  JSONObject jsonParams = new JSONObject(params);
	  
	    return RestAssured.given()
	            .auth()
	            .preemptive()
	            .basic(keyId, keySecret)  // Use basic auth directly
	            .contentType(ContentType.HTML) // Explicitly set the correct charset
	            .body(jsonParams.toString())  // Send form parameters
	            .log().all()  // Log request details
	            .when()
	            .post(basePath)
	            .then()
	            .log().all()  // Log response details
	            .extract()
	            .response();
	        
	}
 
  
  protected Response fetchPaymentDetails(String basePath, String keyId, String keySecret) {
     
	  return RestAssured.given()
          .auth()
          .preemptive()
          .basic(keyId, keySecret)
          .when()
          .get(basePath)
          .then()
          .extract()
          .response();
  }
  
  protected Response selectItems(String basePath, String token, 
          Map<String, Object> queryParams, 
          JSONArray productList) throws JsonProcessingException {
	  
	  return RestAssured.given()
			  .header("Authorization", token)              
              .contentType(ContentType.JSON)   
              .queryParams(queryParams)                     
              .body(productList.toString())                            
              .log().all()
              .when()
			  .post(basePath)
			  .then()
			  .log()
			  .all()
			  .extract()
			  .response();  
      }
  
  protected Response addProduct(String basePath, String token, 
	        File productImage, Map<String, Object> formParams) {
	    
	    return RestAssured.given()
	            .header("Authorization", token)               
	            .multiPart("productImg", productImage)         
	            .formParams(formParams)                       
	            .log().all()                                  
	            .when()
	            .post(basePath)                                
	            .then()
	            .log().all()                                   
	            .extract()
	            .response();                                  
	} 
  
  protected Response addMultipleProduct(String basePath, String token, 
	        String productImage, Map<String, Object> formParams) {
	    
	    return RestAssured.given()
	            .header("Authorization", token)               
	            .multiPart("productImg", productImage)         
	            .formParams(formParams)                       
	            .log().all()                                  
	            .when()
	            .post(basePath)                                
	            .then()
	            .log().all()                                   
	            .extract()
	            .response();                                  
	} 
  

}
