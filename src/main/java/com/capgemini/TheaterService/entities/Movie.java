package com.capgemini.TheaterService.entities;

import com.capgemini.TheaterService.beans.Dimension;

import java.util.Date;
import java.util.List;

public class Movie {

    private String movieId;
    private String name;
    private String genre;
    private String duration;
    private Dimension movieDimension;
    private Double rating;
    private String moviePoster;
    private Date dateReleased;
    private List<String> casts;
    private List<String> languages;

    public Movie() {
        // Default Constructor
    }

    public Movie(String movieId, String name, String genre, String duration, Dimension movieDimension, Double rating,
                 String moviePoster, Date dateReleased, List<String> casts, List<String> languages) {
        this.movieId = movieId;
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.movieDimension = movieDimension;
        this.rating = rating;
        this.moviePoster = moviePoster;
        this.dateReleased = dateReleased;
        this.casts = casts;
        this.languages = languages;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Dimension getMovieDimension() {
        return movieDimension;
    }

    public void setMovieDimension(Dimension movieDimension) {
        this.movieDimension = movieDimension;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Date getDateReleased() {
        return dateReleased;
    }

    public void setDateReleased(Date dateReleased) {
        this.dateReleased = dateReleased;
    }

    public List<String> getCasts() {
        return casts;
    }

    public void setCasts(List<String> casts) {
        this.casts = casts;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

}
