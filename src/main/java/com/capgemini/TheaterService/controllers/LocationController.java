package com.capgemini.TheaterService.controllers;

import com.capgemini.TheaterService.entities.Theater;
import com.capgemini.TheaterService.services.TheaterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @PostMapping("{id}/theaters")
    public ResponseEntity<Void> addMultipleTheatersInCity(@PathVariable("id") String cityId,
                                                          @RequestBody List<@NotNull @Valid Theater> theaters) {
        theaterService.addMultipleTheaters(theaters, cityId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("{id}/theaters")
    public ResponseEntity<Void> removeAllTheatersFromCity(@PathVariable("id") String cityId) {
        theaterService.removeTheatersFromCity(cityId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
