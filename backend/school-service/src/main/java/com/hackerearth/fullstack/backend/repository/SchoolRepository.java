package com.hackerearth.fullstack.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hackerearth.fullstack.backend.model.School;

public interface SchoolRepository extends JpaRepository<School, Long> {

    List<School> findByNameContainingIgnoreCase(String name);

    List<School> findByLocation(String location);
}
