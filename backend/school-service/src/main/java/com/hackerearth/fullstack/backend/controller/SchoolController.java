package com.hackerearth.fullstack.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hackerearth.fullstack.backend.model.School;
import com.hackerearth.fullstack.backend.service.SchoolService;

@RestController
@RequestMapping("/schools")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    @PostMapping
    public ResponseEntity<School> createSchool(@RequestBody School school) {
        School createdSchool = schoolService.createSchool(school);
        return new ResponseEntity<>(createdSchool, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<School>> getAllSchools() {
        List<School> schools = schoolService.getAllSchools();
        return new ResponseEntity<>(schools, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<School> getSchoolById(@PathVariable Long id) {
        School school = schoolService.getSchoolById(id);
        return new ResponseEntity<>(school, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<School> updateSchool(@PathVariable Long id, @RequestBody School school) {
        School updatedSchool = schoolService.updateSchool(id, school);
        return new ResponseEntity<>(updatedSchool, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
        schoolService.deleteSchool(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/by-location/{location}")
    public List<School> getSchoolsByLocation(@PathVariable String location) {
        return schoolService.getSchoolsByLocation(location);
    }

    @GetMapping("/search")
    public List<School> searchSchoolsByName(@RequestParam String name) {
        return schoolService.searchSchoolsByName(name);
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> deleteAllSchools() {
        schoolService.deleteAllSchools();
        return ResponseEntity.noContent().build();
    }

}
