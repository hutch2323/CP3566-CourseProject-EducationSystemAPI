package com.example.courseproject;

import org.springframework.data.repository.CrudRepository;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface GradesRepository extends CrudRepository<Grades, Integer> {
    Grades findGradesByGid(Integer gid);
    Iterable<Grades> findAllByCourseId(Integer courseId);
    Iterable<Grades> findAllByStudentId(Integer studentId);
}
