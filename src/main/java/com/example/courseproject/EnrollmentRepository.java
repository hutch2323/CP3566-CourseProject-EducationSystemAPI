package com.example.courseproject;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface EnrollmentRepository extends CrudRepository<Enrollment, Integer> {
    Enrollment findEnrollmentByEid(Integer eid);
    List<Enrollment> getEnrollmentByCourseId(Integer courseId);
    List<Enrollment> getEnrollmentByStudentId(Integer studentId);
}
