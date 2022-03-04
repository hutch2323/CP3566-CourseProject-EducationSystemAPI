package com.example.courseproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path="/enrollment") // This means URL's start with /demo (after Application path)
public class EnrollmentController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;

    // path to Add a particular enrollment
    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewEnrollment(@RequestParam Integer studentId, @RequestParam Integer courseId){
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        Student student = studentRepository.findStudentByStudentId(studentId);
        if (student == null){
            return "Student ID: " + studentId + " does not exist.";
        }

        Course course = courseRepository.findCourseByCourseId(courseId);
        if (course == null){
            return "Course ID: " + courseId + " does not exist.";
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollmentRepository.save(enrollment);
        return "Student ID: " + enrollment.getStudentId() + " has been successfully enrolled in Course ID: " +
                enrollment.getCourseId();
    }

    // alternate /add route to send in json object rather than path parameters
    @PostMapping(path="/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Map ONLY POST Requests
    public @ResponseBody String addNewEnrollmentObject (@RequestBody Enrollment newEnrollment){
        Student student = studentRepository.findStudentByStudentId(newEnrollment.getStudentId());
        if (student == null){
            return "Student ID: " + newEnrollment.getStudentId() + " does not exist.";
        }

        Course course = courseRepository.findCourseByCourseId(newEnrollment.getCourseId());
        if (course == null){
            return "Course ID: " + newEnrollment.getCourseId() + " does not exist.";
        }

        Enrollment enrollment = enrollmentRepository.save(newEnrollment);
        return "Student ID: " + enrollment.getStudentId() + " has been successfully enrolled in Course ID: " +
                enrollment.getCourseId();
    }

    // path to list all enrollments by courseId
    @GetMapping(path="/list/course")
    public @ResponseBody Iterable<Enrollment> getAllEnrollmentsForCourse(@RequestParam Integer courseId) {
        // This returns a JSON or XML with the enrollment info for a course
        return enrollmentRepository.getEnrollmentByCourseId(courseId);
    }

    // path to list all enrollments by studentId
    @GetMapping(path="/list/student")
    public @ResponseBody Iterable<Enrollment> getAllEnrollmentsForStudent(@RequestParam Integer studentId) {
        // This returns a JSON or XML with the enrollment info for a student
        return enrollmentRepository.getEnrollmentByStudentId(studentId);
    }

    // path to View an enrollment based on Id
    @GetMapping(path="/view/{id}")
    public @ResponseBody Enrollment getEnrollment(@PathVariable Integer id) {
        // This returns a JSON or XML with the enrollments
        return enrollmentRepository.findEnrollmentByEid(id);
    }

    // path to Modify an enrollment
    @PutMapping(path="/modify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String modifyEnrollment(@RequestBody Enrollment modifiedEnrollment){
        Enrollment enrollment = enrollmentRepository.findEnrollmentByEid(modifiedEnrollment.getEid());

        if (enrollment == null){
            return "Enrollment ID: " + modifiedEnrollment.getEid() + " does not exist.";
        }

        Student student = studentRepository.findStudentByStudentId(modifiedEnrollment.getStudentId());
        if (student == null){
            return "Student ID: " + modifiedEnrollment.getStudentId() + " does not exist.";
        }

        Course course = courseRepository.findCourseByCourseId(modifiedEnrollment.getCourseId());
        if (course == null){
            return "Course ID: " + modifiedEnrollment.getCourseId() + " does not exist.";
        }

        enrollment.setCourseId(modifiedEnrollment.getCourseId());
        enrollment.setStudentId(modifiedEnrollment.getStudentId());

        final Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        return "Enrollment ID: " + updatedEnrollment.getEid() + " has been modified.";
    }

    // path to Delete an enrollment
    @DeleteMapping(path="/delete")
    public @ResponseBody String deleteEnrollment(@RequestParam Integer eid){
        Enrollment enrollment = enrollmentRepository.findEnrollmentByEid(eid);
        if (enrollment == null){
            return "Enrollment ID: " + eid + " does not exist.";
        }
        enrollmentRepository.delete(enrollment);
        return "Student: " + enrollment.getStudentId() + " is no longer enrolled in Course: " + enrollment.getCourseId();
    }
}