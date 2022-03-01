package com.example.courseproject;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface CourseRepository extends CrudRepository<Course, Integer> {
    Course findCourseByCourseId(Integer courseId);
    List<Course> getCourseByPid(Integer pid);
}

