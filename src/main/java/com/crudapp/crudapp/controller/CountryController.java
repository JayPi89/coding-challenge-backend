package com.crudapp.crudapp.controller;

import com.crudapp.crudapp.model.Country;
import com.crudapp.crudapp.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CountryController {

    private final CountryRepository countryRepository;

    @GetMapping("/countries")
    public ResponseEntity<List<Country>> getCountry() {
        return new ResponseEntity<List<Country>>(countryRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/countries/{name}")
    public ResponseEntity<List<Country>> getCountry(@PathVariable String name) {
        return new ResponseEntity<List<Country>>(countryRepository.findByNameIgnoreCase(name), HttpStatus.OK);
    }

    @PostMapping("/countries")
    public ResponseEntity<Country> createTutorial(@RequestBody Country country) {
        try {
            Country _country = countryRepository.save(new Country(country.getName(), country.getCountrycode()));
            return new ResponseEntity<>(_country, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/countries/{id}")
    public ResponseEntity<Country> updateTutorial(@PathVariable("id") long id, @RequestBody Country country) {
        Optional<Country> tutorialData = countryRepository.findById(id);

        if (tutorialData.isPresent()) {
            Country _country = tutorialData.get();
            _country.setName(country.getName());
            _country.setCountrycode(country.getCountrycode());
            return new ResponseEntity<>(countryRepository.save(_country), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/countries/{id}")
    public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
        try {
            countryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> errorHandler(MethodArgumentNotValidException e) {
        return new ResponseEntity<String>("Sorry, there seems to be a problem with your request", HttpStatus.BAD_REQUEST);
    }

}
