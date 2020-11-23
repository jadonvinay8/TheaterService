package com.capgemini.TheaterService.controllers;

import com.capgemini.TheaterService.beans.ShortMovie;
import com.capgemini.TheaterService.entities.Movie;
import com.capgemini.TheaterService.entities.Theater;
import com.capgemini.TheaterService.services.TheaterService;
import com.capgemini.TheaterService.services.TheaterServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/cities")
@CrossOrigin
public class LocationController {

    private final TheaterService theaterService;

    public LocationController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @GetMapping("{id}/theaters")
    public ResponseEntity<List<Theater>> getTheatersInCity(@PathVariable("id") String cityId) {
        return new ResponseEntity<>(theaterService.getTheatersInCity(cityId), HttpStatus.OK);
    }

    @GetMapping("{id}/movies")
    public ResponseEntity<Set<ShortMovie>> getMoviesInCity(@PathVariable("id") String cityId) {
        return new ResponseEntity<>(theaterService.getMoviesInCity(cityId), HttpStatus.OK);
    }

    @GetMapping("{id}/movies/details")
    public ResponseEntity<List<Movie>> getFullMoviesInCity(@PathVariable("id") String cityId) {
        return new ResponseEntity<>(theaterService.getFullMoviesInCity(cityId), HttpStatus.OK);
    }

    @PostMapping("{id}/theaters")
    public ResponseEntity<Void> addMultipleTheatersInCity(@PathVariable("id") String cityId,
                                                          @RequestBody List<@NotNull @Valid Theater> theaters) {

        theaterService.addMultipleTheaters(theaters, cityId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}/theaters/{movieId}")
    public ResponseEntity<List<Theater>> getTheatersRunningThisMovie(@PathVariable("id") String cityId,
                                                                     @PathVariable("movieId") String movieId) {

        return new ResponseEntity<>(theaterService.getTheatersRunningThisMovie(cityId, movieId), HttpStatus.OK);
    }

    @DeleteMapping("{id}/theaters")
    public ResponseEntity<Void> removeAllTheatersFromCity(@PathVariable("id") String cityId) {
        theaterService.removeTheatersFromCity(cityId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
