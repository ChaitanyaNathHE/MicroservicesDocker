package com.hackerearth.fullstack.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.hackerearth.fullstack.backend.model.School;
import com.hackerearth.fullstack.backend.repository.SchoolRepository;
import com.hackerearth.fullstack.backend.service.SchoolService;

@SpringBootTest
class sampleTest {

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private SchoolService schoolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSchool_ValidSchool_ReturnsSavedSchool() {
        // Arrange
        School school = new School("School A", "Location A");
        when(schoolRepository.save(any(School.class))).thenReturn(school);

        // Act
        School result = schoolService.createSchool(school);

        // Assert
        assertNotNull(result);
        assertEquals(school.getName(), result.getName());
        assertEquals(school.getLocation(), result.getLocation());

        verify(schoolRepository, times(1)).save(any(School.class));
    }

    @Test
    void getAllSchools_ReturnsListOfSchools() {
        // Arrange
        List<School> schoolList = List.of(
                new School("School A", "Location A"),
                new School("School B", "Location B"));

        when(schoolRepository.findAll()).thenReturn(schoolList);

        // Act
        List<School> result = schoolService.getAllSchools();

        // Assert
        assertEquals(schoolList.size(), result.size());
        assertEquals(schoolList, result);
    }
}
