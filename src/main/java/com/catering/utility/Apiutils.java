package com.catering.utility;

import java.util.Map;

import com.catering.properties.Configproperties;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Apiutils {
	
    protected void setup() {
    	
        String URI = Configproperties.getProperty("URI");
        RestAssured.baseURI = URI;
    }
	
	 static {
	        
		 RestAssured.baseURI = Configproperties.getProperty("URI");	    
	 }
	

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
			  .basePath(basePath)
			  .contentType("application/json")
			  .body(loginCredentials)
			  .when()
			  .post()
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
	  
	    return RestAssured.given()
	        .auth()
	        .preemptive().basic(keyId, keySecret) 
	        .queryParams(params)  
	        .body(basePath)  
	        .when()
	        .post()  
	        .then()
	        .extract()
	        .response();
	}

 
  

  
  
  

}
