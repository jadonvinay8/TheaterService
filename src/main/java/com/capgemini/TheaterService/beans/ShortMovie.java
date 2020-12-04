package com.capgemini.TheaterService.beans;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ShortMovie that = (ShortMovie) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public String toString() {
		return "ShortMovie{" +
		"id='" + id + '\'' +
		", name='" + name + '\'' +
		'}';
	}
}
