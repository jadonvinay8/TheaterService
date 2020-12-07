package com.capgemini.TheaterService.services;

import com.capgemini.TheaterService.beans.ShortMovie;
import com.capgemini.TheaterService.dto.ShowsInfo;
import com.capgemini.TheaterService.entities.Movie;
import com.capgemini.TheaterService.entities.Theater;
import com.capgemini.TheaterService.exceptions.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TheaterService {

    Theater findTheaterById(String id) throws TheaterNotFoundException;

    List<ShortMovie> getMovies(String id);

    List<Theater> getAllTheaters();

    Theater addTheater(Theater theater) throws MicroserviceException, TheaterNameValidationFailedException;

    void removeTheater(String theaterId) throws TheaterNotFoundException;

    Theater updateTheater(String id, Theater theater) throws TheaterNotFoundException, InvalidOperationException;

    Theater addMovieInTheater(String theaterId, String movieId, ShowsInfo showsInfo) throws TheaterNotFoundException, MicroserviceException;

    void removeMovieFromTheater(String theaterId, String movieId) throws TheaterNotFoundException, MicroserviceException;

    void removeTheseMoviesFromTheseTheaters(Map<String, Set<String>> map) throws TheaterNotFoundException ;

    List<Theater> getTheatersInCity(String cityId) throws CityNotFoundException;

    Set<ShortMovie> getMoviesInCity(String cityId) throws CityNotFoundException;

    List<Movie> getFullMoviesInCity(String cityId) throws CityNotFoundException, MicroserviceException;

    List<Theater> getTheatersRunningThisMovie(String cityId, String movieId) throws CityNotFoundException;

    Boolean validateTheaterAndMovie(String theaterId, String movieId) throws TheaterNotFoundException;

    void removeTheatersFromCity(String cityId) throws MicroserviceException;

    void addMultipleTheaters(List<Theater> theaters) throws TheaterNameValidationFailedException, NullPointerException, MicroserviceException, CityNotFoundException;

    Map<String, String> getCitiesByIds(List<String> cityIds) throws MicroserviceException;

    void addMultipleTheaters(List<Theater> theaters, String cityId) throws MicroserviceException, TheaterNameValidationFailedException;

    List<String> validateBatchExistence(List<String> theaterIds);

}
