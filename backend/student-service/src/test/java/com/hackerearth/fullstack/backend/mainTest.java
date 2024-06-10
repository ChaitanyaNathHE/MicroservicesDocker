package com.hackerearth.fullstack.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.hackerearth.fullstack.backend.dto.SchoolDTO;
import com.hackerearth.fullstack.backend.model.Student;
import com.hackerearth.fullstack.backend.repository.StudentRepository;
import com.hackerearth.fullstack.backend.service.StudentService;

@SpringBootTest
class mainTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enrollStudent_ValidStudent_ReturnsSavedStudent() throws ParseException {
        // Arrange
        String dateStr = "2002-01-20";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);
        Student student = new Student("John", "Doe", "john.doe@example.com", date, 1L);
        SchoolDTO schoolDTO = new SchoolDTO(1L, "School A", "Bangalore");

        when(restTemplate.getForObject(anyString(), eq(SchoolDTO.class))).thenReturn(schoolDTO);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Act
        Student result = studentService.enrollStudent(student);

        // Assert
        assertNotNull(result);
        assertEquals(student.getFirstname(), result.getFirstname());
        assertEquals(student.getLastname(), result.getLastname());
        assertEquals(student.getEmail(), result.getEmail());
        assertEquals(student.getDob(), result.getDob());
        assertEquals(student.getSchoolId(), result.getSchoolId());

        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void enrollStudent_InvalidSchool_ThrowsIllegalArgumentException() throws ParseException {
        // Arrange
        String dateStr = "1990-01-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);
        Student student = new Student("John", "Doe", "john.doe@example.com", date, 1L);
        when(restTemplate.getForObject(anyString(), eq(SchoolDTO.class))).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> studentService.enrollStudent(student));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void getAllStudents_ReturnsListOfStudents() throws ParseException {
        // Arrange
        String dateStr1 = "1990-01-01";
        String dateStr2 = "1992-05-15";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(dateStr1);
        Date date2 = sdf.parse(dateStr2);
        List<Student> studentList = List.of(
                new Student("John", "Doe", "john.doe@example.com", date1, 1L),
                new Student("Jane", "Doe", "jane.doe@example.com", date2, 2L));

        when(studentRepository.findAll()).thenReturn(studentList);

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertEquals(studentList.size(), result.size());
        assertEquals(studentList, result);
    }

        @Test
        void updateStudent_ValidStudent_ReturnsUpdatedStudent() throws ParseException {
            // Arrange
            String dateStr1 = "1990-01-01";
            String dateStr2 = "1992-05-15";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(dateStr1);
            Date date2 = sdf.parse(dateStr2);
            Student existingStudent = new Student("John", "Doe", "john.doe@example.com", date1, 1L);
            Student updatedStudent = new Student("UpdatedJohn", "UpdatedDoe", "updated.john@example.com",
                    date2, 2L);
            SchoolDTO schoolDTO = new SchoolDTO(2L, "UpdatedSchool", "Delhi");

            when(studentRepository.findById(existingStudent.getId())).thenReturn(Optional.of(existingStudent));
            when(restTemplate.getForObject(anyString(), eq(SchoolDTO.class))).thenReturn(schoolDTO);
            when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

            // Act
            Student result = studentService.updateStudent(existingStudent.getId(), updatedStudent);

            // Assert
            assertNotNull(result);
            assertEquals(updatedStudent.getFirstname(), result.getFirstname());
            assertEquals(updatedStudent.getLastname(), result.getLastname());
            assertEquals(updatedStudent.getEmail(), result.getEmail());
            assertEquals(updatedStudent.getDob(), result.getDob());
            assertEquals(updatedStudent.getSchoolId(), result.getSchoolId());

            verify(studentRepository, times(1)).save(any(Student.class));
        }

    @Test
    void updateStudent_InvalidStudent_ReturnsNull() throws ParseException {
        // Arrange
        String dateStr = "1990-01-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);
        Student updatedStudent = new Student("UpdatedJohn", "UpdatedDoe", "updated.john@example.com",
                date, 2L);

        when(studentRepository.findById(updatedStudent.getId())).thenReturn(Optional.empty());

        // Act
        Student result = studentService.updateStudent(updatedStudent.getId(), updatedStudent);

        // Assert
        assertNull(result);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void deleteStudent_ValidStudentId_DeletesStudent() {
        // Arrange
        Long studentId = 1L;

        // Act
        studentService.deleteStudent(studentId);

        // Assert
        verify(studentRepository, times(1)).deleteById(studentId);
    }

    @Test
    void searchStudentsByName_ValidName_ReturnsListOfStudents() throws ParseException {
        // Arrange
        String dateStr1 = "1990-01-01";
        String dateStr2 = "1992-05-15";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(dateStr1);
        Date date2 = sdf.parse(dateStr2);
        String searchName = "John";
        List<Student> studentList = List.of(
                new Student("John", "Doe", "john.doe@example.com", date1, 1L),
                new Student("Johnny", "Walker", "johnny.walker@example.com", date2, 2L));

        when(studentRepository.findByFirstname(searchName)).thenReturn(studentList);

        // Act
        List<Student> result = studentService.searchStudentsByName(searchName);

        // Assert
        assertEquals(studentList.size(), result.size());
        assertEquals(studentList, result);
    }

    @Test
    void getStudentsBySchool_ValidSchoolId_ReturnsListOfStudents() throws ParseException {
        // Arrange
        String dateStr1 = "1990-01-01";
        String dateStr2 = "1992-05-15";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(dateStr1);
        Date date2 = sdf.parse(dateStr2);
        Long schoolId = 1L;
        List<Student> studentList = List.of(
                new Student("John", "Doe", "john.doe@example.com", date1, schoolId),
                new Student("Jane", "Doe", "jane.doe@example.com", date2, schoolId));

        SchoolDTO schoolDTO = new SchoolDTO(schoolId, "School A", "Mumbai");

        when(restTemplate.getForObject(anyString(), eq(SchoolDTO.class))).thenReturn(schoolDTO);
        when(studentRepository.findBySchoolId(schoolId)).thenReturn(studentList);

        // Act
        List<Student> result = studentService.getStudentsBySchool(schoolId);

        // Assert
        assertEquals(studentList.size(), result.size());
        assertEquals(studentList, result);
    }
}