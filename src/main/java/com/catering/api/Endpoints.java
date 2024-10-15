package com.catering.api;

public class Endpoints {
	
	public static final String loginUser = "/food/api/v1/user/login";
	
	public static final String loginAdmin = "/#/AdminLogin/";
	
	public static final String getByLocation = "/food/api/v1/food/byLocation";
	
	public static final String getAllLocation = "/food/api/v1/food/allLocation";
	
	public static final String selectOrder = "food/api/v1/order/save";
	
	public static final String createOrder = "/food/api/v1/payment/createOrder";
	
	public static final String checkOrderStatus = "/food/api/v1/payment/checkStatus";
	
	public static final String createPayment = "https://api.razorpay.com/v1/payments";
	
	public static final String saveItem = "/food/api/v1/food/saveItem";

}
