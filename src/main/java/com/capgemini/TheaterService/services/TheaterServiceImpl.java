package com.capgemini.TheaterService.services;

import com.capgemini.TheaterService.beans.Address;
import com.capgemini.TheaterService.beans.MovieRequest;
import com.capgemini.TheaterService.beans.ShortMovie;
import com.capgemini.TheaterService.dao.TheaterDAO;
import com.capgemini.TheaterService.entities.Movie;
import com.capgemini.TheaterService.entities.Theater;
import com.capgemini.TheaterService.exceptions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service class to perform business operations related to Theater functionality
 *
 * @author Vinay Pratap Singh
 *
 */
@Service
@Transactional
public class TheaterServiceImpl implements TheaterService {

    private final TheaterDAO theaterDAO;
    private final RestTemplate restTemplate;

    @Value("${service.location.single}")
    private String singleExistenceUrl;

    @Value("${service.location.batch}")
    private String batchExistenceUrl;

    @Value("${service.movie.single}")
    private String getMovieByIdUrl;

    @Value("${service.screen.movie.add}")
    private String addMovieToScreenUrl;

    @Value("${service.screen.movie.remove}")
    private String removeMovieFromScreenUrl;

    @Value("${service.screen.delete}")
    private String removeScreensUrl;

    @Value("${service.movie.bulk}")
    private String getMoviesByIdsUrl;

    @Autowired
    public TheaterServiceImpl(TheaterDAO theaterDAO, RestTemplate restTemplate) {
        this.theaterDAO = theaterDAO;
        this.restTemplate = restTemplate;
    }

    @Override
    public Theater findTheaterById(String id) {
        return theaterDAO.findById(id).orElseThrow(TheaterNotFoundException::new);
    }

    @Override
    public List<ShortMovie> getMovies(String id) {
        return findTheaterById(id).getMovies();
    }

    @Override
    public List<Theater> getAllTheaters() {
        return StreamSupport
          .stream(theaterDAO.findAll().spliterator(), false)
          .collect(Collectors.toList());
    }

    @Override
    public Theater addTheater(Theater theater) {
        var sanitizedTheater = sanitizeTheater(theater);
        validateCity(theater.getCityId()); // if city not valid, throw city not found exception
        validateTheaterNamingConstraints(sanitizedTheater); // check for naming constraint

        return theaterDAO.save(sanitizedTheater);
    }

    private void validateTheaterNamingConstraints(Theater theaterToValidate) {
        var name = theaterToValidate.getTheaterName().toLowerCase();
        var cityId = theaterToValidate.getCityId();
        String area = theaterToValidate.getAddress().getArea().toLowerCase(); // throws exception if theater with same name is defined in same area

        getTheatersInCity(cityId).stream()
          .filter(theater -> name.equals(theater.getTheaterName().toLowerCase()))
          .filter(theater -> area.equals(theater.getAddress().getArea().toLowerCase()))
          .forEach(theater -> {
              throw new TheaterNameValidationFailedException("Theater with name" + theater.getTheaterName()
                + " is already present in same area");
          });
    }

    private List<Theater> validateTheaterNamingConstraints(List<Theater> theatersToBeValidated) {
        var names = new HashSet<String>();
        var cityIds = new HashSet<String>();
        var areas = new HashSet<String>();

        theatersToBeValidated.forEach(theater -> {
            var sanitizedTheater = sanitizeTheater(theater);
            names.add(sanitizedTheater.getTheaterName().toLowerCase());
            cityIds.add(sanitizedTheater.getCityId());
            areas.add(sanitizedTheater.getAddress().getArea().toLowerCase());
        });

        return getAllTheaters().stream()
          .filter(theater -> names.contains(theater.getTheaterName().toLowerCase()))
          .filter(theater -> cityIds.contains(theater.getCityId()))
          .filter(theater -> areas.contains(theater.getAddress().getArea().toLowerCase()))
          .peek(theater -> {
              throw new TheaterNameValidationFailedException("Theater with name" + theater.getTheaterName()
                + " is already present in same area");
          })
          .collect(Collectors.toList());
    }

    private Theater sanitizeTheater(Theater theater) {
        theater.setCityId(theater.getCityId().trim());
        theater.setTheaterName(theater.getTheaterName().trim());
        var area = theater.getAddress().getArea().trim();
        var address = new Address(theater.getAddress().getCity(), theater.getAddress().getState(),
          area, theater.getAddress().getPincode());
        theater.setAddress(address);
        return theater;
    }

    @Override
    public void removeTheater(String theaterId) {
        var theater = findTheaterById(theaterId);

        var theaterIdList = List.of(theaterId);
        var theaterList = List.of(theater);

        removeUnderlyingScreens(theaterIdList, theaterList);
    }

    @Override
    public Theater updateTheater(String id, Theater theater) {
        var retrievedTheater = findTheaterById(id); // throw an exception if id doesn't exist
        theater.setTheaterId(id);

        // validateTheaterNamingConstraints(theater); // check for naming constraint

        if (retrievedTheater.getCityId().equals(theater.getCityId())) {
            return theaterDAO.save(theater);
        } else {
            throw new InvalidOperationException("can't change city id");
        }
    }

    @Override
    public Theater addMovieInTheater(String theaterId, String movieId) {
        var theater = findTheaterById(theaterId);
        var movies = theater.getMovies();
        movies.stream()
          .filter(shortMovie -> shortMovie.getId().equals(movieId))
          .forEach(shortMovie -> {
              throw new InvalidOperationException("Movie already exists in the theater");
          });

        var movie = retrieveMovie(movieId);
//        var movieRequest = new MovieRequest(movieId, Set.of(movie.getMovieDimension()));
//        var requestUrl = addMovieToScreenUrl.replaceAll("theaterId", theaterId);
//        callScreenService(requestUrl, movieRequest, HttpMethod.PUT);

        // Add movie to theater if everything goes fine in screen service
        movies.add(new ShortMovie(movieId, movie.getName()));
        theater.setMovies(movies);

        return updateTheater(theaterId, theater);
    }

    @Override
    public void removeMovieFromTheater(String theaterId, String movieId) {
        var theater = findTheaterById(theaterId);
        var isMoviePresent = theater.getMovies()
          .stream()
          .anyMatch(shortMovie -> shortMovie.getId().equals(movieId));

        if (!isMoviePresent) throw new InvalidOperationException("The movie does not exist in this theater");

//        var requestUrl = removeMovieFromScreenUrl.replaceAll("theaterId", theaterId)
//          .replaceAll("movieId", movieId);
//        callScreenService(requestUrl, null, HttpMethod.DELETE);

        // Remove the movie if everything goes fine in screen service
        var movies = theater.getMovies()
          .stream()
          .filter(Predicate.not(shortMovie -> shortMovie.getId().equals(movieId)))
          .collect(Collectors.toList());

        theater.setMovies(movies);
        updateTheater(theaterId, theater);
    }

    @Override
    public void removeTheseMoviesFromTheseTheaters(Map<String, Set<String>> map) {
        List<Theater> theatersToBeUpdated = new ArrayList<>();
        map.forEach((theaterId, movieIds) -> {
            var theater = findTheaterById(theaterId);
            List<ShortMovie> movies = theater
              .getMovies()
              .stream()
              .filter(Predicate.not(movie -> movieIds.contains(movie.getId())))
              .collect(Collectors.toList());
            theater.setMovies(movies);
            theatersToBeUpdated.add(theater);
        });

        theaterDAO.saveAll(theatersToBeUpdated);
    }

    private Movie retrieveMovie(String movieId) {
        var requestUrl = getMovieByIdUrl + movieId;
        var response = callExternalService(null, requestUrl, HttpMethod.GET, Movie.class);

        return (Movie) response.getBody();
    }

    private void callScreenService(String url, MovieRequest movie, HttpMethod method) {
        var requestBody = movie == null ? null : stringify(movie);
        callExternalService(requestBody, url, method, Void.class);
    }

    @Override
    public List<Theater> getTheatersInCity(String cityId) {
        validateCity(cityId); // if city not valid, throw city not found exception
        return theaterDAO.findByCityId(cityId);
    }

    @Override
    public Set<ShortMovie> getMoviesInCity(String cityId) {
        return getTheatersInCity(cityId)
          .stream()
          .flatMap(theater -> theater.getMovies().stream())
          .collect(Collectors.toSet());
    }

    @Override
    public List<Movie> getFullMoviesInCity(String cityId) {
        Set<String> movieIds = getTheatersInCity(cityId)
          .stream()
          .flatMap(theater -> theater.getMovies().stream())
          .map(ShortMovie::getId)
          .collect(Collectors.toSet());

        var requestBody = stringify(movieIds);
        ResponseEntity<Movie[]> response = (ResponseEntity<Movie[]>) callExternalService(requestBody, getMoviesByIdsUrl, HttpMethod.POST, Movie[].class);
        return Arrays.asList(response.getBody());
    }

    @Override
    public List<Theater> getTheatersRunningThisMovie(String cityId, String movieId) {
        List<Theater> theaters = new ArrayList<>();

        getTheatersInCity(cityId).forEach(theater -> theater.getMovies()
          .stream()
          .map(ShortMovie::getId)
          .filter(id -> id.equals(movieId))
          .peek(id -> theaters.add(theater))
          .findFirst());

        return theaters;
    }

    @Override
    public Boolean validateTheaterAndMovie(String theaterId, String movieId) {
        var theater = findTheaterById(theaterId);
        return theater.getMovies()
          .stream()
          .anyMatch(movie -> movie.getId().equals(movieId));
    }

    @Override
    public void removeTheatersFromCity(String cityId) {
        validateCity(cityId); // if city not valid, throw city not found exception
        var theaters = theaterDAO.findByCityId(cityId);
        if (theaters.isEmpty())
            return;

        List<String> theaterIds = theaters.stream()
          .map(Theater::getTheaterId)
          .collect(Collectors.toList());

        removeUnderlyingScreens(theaterIds, theaters);
    }

    @Override
    public void addMultipleTheaters(List<Theater> theaters) {
        List<String> cityIds = new ArrayList<>(theaters.size());
        validateInputList(theaters).forEach(theater -> {
            if (theater.getCityId() == null)
                throw new NullPointerException("Null value provided for cityId");
            cityIds.add(theater.getCityId());
        });

        List<Theater> sanitizedTheaters = validateTheaterNamingConstraints(theaters);// check for naming constraint

        // validate CityIds existence
        getCitiesByIds(cityIds);

        theaterDAO.saveAll(sanitizedTheaters); // save if everything is fine
    }

    @Override
    public Map<String, String> getCitiesByIds(List<String> cityIds) {
        var ids = stringify(cityIds);
        return (Map<String, String>) callExternalService(ids, batchExistenceUrl, HttpMethod.POST, Map.class).getBody();
    }

    private String stringify(Iterable<String> ids) {
        try {
            return new ObjectMapper().writeValueAsString(ids);
        } catch (JsonProcessingException e) {
            throw new InvalidOperationException("Can't process JSON");
        }
    }

    private String stringify(MovieRequest movie) {
        try {
            return new ObjectMapper().writeValueAsString(movie);
        } catch (JsonProcessingException e) {
            throw new InvalidOperationException("Can't process JSON");
        }
    }

    @Override
    public void addMultipleTheaters(List<Theater> theaters, String cityId) {
        validateCity(cityId); // if city not valid, throw city not found exception

        List<Theater> filteredTheaters = validateInputList(theaters)
          .stream()
          .filter(Objects::nonNull)
          .peek(theater -> theater.setCityId(cityId.trim()))
          .map(this::sanitizeTheater)
          .collect(Collectors.toList());

        validateTheaterNamingConstraints(filteredTheaters); // check for naming constraint

        theaterDAO.saveAll(filteredTheaters);
    }

    private void validateCity(String cityId) {
        var requestUrl = singleExistenceUrl + cityId.trim();
        callExternalService(null, requestUrl, HttpMethod.GET, Object.class); // throws exception if id is invalid
    }

    private List<Theater> validateInputList(List<Theater> list) {
        return Optional.ofNullable(list).orElseThrow(() -> {
            throw new NullPointerException("Null value supplied in payload");
        });
    }

    private void removeUnderlyingScreens(List<String> theaterIds, List<Theater> theaters) {
        var requestBody = stringify(theaterIds);
        callExternalService(requestBody, removeScreensUrl, HttpMethod.DELETE, Boolean.class);
        theaterDAO.deleteAll(theaters);
    }

    private ResponseEntity<?> callExternalService(String requestBody, String url, HttpMethod method, Class<?> claas) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var entity = new HttpEntity<>(requestBody, headers);

        try {
            return requestBody == null && method == HttpMethod.GET
              ? restTemplate.getForEntity(url, claas)
              : restTemplate.exchange(url, method, entity, claas);
        } catch (HttpClientErrorException e) {
            throw new EntityNotFoundException("No entity was found with that id");
        } catch (HttpServerErrorException e) {
            throw new OperationFailedException("Something went wrong in other API");
        }
    }

}
