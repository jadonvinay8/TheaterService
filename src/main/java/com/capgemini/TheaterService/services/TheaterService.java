package com.capgemini.TheaterService.services;

import com.capgemini.TheaterService.beans.ShortMovie;
import com.capgemini.TheaterService.entities.Movie;
import com.capgemini.TheaterService.entities.Theater;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TheaterService {

    Theater findTheaterById(String id);

    List<ShortMovie> getMovies(String id);

    List<Theater> getAllTheaters();

    Theater addTheater(Theater theater);

    void removeTheater(String theaterId);

    Theater updateTheater(String id, Theater theater);

    Theater addMovieInTheater(String theaterId, String movieId);

    void removeMovieFromTheater(String theaterId, String movieId);

    void removeTheseMoviesFromTheseTheaters(Map<String, Set<String>> map);

    List<Theater> getTheatersInCity(String cityId);

    Set<ShortMovie> getMoviesInCity(String cityId);

    List<Movie> getFullMoviesInCity(String cityId);

    List<Theater> getTheatersRunningThisMovie(String cityId, String movieId);

    Boolean validateTheaterAndMovie(String theaterId, String movieId);

    void removeTheatersFromCity(String cityId);

    void addMultipleTheaters(List<Theater> theaters);

    Map<String, String> getCitiesByIds(List<String> cityIds);

    void addMultipleTheaters(List<Theater> theaters, String cityId);

    List<String> validateBatchExistence(List<String> theaterIds);
}
