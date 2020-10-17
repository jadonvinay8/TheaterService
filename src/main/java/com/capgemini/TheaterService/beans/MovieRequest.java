package com.capgemini.TheaterService.beans;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class MovieRequest {

    @NotNull
    private String movieId;
    @NotNull
    private Set<Dimension> dimensions;

    public MovieRequest() {
    }

    public MovieRequest(@NotNull String movieId, @NotNull Set<Dimension> dimensions) {
        this.movieId = movieId;
        this.dimensions = dimensions;
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

    public void setDimensions(Set<Dimension> dimensions) {
        this.dimensions = dimensions;
    }
}
