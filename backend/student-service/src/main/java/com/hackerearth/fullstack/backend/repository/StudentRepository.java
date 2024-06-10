package com.hackerearth.fullstack.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hackerearth.fullstack.backend.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findBySchoolId(Long schoolId);

    List<Student> findByFirstname(String name);
}