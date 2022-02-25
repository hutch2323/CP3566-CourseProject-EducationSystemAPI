package com.example.courseproject;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface EnrollmentRepository extends CrudRepository<Enrollment, Integer> {
    Enrollment findEnrollmentByEid(Integer eid);
    Iterable<Enrollment> getEnrollmentByCourseId(Integer courseId);
    Iterable<Enrollment> getEnrollmentByStudentId(Integer studentId);
}
