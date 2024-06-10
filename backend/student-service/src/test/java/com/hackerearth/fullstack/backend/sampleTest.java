package com.hackerearth.fullstack.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
class sampleTest {

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
}