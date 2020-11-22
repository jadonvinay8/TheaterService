package com.capgemini.TheaterService.utils;

import com.capgemini.TheaterService.beans.Address;
import com.capgemini.TheaterService.entities.Theater;
import com.capgemini.TheaterService.exceptions.CityNotFoundException;
import com.capgemini.TheaterService.services.TheaterService;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CSVConverter {

    private final TheaterService service;
    private static final String INVALID_KEY = "Invalid";
    private static final int LENGTH_OF_EMPTY_LIST = 2;

    public CSVConverter(TheaterService service) {
        this.service = service;
    }

    public List<Theater> csvToTheaters(InputStream stream) {

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
               CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Theater> theaters = new ArrayList<>();
            List<CSVRecord> records = csvParser.getRecords();
            List<String> cityIds = new ArrayList<>();

            records.forEach(record -> {
                var cityId = record.get("cityId");
                if (!cityIds.contains(cityId))
                    cityIds.add(cityId);
            });

            Map<String, String> citiesByIds = service.getCitiesByIds(cityIds);
            if (citiesByIds.get(INVALID_KEY).length() > LENGTH_OF_EMPTY_LIST) {
                throw new CityNotFoundException("These IDs are invalid: " + citiesByIds.get(INVALID_KEY));
            }

            records.forEach(record -> {
                var theaterName = record.get("theaterName");

                var state = record.get("state");
                var area = record.get("area");
                var pinCode = record.get("pinCode");
                var cityId = record.get("cityId");
                var cityName = citiesByIds.get(cityId);
                var address = new Address(cityName, state, area, pinCode);

                var rating = Double.parseDouble(record.get("rating"));
                var theater = new Theater(theaterName, address, cityId, rating, new ArrayList<>());

                theaters.add(theater);
            });

            return theaters;

        } catch (IOException e) {
            throw new RuntimeException("Can't parse CSV");
        }
    }

}
