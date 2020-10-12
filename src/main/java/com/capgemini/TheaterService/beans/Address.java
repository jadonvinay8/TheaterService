package com.capgemini.TheaterService.beans;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@DynamoDBDocument
public class Address {

	@NotNull
	@NotBlank
	private	String city;

	@NotNull
	@NotBlank
	private	String state;
	private	String addressLine1;

	@NotNull
	@NotBlank
	private	String pincode;
	
	public Address() {
		// Default Constructor
	}

	public Address(String city, String state, String addressLine1, String pincode) {
		super();
		this.city = city;
		this.state = state;
		this.addressLine1 = addressLine1;
		this.pincode = pincode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	
	
	
}
