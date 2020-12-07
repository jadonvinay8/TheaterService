package com.capgemini.TheaterService.beans;

import com.capgemini.TheaterService.dto.ShowsInfo;

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

    private ShowsInfo showsInfo;

    public MovieRequest() {
    }

    public MovieRequest(@NotNull String movieId, @NotNull Set<Dimension> dimensions, @NotEmpty String movieDuration, ShowsInfo showsInfo) {
        this.movieId = movieId;
        this.dimensions = dimensions;
        this.movieDuration = movieDuration;
        this.showsInfo = showsInfo;
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

    public String getMovieDuration() {
        return movieDuration;
    }

    public void setDimensions(Set<Dimension> dimensions) {
        this.dimensions = dimensions;
    }

    public ShowsInfo getShowsInfo() {
        return showsInfo;
    }

    public void setShowsInfo(ShowsInfo showsInfo) {
        this.showsInfo = showsInfo;
    }

}
