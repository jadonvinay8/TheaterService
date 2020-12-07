package com.capgemini.TheaterService.controllers;

import com.capgemini.TheaterService.dto.MicroserviceResponse;
import com.capgemini.TheaterService.dto.ShowsInfo;
import com.capgemini.TheaterService.entities.Theater;
import com.capgemini.TheaterService.services.TheaterService;
import com.capgemini.TheaterService.utils.CSVConverter;
import com.capgemini.TheaterService.utils.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Controller for handling theater related end-points
 *
 * @author Vinay Pratap Singh
 */
@RestController
@RequestMapping("v1/theaters")
public class TheaterController {

    private final TheaterService theaterService;
    private final CSVConverter csvConverter;
    private static final String SUCCESS_MESSAGE = "Operation Completed successfully";


    @Autowired
    public TheaterController(TheaterService theaterService, CSVConverter csvConverter) {
        this.theaterService = theaterService;
        this.csvConverter = csvConverter;
    }

    @GetMapping
    public ResponseEntity<MicroserviceResponse> getAllTheaters() {
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.OK.value(),
          theaterService.getAllTheaters(), null);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<MicroserviceResponse> addTheater(@Valid @RequestBody Theater theater) {
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.CREATED.value(),
          theaterService.addTheater(theater), null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("batch")
    public ResponseEntity<MicroserviceResponse> addMultipleTheaters(@RequestBody List<@NotNull @Valid Theater> theaters) {
        theaterService.addMultipleTheaters(theaters);
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.CREATED.value(), SUCCESS_MESSAGE, null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<MicroserviceResponse> findById(@PathVariable("id") String id) {
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.OK.value(),
          theaterService.findTheaterById(id), null);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<MicroserviceResponse> updateTheater(@PathVariable("id") String id,
                                                              @Valid @RequestBody Theater theater) {
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.OK.value(),
          theaterService.updateTheater(id, theater), null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MicroserviceResponse> removeTheater(@PathVariable("id") String id) {
        theaterService.removeTheater(id);
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.NO_CONTENT.value(), SUCCESS_MESSAGE, null);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<MicroserviceResponse> removeTheseMoviesFromTheseTheaters(@RequestBody Map<String, Set<String>> map) {
        theaterService.removeTheseMoviesFromTheseTheaters(map);
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.NO_CONTENT.value(), SUCCESS_MESSAGE, null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}/{movieId}")
    public ResponseEntity<MicroserviceResponse> validateTheaterAndMovie(@PathVariable("id") String theaterId,
                                                                        @PathVariable("movieId") String movieId) {

        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.OK.value(),
          theaterService.validateTheaterAndMovie(theaterId, movieId), null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}/movies")
    public ResponseEntity<MicroserviceResponse> getMoviesInATheater(@PathVariable("id") String id) {
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.OK.value(),
          theaterService.getMovies(id), null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/movies/{movieId}")
    public ResponseEntity<MicroserviceResponse> addMovieToTheater(@PathVariable("id") @NotNull String id,
                                                                  @PathVariable("movieId") @NotNull String movieId,
                                                                  @Valid @RequestBody ShowsInfo showsInfo) {

        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.CREATED.value(),
          theaterService.addMovieInTheater(id, movieId, showsInfo), null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/movies/{movieId}")
    public ResponseEntity<MicroserviceResponse> removeMovieFromTheater(@PathVariable("id") String theaterId,
                                                                       @PathVariable("movieId") String movieId) {

        theaterService.removeMovieFromTheater(theaterId, movieId);
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.NO_CONTENT.value(), SUCCESS_MESSAGE, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/existence")
    public ResponseEntity<MicroserviceResponse> checkBatchExistence(@RequestBody List<@NotNull String> theaterIds) {
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.OK.value(),
          theaterService.validateBatchExistence(theaterIds), null);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MicroserviceResponse> addTheatersViaFile(@RequestParam("file") MultipartFile file) throws IOException {
        var theaters = csvConverter.csvToTheaters(file.getInputStream());
        theaterService.addMultipleTheaters(theaters);
        MicroserviceResponse response = ResponseBuilder.build(HttpStatus.CREATED.value(), SUCCESS_MESSAGE, null);
        return ResponseEntity.ok(response);
    }

}
