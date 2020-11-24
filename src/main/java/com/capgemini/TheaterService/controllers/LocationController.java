package com.capgemini.TheaterService.controllers;

import com.capgemini.TheaterService.dto.MicroserviceResponse;
import com.capgemini.TheaterService.entities.Theater;
import com.capgemini.TheaterService.services.TheaterService;
import com.capgemini.TheaterService.utils.ResponseBuilder;
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
    private static final String SUCCESS_MESSAGE = "Operation Completed successfully";


    public LocationController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @GetMapping("{id}/theaters")
    public ResponseEntity<MicroserviceResponse> getTheatersInCity(@PathVariable("id") String cityId) {
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.OK.value(), theaterService.getTheatersInCity(cityId), null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}/movies")
    public ResponseEntity<MicroserviceResponse> getMoviesInCity(@PathVariable("id") String cityId) {
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.OK.value(), theaterService.getMoviesInCity(cityId), null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}/movies/details")
    public ResponseEntity<MicroserviceResponse> getFullMoviesInCity(@PathVariable("id") String cityId) {
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.OK.value(), theaterService.getFullMoviesInCity(cityId), null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("{id}/theaters")
    public ResponseEntity<MicroserviceResponse> addMultipleTheatersInCity(@PathVariable("id") String cityId,
                                                          @RequestBody List<@NotNull @Valid Theater> theaters) {

        theaterService.addMultipleTheaters(theaters, cityId);
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.CREATED.value(), SUCCESS_MESSAGE, null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}/theaters/{movieId}")
    public ResponseEntity<MicroserviceResponse> getTheatersRunningThisMovie(@PathVariable("id") String cityId,
                                                                     @PathVariable("movieId") String movieId) {

        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.OK.value(), theaterService.getTheatersRunningThisMovie(cityId, movieId), null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}/theaters")
    public ResponseEntity<MicroserviceResponse> removeAllTheatersFromCity(@PathVariable("id") String cityId) {
        theaterService.removeTheatersFromCity(cityId);
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.NO_CONTENT.value(), SUCCESS_MESSAGE, null);
        return ResponseEntity.ok(response);
    }

}
