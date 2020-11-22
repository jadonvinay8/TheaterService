package com.capgemini.TheaterService.beans;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@DynamoDBDocument
public class Address {

	@NotNull
	@NotBlank
	private	String city;

	@NotNull
	@NotBlank
	private	String state;
	private	String area;

	@NotNull
	@NotBlank
	private	String pincode;
	
	public Address() {
		// Default Constructor
	}

	public Address(String city, String state, String area, String pinCode) {
		super();
		this.city = city;
		this.state = state;
		this.area = area;
		this.pincode = pinCode;
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	@Override
	public String toString() {
		return "Address{" +
		"city='" + city + '\'' +
		", state='" + state + '\'' +
		", area='" + area + '\'' +
		", pincode='" + pincode + '\'' +
		'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Address address = (Address) o;
		return Objects.equals(city, address.city) &&
				Objects.equals(state, address.state) &&
				Objects.equals(area, address.area) &&
				Objects.equals(pincode, address.pincode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, state, area, pincode);
	}

}
