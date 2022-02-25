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

    // path to Add a particular course
    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewGrades(@RequestParam Integer studentId, @RequestParam Integer courseId,
                                                 @RequestParam Integer grade){
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        Grades grades = new Grades();
        grades.setStudentId(studentId);
        grades.setCourseId(courseId);
        grades.setGrade(grade);
        gradesRepository.save(grades);
        return "Saved";
    }

    // alternate /add route to send in json object rather than path parameters
    @PostMapping(path="/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Map ONLY POST Requests
    public @ResponseBody String addNewGradesObject (@RequestBody Grades newGrade){
        Grades grade = gradesRepository.save(newGrade);
        return "Saved";
    }

//    // path to List all courses
//    @GetMapping(path="/list")
//    public @ResponseBody Iterable<Enrollment> getAllEnrollments() {
//        // This returns a JSON or XML with the users
//        return gradesRepository.findAll();
//    }

    @GetMapping(path="/list/course")
    public @ResponseBody Iterable<Grades> getAllGradesForCourse(@RequestParam Integer courseId) {
        // This returns a JSON or XML with the enrollment info for a course
        return gradesRepository.getGradesByCourseId(courseId);
    }

    @GetMapping(path="/list/student")
    public @ResponseBody Iterable<Grades> getAllGradesForStudent(@RequestParam Integer studentId) {
        // This returns a JSON or XML with the enrollment info for a student
        return gradesRepository.getGradesByStudentId(studentId);
    }

    // path to View one course based on ID
    @GetMapping(path="/view/{id}")
    public @ResponseBody Grades getGrades(@PathVariable Integer id) {
        // This returns a JSON or XML with the users
        return gradesRepository.findGradesByGid(id);
    }

    @PutMapping(path="/modify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Grades modifyGrades(@RequestBody Grades modifiedGrades){
        Grades grades = gradesRepository.findGradesByGid(modifiedGrades.getGid());

        grades.setCourseId(modifiedGrades.getCourseId());
        grades.setStudentId(modifiedGrades.getStudentId());
        grades.setGrade(modifiedGrades.getGrade());

        final Grades updateGrades = gradesRepository.save(grades);

        return updateGrades;
    }

    @DeleteMapping(path="/delete")
    public @ResponseBody String deleteGrades(@RequestBody Grades gradesToDelete){
        Grades grades = gradesRepository.findGradesByGid(gradesToDelete.getGid());
        gradesRepository.delete(grades);
        return "Grades: " + grades.getGid() + " has been deleted.";
    }
}
