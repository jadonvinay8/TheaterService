package com.capgemini.TheaterService.beans;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class MovieRequest {

    @NotNull
    private String movieId;
    @NotNull
    private Set<Dimension> dimensions;
    @NotEmpty
    private String movieDuration;
    @NotEmpty
    private Integer noOfShows;

    public MovieRequest() {
    }

    public MovieRequest(@NotNull String movieId, @NotNull Set<Dimension> dimensions, @NotEmpty String movieDuration, @NotEmpty Integer noOfShows) {
        this.movieId = movieId;
        this.dimensions = dimensions;
        this.movieDuration = movieDuration;
        this.noOfShows = noOfShows;
    }

    public String getMovieId() {
        return movieId;
    }

    public Set<Dimension> getDimensions() {
        return dimensions;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setMovieDuration(String movieDuration) {
        this.movieDuration = movieDuration;
    }

    public void setNoOfShows(Integer noOfShows) {
        this.noOfShows = noOfShows;
    }

    public String getMovieDuration() {
        return movieDuration;
    }

    public Integer getNoOfShows() {
        return noOfShows;
    }

    public void setDimensions(Set<Dimension> dimensions) {
        this.dimensions = dimensions;
    }
}
