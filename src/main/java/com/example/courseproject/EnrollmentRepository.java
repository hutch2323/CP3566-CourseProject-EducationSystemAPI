package com.example.courseproject;

import org.springframework.data.repository.CrudRepository;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface EnrollmentRepository extends CrudRepository<Enrollment, Integer> {
    Enrollment findEnrollmentByEid(Integer eid);
    Iterable<Enrollment> findAllByCourseId(Integer courseId);
    Iterable<Enrollment> findAllByStudentId(Integer studentId);
}
