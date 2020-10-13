package com.capgemini.TheaterService.controllers;

import java.util.List;

import com.capgemini.TheaterService.beans.ShortMovie;
import com.capgemini.TheaterService.entities.Theater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capgemini.TheaterService.services.TheaterService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Controller for handling theater related end-points
 *
 * @author Vinay Pratap Singh
 */
@RestController
@RequestMapping("v1/theaters")
public class TheaterController {

    private final TheaterService theaterService;

    @Autowired
    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @GetMapping
    public ResponseEntity<List<Theater>> getAllTheaters() {
        return new ResponseEntity<>(theaterService.getAllTheaters(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Theater> addTheater(@Valid @RequestBody Theater theater) {
        return new ResponseEntity<>(theaterService.addTheater(theater), HttpStatus.CREATED);
    }

    @PostMapping("batch")
    public ResponseEntity<Void> addMultipleTheaters(@RequestBody List<@NotNull @Valid Theater> theaters) {
        theaterService.addMultipleTheaters(theaters);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Theater> updateTheater(@PathVariable("id") String id, @Valid @RequestBody Theater theater) {
        return new ResponseEntity<>(theaterService.updateTheater(id, theater), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeTheater(@PathVariable("id") String id) {
        theaterService.removeTheater(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{id}/movies")
    public ResponseEntity<List<ShortMovie>> getMoviesInATheater(@PathVariable("id") String id) {
        return new ResponseEntity<>(theaterService.getMovies(id), HttpStatus.OK);
    }

    @PostMapping("/{id}/movies/{movieId}")
    public ResponseEntity<Theater> addMovieToTheater(@PathVariable("id") @NotNull String id,
                                                      @PathVariable("movieId") @NotNull String movieId) {

        return new ResponseEntity<>(theaterService.addMovieInTheater(id, movieId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/movies/{movieId}")
    public ResponseEntity<Void> removeMovieFromTheater(@PathVariable("id") String theaterId,
                                                       @PathVariable("movieId") String movieId) {

        theaterService.removeMovieFromTheater(theaterId, movieId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
