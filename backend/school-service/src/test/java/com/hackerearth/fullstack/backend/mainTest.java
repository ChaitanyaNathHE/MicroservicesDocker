package com.hackerearth.fullstack.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

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
class mainTest {

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

    @Test
    void getSchoolById_SchoolExists_ReturnsOptionalSchool() {
        // Arrange
        School school = new School("School A", "Location A");

        when(schoolRepository.findById(school.getId())).thenReturn(Optional.of(school));

        // Act
        School result = schoolService.getSchoolById(school.getId());

        // Assert
        assertNotNull(result);
        assertEquals(school, result);
    }

    @Test
    void getSchoolById_SchoolDoesNotExist_ReturnsNull() {
        // Arrange
        Long schoolId = -999L;

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.empty());

        // Act
        School result = schoolService.getSchoolById(schoolId);

        // Assert
        assertNull(result);
    }

    @Test
    void updateSchool_ValidSchool_ReturnsUpdatedSchool() {
        // Arrange
        School existingSchool = new School("School A", "Location A");
        School updatedSchool = new School("UpdatedSchool", "UpdatedLocation");

        when(schoolRepository.findById(existingSchool.getId())).thenReturn(Optional.of(existingSchool));
        when(schoolRepository.save(any(School.class))).thenReturn(updatedSchool);

        // Act
        School result = schoolService.updateSchool(existingSchool.getId(), updatedSchool);

        // Assert
        assertNotNull(result);
        assertEquals(updatedSchool.getName(), result.getName());
        assertEquals(updatedSchool.getLocation(), result.getLocation());

        verify(schoolRepository, times(1)).save(any(School.class));
    }

    @Test
    void updateSchool_InvalidSchool_ReturnsNull() {
        // Arrange
        Long schoolId = -999L;
        School updatedSchool = new School("UpdatedSchool", "UpdatedLocation");

        when(schoolRepository.findById(updatedSchool.getId())).thenReturn(Optional.empty());

        // Act
        School result = schoolService.updateSchool(schoolId, updatedSchool);

        // Assert
        assertNull(result);
        verify(schoolRepository, never()).save(any(School.class));
    }

    @Test
    void deleteSchool_ValidSchoolId_DeletesSchool() {
        // Arrange
        Long schoolId = 1L;

        // Act
        schoolService.deleteSchool(schoolId);

        // Assert
        verify(schoolRepository, times(1)).deleteById(schoolId);
    }

    @Test
    void getSchoolsByLocation_ValidLocation_ReturnsListOfSchools() {
        // Arrange
        String location = "Location A";
        List<School> schoolList = List.of(
                new School("School A", location),
                new School("School B", location));

        when(schoolRepository.findByLocation(location)).thenReturn(schoolList);

        // Act
        List<School> result = schoolService.getSchoolsByLocation(location);

        // Assert
        assertEquals(schoolList.size(), result.size());
        assertEquals(schoolList, result);
    }

    @Test
    void searchSchoolsByName_ValidName_ReturnsListOfSchools() {
        // Arrange
        String searchName = "School";
        List<School> schoolList = List.of(
                new School("School A", "Location A"),
                new School("School B", "Location B"));

        when(schoolRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(schoolList);

        // Act
        List<School> result = schoolService.searchSchoolsByName(searchName);

        // Assert
        assertEquals(schoolList.size(), result.size());
        assertEquals(schoolList, result);
    }
}
