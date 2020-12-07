package com.capgemini.TheaterService.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class ShowsInfo {

    @Min(1)
    @Max(5)
    @NotNull
    private int shows;

    @FutureOrPresent
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @FutureOrPresent
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    public ShowsInfo() {
    }

    public ShowsInfo(int shows, @FutureOrPresent Date startDate, @FutureOrPresent Date endDate) {
        this.shows = shows;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getShows() {
        return shows;
    }

    public void setShows(int shows) {
        this.shows = shows;
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

}
