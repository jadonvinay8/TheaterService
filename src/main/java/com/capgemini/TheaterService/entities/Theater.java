package com.capgemini.TheaterService.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.capgemini.TheaterService.beans.Address;
import com.capgemini.TheaterService.beans.ShortMovie;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@DynamoDBTable(tableName = "Theater")
public class Theater {

	private	String theaterId;
	private String cityId;

	@NotNull
	@NotBlank
	private	String theaterName;

	@NotNull
	private Address address;

	@NotNull
	@Min(1)
	@Max(5)
	private Double rating;
	private	List<ShortMovie> movies;
	
	public Theater() {
		// Default Constructor
	}

	public Theater(String theatreName, Address address, String cityId, Double rating, List<ShortMovie> movies) {
		super();
		this.theaterName = theatreName;
		this.address = address;
		this.cityId = cityId;
		this.rating = rating;
		this.movies = movies;
	}

	@DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
	public String getTheaterId() {
		return theaterId;
	}

	public void setTheaterId(String theaterId) {
		this.theaterId = theaterId;
	}

	@DynamoDBAttribute
	public String getTheaterName() {
		return theaterName;
	}

	public void setTheaterName(String theaterName) {
		this.theaterName = theaterName;
	}

	@DynamoDBAttribute
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@DynamoDBAttribute
	public List<ShortMovie> getMovies() {
		return movies;
	}

	public void setMovies(List<ShortMovie> movies) {
		this.movies = movies;
	}

	@DynamoDBAttribute
	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	@DynamoDBAttribute
	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}
}
