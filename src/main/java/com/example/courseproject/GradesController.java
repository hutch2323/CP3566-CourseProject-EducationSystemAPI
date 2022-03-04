package com.example.courseproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // This means that this class is a Controller
@RequestMapping(path="/grades") // This means URL's start with /demo (after Application path)
public class GradesController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private GradesRepository gradesRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;

    // path to Add a particular grade
    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewGrades(@RequestParam Integer studentId, @RequestParam Integer courseId,
                                                 @RequestParam Integer grade){
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

        Grades grades = new Grades();
        grades.setStudentId(studentId);
        grades.setCourseId(courseId);
        grades.setGrade(grade);
        gradesRepository.save(grades);
        return "Grade for Student ID: " + grades.getStudentId() + " of " + grades.getGrade() + " in Course ID: " +
                grades.getCourseId() + " has been added.";
    }

    // alternate /add route to send in json object rather than path parameters
    @PostMapping(path="/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Map ONLY POST Requests
    public @ResponseBody String addNewGradesObject (@RequestBody Grades newGrade){
        Student student = studentRepository.findStudentByStudentId(newGrade.getStudentId());
        if (student == null){
            return "Student ID: " + newGrade.getStudentId() + " does not exist.";
        }

        Course course = courseRepository.findCourseByCourseId(newGrade.getCourseId());
        if (course == null){
            return "Course ID: " + newGrade.getCourseId() + " does not exist.";
        }

        Grades grade = gradesRepository.save(newGrade);
        return "Grade for Student ID: " + grade.getStudentId() + " of " + grade.getGrade() + " in Course ID: " +
                grade.getCourseId() + " has been added.";
    }

    // path to view all grades based on a particular course ID
    @GetMapping(path="/list/course")
    public @ResponseBody Iterable<Grades> getAllGradesForCourse(@RequestParam Integer courseId) {
        // This returns a JSON or XML with the enrollment info for a course
        return gradesRepository.getGradesByCourseId(courseId);
    }

    // path to view all grades based on a particular student ID
    @GetMapping(path="/list/student")
    public @ResponseBody Iterable<Grades> getAllGradesForStudent(@RequestParam Integer studentId) {
        // This returns a JSON or XML with the enrollment info for a student
        return gradesRepository.getGradesByStudentId(studentId);
    }

    // path to View one grade based on ID
    @GetMapping(path="/view/{id}")
    public @ResponseBody Grades getGrades(@PathVariable Integer id) {
        // This returns a JSON or XML with the users
        return gradesRepository.findGradesByGid(id);
    }

    // path to Modify a particular grade
    @PutMapping(path="/modify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String modifyGrades(@RequestBody Grades modifiedGrades){
        Grades grades = gradesRepository.findGradesByGid(modifiedGrades.getGid());
        if (grades == null){
            return "Grade ID: " + modifiedGrades.getGid() + " does not exist.";
        }

        Student student = studentRepository.findStudentByStudentId(modifiedGrades.getStudentId());
        if (student == null){
            return "Student ID: " + modifiedGrades.getStudentId() + " does not exist.";
        }

        Course course = courseRepository.findCourseByCourseId(modifiedGrades.getCourseId());
        if (course == null){
            return "Course ID: " + modifiedGrades.getCourseId() + " does not exist.";
        }

        grades.setCourseId(modifiedGrades.getCourseId());
        grades.setStudentId(modifiedGrades.getStudentId());
        grades.setGrade(modifiedGrades.getGrade());

        final Grades updateGrades = gradesRepository.save(grades);

        return "Grade ID: " + updateGrades.getGid() + " has been modified.";
    }

    // path to Delete a particular grade

    @DeleteMapping(path="/delete")
    public @ResponseBody String deleteGrades(@RequestParam Integer gid){
        Grades grades = gradesRepository.findGradesByGid(gid);
        if (grades == null){
            return "Grade ID: " + gid + " does not exist.";
        }
        gradesRepository.delete(grades);
        return "Grades ID: " + grades.getGid() + " has been deleted.";
    }
}
