package com.example.courseproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // This means that this class is a Controller
@RequestMapping(path="/course") // This means URL's start with /demo (after Application path)
public class CourseController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private CourseRepository courseRepository;


    // path to Add a particular course
    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewCourse (@RequestParam String courseName, @RequestParam String courseNumber,
                                               @RequestParam Integer capacity, @RequestParam String year,
                                               @RequestParam String semester, @RequestParam Integer pid ){
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        Course course = new Course();
        course.setCourseName(courseName);
        course.setCourseNumber(courseNumber);
        course.setCapacity(capacity);
        course.setYear(year);
        course.setSemester(semester);
        course.setPid(pid);
        courseRepository.save(course);
        return "Saved";
    }

    // alternate /add route to send in json object rather than path parameters
    @PostMapping(path="/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Map ONLY POST Requests
    public @ResponseBody String addNewCourseObject (@RequestBody Course newCourse){
        Course course = courseRepository.save(newCourse);
        return "Saved";
    }

    // path to List all courses
    @GetMapping(path="/list")
    public @ResponseBody Iterable<Course> getAllCourses() {
        // This returns a JSON or XML with the users
        return courseRepository.findAll();
    }

    // path to List all courses by Program ID
    @GetMapping(path="/list/program")
    public @ResponseBody Iterable<Course> getAllCoursesByProgramId(@RequestParam Integer pid) {
        // This returns a JSON or XML with the users
        return courseRepository.getCourseByPid(pid);
    }

    // path to View one course based on ID
    @GetMapping(path="/view/{id}")
    public @ResponseBody Course getCourse(@PathVariable Integer id) {
        // This returns a JSON or XML with the users
        return courseRepository.findCourseByCourseId(id);
    }

    @PutMapping(path="/modify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Course modifyCourse(@RequestBody Course modifiedCourse){
        Course course = courseRepository.findCourseByCourseId(modifiedCourse.getCourseId());

        course.setCourseName(modifiedCourse.getCourseName());
        course.setCourseNumber(modifiedCourse.getCourseNumber());
        course.setCapacity(modifiedCourse.getCapacity());
        course.setYear(modifiedCourse.getYear());
        course.setSemester(modifiedCourse.getSemester());
        course.setPid(modifiedCourse.getPid());

        final Course updatedCourse = courseRepository.save(course);

        return updatedCourse;
    }

    @DeleteMapping(path="/delete")
    public @ResponseBody String deleteCourse(@RequestParam Integer courseId){
        Course course = courseRepository.findCourseByCourseId(courseId);
        courseRepository.delete(course);
        return course.getCourseNumber() + ": " + course.getCourseName() + " has been deleted!";
    }
}