package com.hackerearth.fullstack.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hackerearth.fullstack.backend.dto.SchoolDTO;
import com.hackerearth.fullstack.backend.model.Student;
import com.hackerearth.fullstack.backend.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String SCHOOL_SERVICE_URL = "http://localhost:8001/schools/";

    public Student enrollStudent(Student student) {
        String schoolApiUrl = SCHOOL_SERVICE_URL + student.getSchoolId();
        SchoolDTO schoolDTO = restTemplate.getForObject(schoolApiUrl, SchoolDTO.class);

        if (schoolDTO == null) {
            throw new IllegalArgumentException("School with ID " + student.getSchoolId() + " not found.");
        } else {
            return studentRepository.save(student);
        }

    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Long id, Student student) {
        Student existingStudent = getStudentById(id);
        if (existingStudent != null) {
            existingStudent.setFirstname(student.getFirstname());
            existingStudent.setLastname(student.getLastname());
            existingStudent.setEmail(student.getEmail());
            existingStudent.setDob(student.getDob());
            String schoolApiUrl = SCHOOL_SERVICE_URL + student.getSchoolId();
            SchoolDTO schoolDTO = restTemplate.getForObject(schoolApiUrl, SchoolDTO.class);
            if (schoolDTO == null) {
                throw new IllegalArgumentException("School with ID " + student.getSchoolId() + " not found.");
            } else {
                existingStudent.setSchoolId(student.getSchoolId());
            }
            return studentRepository.save(existingStudent);
        }
        return null;
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> searchStudentsByName(String name) {
        return studentRepository.findByFirstname(name);
    }

    public List<Student> getStudentsBySchool(Long schoolId) {
        String schoolApiUrl = SCHOOL_SERVICE_URL + schoolId;
        SchoolDTO schoolDTO = restTemplate.getForObject(schoolApiUrl, SchoolDTO.class);

        if (schoolDTO == null) {
            throw new IllegalArgumentException("School with ID " + schoolId + " not found.");
        } else {
            return studentRepository.findBySchoolId(schoolId);
        }
    }
}
