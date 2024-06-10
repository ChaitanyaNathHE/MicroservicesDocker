package com.hackerearth.fullstack.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackerearth.fullstack.backend.model.School;
import com.hackerearth.fullstack.backend.repository.SchoolRepository;

@Service
public class SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

    public School createSchool(School school) {
        return schoolRepository.save(school);
    }

    public List<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    public School getSchoolById(Long id) {
        return schoolRepository.findById(id).orElse(null);
    }

    public School updateSchool(Long id, School school) {
        School existingSchool = getSchoolById(id);
        if (existingSchool != null) {
            existingSchool.setName(school.getName());
            existingSchool.setLocation(school.getLocation());
            return schoolRepository.save(existingSchool);
        }
        return null;
    }

    public void deleteSchool(Long id) {
        schoolRepository.deleteById(id);
    }

    public List<School> getSchoolsByLocation(String location) {
        return schoolRepository.findByLocation(location);
    }

    public List<School> searchSchoolsByName(String name) {
        return schoolRepository.findByNameContainingIgnoreCase(name);
    }

}
