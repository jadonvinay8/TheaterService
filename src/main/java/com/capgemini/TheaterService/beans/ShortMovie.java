package com.capgemini.TheaterService.beans;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class ShortMovie {

	private String id;
	private String name;
	
	public ShortMovie() {
		// Default Constructor
	}
	
	public ShortMovie(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public ShortMovie(String name) {
		super();
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
