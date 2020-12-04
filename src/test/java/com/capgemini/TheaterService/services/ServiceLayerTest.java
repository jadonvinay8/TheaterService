package com.capgemini.TheaterService.services;

import com.capgemini.TheaterService.beans.Address;
import com.capgemini.TheaterService.beans.Dimension;
import com.capgemini.TheaterService.beans.ShortMovie;
import com.capgemini.TheaterService.dao.TheaterDAO;
import com.capgemini.TheaterService.dto.MicroserviceResponse;
import com.capgemini.TheaterService.dto.NumberOfShows;
import com.capgemini.TheaterService.entities.Movie;
import com.capgemini.TheaterService.entities.Theater;
import com.capgemini.TheaterService.exceptions.InvalidOperationException;
import com.capgemini.TheaterService.exceptions.MicroserviceException;
import com.capgemini.TheaterService.exceptions.TheaterNameValidationFailedException;
import com.capgemini.TheaterService.exceptions.TheaterNotFoundException;
import com.capgemini.TheaterService.utils.ResponseBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceLayerTest {

    @Mock
    private TheaterDAO theaterDAO;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TheaterServiceImpl service;

    private static final Map<String, Theater> theaterMap = new HashMap<>();

    @Value("${service.screen.delete}")
    private String removeScreensUrl;


    @BeforeClass
    public static void init() {
        theaterMap.put("1", new Theater("1", "1", "Theater1", new Address("Pune", "Uttar Pradesh", "", "201335"), 4.2, new ArrayList<>()));
        theaterMap.put("2", new Theater("2", "2", "Theater2", new Address("Delhi", "Uttar Pradesh", "", "201335"), 4.2, new ArrayList<>()));
        theaterMap.put("3", new Theater("3", "1", "Theater3", new Address("Pune", "Uttar Pradesh", "", "201335"), 4.2, new ArrayList<>()));
        theaterMap.put("4", new Theater("4", "2", "Theater4", new Address("Delhi", "Uttar Pradesh", "", "201335"), 4.2, new ArrayList<>()));
    }

    @Test
    public void testGetAllTheaters() {
        List<Theater> theaters = new ArrayList<>();
        theaterMap.forEach((key, value) -> theaters.add(value));
        when(theaterDAO.findAll()).thenReturn(theaters);
        assertEquals(theaters, service.getAllTheaters());
    }

    @Test
    public void testFindTheaterById() {
        String id = "1";
        when(theaterDAO.findById(id)).thenReturn(Optional.of(theaterMap.get(id)));
        assertEquals(theaterMap.get(id), service.findTheaterById(id));
    }

    @Test(expected = TheaterNotFoundException.class)
    public void testFindByIdThrowsTheaterNotFoundException() {
        String id = "5";
        when(theaterDAO.findById(id)).thenThrow(TheaterNotFoundException.class);
        assertEquals(theaterMap.get(id), service.findTheaterById(id));
    }

    @Test
    public void testGetMovies() {
        String id = "1";
        when(theaterDAO.findById(id)).thenReturn(Optional.of(theaterMap.get(id)));
        assertEquals(theaterMap.get(id).getMovies(), service.getMovies(id));
    }

    @Test
    public void testAddTheater() {
        Theater theater = theaterMap.get("1");
        when(theaterDAO.save(theater)).thenReturn(theater);
        MicroserviceResponse response = ResponseBuilder.build(200, "success", null);
        when(restTemplate.getForEntity(anyString(), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        assertEquals(theater, service.addTheater(theater));
    }

    @Test(expected = MicroserviceException.class)
    public void testAddTheaterThrowsMicroserviceException() {
        Theater theater = theaterMap.get("1");
        MicroserviceResponse response = ResponseBuilder.build(404, null, null);
        when(restTemplate.getForEntity(anyString(), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        assertEquals(theater, service.addTheater(theater));
    }

    @Test(expected = TheaterNameValidationFailedException.class)
    public void testAddTheaterThrowsTheaterNameValidationFailedException() {
        List<Theater> theaters = new ArrayList<>();
        theaterMap.forEach((key, value) -> theaters.add(value));
        Theater theater = theaterMap.get("1");
        when(theaterDAO.findByCityId("1")).thenReturn(theaters);
        MicroserviceResponse response = ResponseBuilder.build(200, "success", null);
        when(restTemplate.getForEntity(anyString(), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        assertEquals(theater, service.addTheater(theater));
    }

    @Test
    public void testRemoveTheater() {
        String id = "2";
        when(theaterDAO.findById(id)).thenReturn(Optional.ofNullable(theaterMap.get(id)));
        MicroserviceResponse response = ResponseBuilder.build(200, "success", null);
        when(restTemplate.exchange(eq(removeScreensUrl), any(HttpMethod.class), any(HttpEntity.class), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        service.removeTheater(id);
        theaterMap.remove(id);
        assertEquals(3, theaterMap.size());
    }

    @Test
    public void testUpdateTheater() {
        String id = "2";
        Theater theater = theaterMap.get(id);
        when(theaterDAO.findById(id)).thenReturn(Optional.ofNullable(theaterMap.get(id)));
        when(theaterDAO.save(theater)).thenReturn(theater);
        assertEquals(theater, service.updateTheater(id, theater));
    }

    @Test
    public void testAddMovieInTheater() {
        String id = "2";
        Theater theater = theaterMap.get(id);
        when(theaterDAO.findById(id)).thenReturn(Optional.ofNullable(theaterMap.get(id)));
        Movie movie = new Movie("1", "movie", "", "", Dimension._2D, 5.0, Date.from(Instant.now()), new ArrayList<>(), new ArrayList<>());
        MicroserviceResponse response = ResponseBuilder.build(200, movie, null);
        ReflectionTestUtils.setField(service, "addMovieToScreenUrl", id);
        ReflectionTestUtils.setField(service, "getMovieByIdUrl", id);
        when(restTemplate.exchange(eq(id), any(HttpMethod.class), any(HttpEntity.class), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        when(theaterDAO.save(theater)).thenReturn(theater);
        when(restTemplate.getForEntity(id + id, MicroserviceResponse.class)).thenReturn(ResponseEntity.ok(response));
        assertEquals(theater, service.addMovieInTheater(id, id, new NumberOfShows(5)));
    }

    @Test
    public void testRemoveMovieFromTheater() {
        String id = "1";
        Theater theater = theaterMap.get(id);
        Movie movie = new Movie("1", "movie", "", "", Dimension._2D, 5.0, Date.from(Instant.now()), new ArrayList<>(), new ArrayList<>());
        theater.setMovies(List.of(new ShortMovie(movie.getMovieId(), movie.getName())));
        when(theaterDAO.findById(id)).thenReturn(Optional.of(theater));
        MicroserviceResponse response = ResponseBuilder.build(200, movie, null);
        ReflectionTestUtils.setField(service, "removeMovieFromScreenUrl", id);
        ReflectionTestUtils.setField(service, "getMovieByIdUrl", id);
        when(restTemplate.exchange(eq(id), any(HttpMethod.class), any(HttpEntity.class), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        when(theaterDAO.save(theater)).thenReturn(theater);
        service.removeMovieFromTheater(id, id);
    }

    @Test(expected = InvalidOperationException.class)
    public void testRemoveMovieFromTheaterThrowsInvalidOperationException() {
        String id = "4";
        when(theaterDAO.findById(id)).thenReturn(Optional.ofNullable(theaterMap.get(id)));
        ReflectionTestUtils.setField(service, "addMovieToScreenUrl", id);
        ReflectionTestUtils.setField(service, "getMovieByIdUrl", id);
        service.removeMovieFromTheater(id, id);
    }

    @Test
    public void testRemoveTheseMoviesFromTheseTheaters() {
        String id = "1";
        Theater theater = theaterMap.get(id);
        Movie movie = new Movie("1", "movie", "", "", Dimension._2D, 5.0, Date.from(Instant.now()), new ArrayList<>(), new ArrayList<>());
        theater.setMovies(List.of(new ShortMovie(movie.getMovieId(), movie.getName())));
        when(theaterDAO.findById(id)).thenReturn(Optional.of(theater));
        HashMap<String, Set<String>> map = new HashMap<>();
        map.put("1", Set.of("1", "2"));
        service.removeTheseMoviesFromTheseTheaters(map);
    }

    @Test
    public void testGetTheatersInCity() {
        String id = "1";
        List<Theater> theaters = new ArrayList<>();
        theaterMap.forEach((key, value) -> theaters.add(value));
        MicroserviceResponse response = ResponseBuilder.build(200, "success", null);
        when(restTemplate.getForEntity(anyString(), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        when(theaterDAO.findByCityId(id)).thenReturn(theaters);
        assertEquals(theaters, service.getTheatersInCity(id));
    }

    @Test
    public void testGetMoviesInCity() {
        String id = "1";
        List<Theater> theaters = new ArrayList<>();
        theaterMap.forEach((key, value) -> theaters.add(value));
        ShortMovie movie1 = new ShortMovie("1", "movie");
        ShortMovie movie2 = new ShortMovie("2", "movie");
        theaters.get(0).setMovies(List.of(movie1));
        MicroserviceResponse response = ResponseBuilder.build(200, "success", null);
        when(restTemplate.getForEntity(anyString(), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        when(theaterDAO.findByCityId(id)).thenReturn(theaters);
        assertEquals(Set.of(movie1, movie2), service.getMoviesInCity(id));
    }

    @Test
    public void testGetFullMoviesInCity() {
        String id = "1";
        List<Theater> theaters = new ArrayList<>();
        theaterMap.forEach((key, value) -> theaters.add(value));
        ShortMovie movie1 = new ShortMovie("1", "movie");
        theaters.get(0).setMovies(List.of(movie1));
        MicroserviceResponse response = ResponseBuilder.build(200, "success", null);
        when(restTemplate.getForEntity(anyString(), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        when(theaterDAO.findByCityId(id)).thenReturn(theaters);
        ReflectionTestUtils.setField(service, "getMoviesByIdsUrl", id);
        Movie movie3 = new Movie("1", "movie", "", "", Dimension._2D, 5.0, Date.from(Instant.now()), new ArrayList<>(), new ArrayList<>());
        Movie movie4 = new Movie("2", "movie", "", "", Dimension._2D, 5.0, Date.from(Instant.now()), new ArrayList<>(), new ArrayList<>());
        MicroserviceResponse movieResponse = ResponseBuilder.build(200, List.of(movie3, movie4), null);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(movieResponse));
        assertEquals(List.of(movie3, movie4), service.getFullMoviesInCity(id));
    }

    @Test
    public void testGetTheatersRunningThisMovie() {
        String id = "1";
        List<Theater> theaters = new ArrayList<>();
        theaterMap.forEach((key, value) -> theaters.add(value));
        ShortMovie movie1 = new ShortMovie("1", "movie");
        theaters.get(0).setMovies(List.of(movie1));
        when(theaterDAO.findByCityId(id)).thenReturn(theaters);
        MicroserviceResponse response = ResponseBuilder.build(200, "success", null);
        when(restTemplate.getForEntity(anyString(), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        assertEquals(List.of(theaters.get(0)), service.getTheatersRunningThisMovie(id, id));
    }

    @Test
    public void testValidateTheaterAndMovie() {
        String id = "1";
        Theater theater = theaterMap.get(id);
        theater.setMovies(List.of(new ShortMovie("1", "movie")));
        when(theaterDAO.findById(id)).thenReturn(Optional.of(theater));
        assertTrue(service.validateTheaterAndMovie(id, id));
    }

    @Test
    public void testRemoveTheatersFromCity() {
        String id = "1";
        List<Theater> theaters = new ArrayList<>();
        theaterMap.forEach((key, value) -> theaters.add(value));
        ShortMovie movie1 = new ShortMovie("1", "movie");
        theaters.get(0).setMovies(List.of(movie1));
        MicroserviceResponse response = ResponseBuilder.build(200, "success", null);
        when(restTemplate.getForEntity(anyString(), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        when(theaterDAO.findByCityId(id)).thenReturn(theaters);
        when(restTemplate.exchange(eq(removeScreensUrl), any(HttpMethod.class), any(HttpEntity.class), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        service.removeTheatersFromCity(id);
    }

    @Test
    public void testAddMultipleTheaters() {
        List<Theater> theaters = new ArrayList<>();
        theaterMap.forEach((key, value) -> theaters.add(value));
        when(theaterDAO.findAll()).thenReturn(List.of(theaters.get(0)));
        ReflectionTestUtils.setField(service, "batchExistenceUrl", "1");
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        map.put("Invalid", "[]");
        MicroserviceResponse response = ResponseBuilder.build(200, map, null);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        service.addMultipleTheaters(List.of(theaters.get(2), theaters.get(3)));
    }

    @Test
    public void testGetCitiesByIds() {
        ReflectionTestUtils.setField(service, "batchExistenceUrl", "1");
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        map.put("Invalid", "[]");
        MicroserviceResponse response = ResponseBuilder.build(200, map, null);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        assertEquals(map, service.getCitiesByIds(List.of("1")));
    }

    @Test
    public void testAddMultipleTheatersOverloaded() {
        String id = "1";
        MicroserviceResponse response = ResponseBuilder.build(200, "success", null);
        when(restTemplate.getForEntity(anyString(), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        List<Theater> theaters = new ArrayList<>();
        theaterMap.forEach((key, value) -> theaters.add(value));
        when(theaterDAO.findAll()).thenReturn(List.of(theaters.get(0)));
        service.addMultipleTheaters(List.of(theaters.get(2), theaters.get(3)), id);
    }

    @Test(expected = NullPointerException.class)
    public void testAddMultipleTheatersOverloadedThrowsNullPointerException() {
        String id = "1";
        MicroserviceResponse response = ResponseBuilder.build(200, "success", null);
        when(restTemplate.getForEntity(anyString(), eq(MicroserviceResponse.class))).thenReturn(ResponseEntity.ok(response));
        service.addMultipleTheaters(null, id);
    }

    @Test
    public void testValidateBatchExistence() {
        when(theaterDAO.findById(anyString())).thenReturn(Optional.ofNullable(theaterMap.get("1")));
        assertEquals(new ArrayList<>(), service.validateBatchExistence(List.of("1", "2", "3")));
    }

    @Test
    public void testValidateBatchExistenceThrowsTheaterNotFoundException() {
        when(theaterDAO.findById(anyString())).thenThrow(TheaterNotFoundException.class);
        assertEquals(List.of("1", "2", "3"), service.validateBatchExistence(List.of("1", "2", "3")));
    }

}
