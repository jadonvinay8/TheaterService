package com.capgemini.TheaterService.services;

import com.capgemini.TheaterService.beans.ShortMovie;
import com.capgemini.TheaterService.dao.TheaterDAO;
import com.capgemini.TheaterService.entities.Movie;
import com.capgemini.TheaterService.entities.Theater;
import com.capgemini.TheaterService.exceptions.CityNotFoundException;
import com.capgemini.TheaterService.exceptions.InvalidOperationException;
import com.capgemini.TheaterService.exceptions.TheaterNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service class to perform CRUD operations related to Theater functionality
 *
 * @author Vinay Pratap Singh
 */
@Service
@Transactional
public class TheaterService {

    private final TheaterDAO theaterDAO;

    private final RestTemplate restTemplate;

    @Value("${service.location.single}")
    private String singleExistenceUrl;

    @Value("${service.location.batch}")
    private String batchExistenceUrl;

    @Value("${service.movie.single}")
    private String getMovieByIdUrl;

    @Value("${service.screen.add}")
    private String addMovieToScreenUrl;

    @Value("${service.screen.remove}")
    private String removeMovieFromScreenUrl;

    @Autowired
    public TheaterService(TheaterDAO theaterDAO, RestTemplate restTemplate) {
        this.theaterDAO = theaterDAO;
        this.restTemplate = restTemplate;
    }

    public Theater findTheaterById(String id) {
        return theaterDAO.findById(id).orElseThrow(TheaterNotFoundException::new);
    }

    public List<ShortMovie> getMovies(String id) {
        return findTheaterById(id).getMovies();
    }

    public List<Theater> getAllTheaters() {
        return StreamSupport
                .stream(theaterDAO.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Theater addTheater(Theater theater) {
        validateCity(theater.getCityId()); // if city not valid, throw city not found exception
        return theaterDAO.save(theater);
    }

    public void removeTheater(String theaterId) {
        var theater = findTheaterById(theaterId);
        // TODO: Remove the screens from the screen API
        theaterDAO.delete(theater);
    }

    public Theater updateTheater(String id, Theater theater) {
        var retrievedTheater = findTheaterById(id); // throw an exception if id doesn't exist
        theater.setTheaterId(id);

        if (retrievedTheater.getCityId().equals(theater.getCityId())) {
            return theaterDAO.save(theater);
        } else {
            throw new InvalidOperationException("can't change city id");
        }
    }

    public Theater addMovieInTheater(String theaterId, String movieId) {
        var theater = findTheaterById(theaterId);
        var movie = retrieveMovie(movieId);

        // callScreenService(removeMovieFromScreenUrl, movie, theaterId);

        // Add movie to theater if everything goes fine in screen service
        var movies = theater.getMovies();
        movies.add(new ShortMovie(movieId, movie.getName()));
        theater.setMovies(movies);
        return updateTheater(theaterId, theater);
    }

    public void removeMovieFromTheater(String theaterId, String movieId) {
        var theater = findTheaterById(theaterId);
        var movie = retrieveMovie(movieId);

        // callScreenService(addMovieToScreenUrl, movie, theaterId);

        // Remove the movie if everything goes fine in screen service
        var movies= theater.getMovies()
                .stream()
                .filter(Predicate.not(shortMovie -> shortMovie.getId().equals(movieId)))
                .collect(Collectors.toList());

        theater.setMovies(movies);
        updateTheater(theaterId, theater);
    }

    private Movie retrieveMovie(String movieId) {
        var requestUrl = getMovieByIdUrl + movieId;
        var response = callExternalService(null, requestUrl, Movie.class);

        return (Movie) response.getBody();
    }

    private void callScreenService(String url, Movie movie, String theaterId) {
        // TODO: Call the screen service to do the needful
        callExternalService(movie.toString(), url, theaterId.getClass());
    }

    public List<Theater> getTheatersInCity(String cityId) {
        validateCity(cityId); // if city not valid, throw city not found exception
        return theaterDAO.findByCityId(cityId);
//                .stream()
//                .filter(Predicate.not(theater -> theater.getMovies().isEmpty()))
//                .collect(Collectors.toList());
    }

    public void removeTheatersFromCity(String cityId) {
        validateCity(cityId); // if city not valid, throw city not found exception
        List<Theater> theaters = theaterDAO.findByCityId(cityId);

        // TODO: Remove the screens from the screen API

        theaterDAO.deleteAll(theaters);
    }

    public void addMultipleTheaters(List<Theater> theaters) {
        List<String> cityIds = new ArrayList<>(theaters.size());
        validateInputList(theaters).forEach(theater -> {
            if (theater.getCityId() == null)
                throw new NullPointerException("Null value provided for cityId");
            cityIds.add(theater.getCityId());
        });

        // Calling bulk validator to see if all these ids exist
        var ids = stringify(cityIds);
        callExternalService(ids, batchExistenceUrl, Boolean.class); // throws exception if any Id is invalid

        theaterDAO.saveAll(theaters); // save if everything is fine
    }

    private String stringify(List<String> cityIds) {
        try {
            return new ObjectMapper().writeValueAsString(cityIds);
        } catch (JsonProcessingException e) {
            throw new InvalidOperationException("Can't process JSON");
        }
    }

    public void addMultipleTheaters(List<Theater> theaters, String cityId) {
        validateCity(cityId); // if city not valid, throw city not found exception

        List<Theater> filteredTheaters = validateInputList(theaters)
                .stream()
                .filter(Objects::nonNull)
                .peek(theater -> theater.setCityId(cityId))
                .collect(Collectors.toList());

        theaterDAO.saveAll(filteredTheaters);
    }

    private void validateCity(String cityId) {
        var requestUrl = singleExistenceUrl + cityId;
        callExternalService(null, requestUrl, Object.class); // throws the exception if id is invalid
    }

    private List<Theater> validateInputList(List<Theater> list) {
        return Optional.ofNullable(list)
                .orElseThrow(() -> {
                    throw new NullPointerException("Null value supplied in payload");
                });
    }

    private ResponseEntity<?> callExternalService(String requestBody, String url, Class<?> claas) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var entity = new HttpEntity<>(requestBody, headers);

        try {
            return requestBody == null
                    ? restTemplate.getForEntity(url, claas)
                    : restTemplate.postForEntity(url, entity, claas);
        } catch (HttpClientErrorException e) {
            throw new CityNotFoundException("No object was found with that id");
        }
    }

}
