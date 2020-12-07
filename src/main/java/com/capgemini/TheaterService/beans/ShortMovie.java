package com.capgemini.TheaterService.beans;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.util.Date;
import java.util.Objects;

@DynamoDBDocument
public class ShortMovie {

	private String id;
	private Date startDate;
	private Date endDate;
	
	public ShortMovie() {
		// Default Constructor
	}
	
	public ShortMovie(String id, Date startDate, Date endDate) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public ShortMovie(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ShortMovie that = (ShortMovie) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, startDate, endDate);
	}

	@Override
	public String toString() {
		return "ShortMovie{" +
		"id='" + id + '\'' +
		", startDate=" + startDate +
		", endDate=" + endDate +
		'}';
	}

}
