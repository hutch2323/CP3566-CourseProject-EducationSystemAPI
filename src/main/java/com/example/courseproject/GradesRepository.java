package com.example.courseproject;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface GradesRepository extends CrudRepository<Grades, Integer> {
    Grades findGradesByGid(Integer gid);
    List<Grades> getGradesByCourseId(Integer courseId);
    List<Grades> getGradesByStudentId(Integer studentId);
}
