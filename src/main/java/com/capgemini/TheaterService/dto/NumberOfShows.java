package com.capgemini.TheaterService.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class NumberOfShows {

    @Min(1)
    @Max(5)
    private int shows;

    public NumberOfShows() {
    }

    public NumberOfShows(int shows) {
        this.shows = shows;
    }

    public int getShows() {
        return shows;
    }

    public void setShows(int shows) {
        this.shows = shows;
    }
}
