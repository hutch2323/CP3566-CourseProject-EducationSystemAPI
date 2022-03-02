package com.example.courseproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path="/program") // This means URL's start with /demo (after Application path)
public class ProgramsController {
    @Autowired
    private ProgramsRepository programsRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private GradesRepository gradesRepository;

    // path to Add a particular program
    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewProgram (@RequestParam String programName, @RequestParam String campus){
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        Programs program = new Programs();
        program.setProgramName(programName);
        program.setCampus(campus);
        programsRepository.save(program);

        return program.getProgramName() + " has been added.";
    }

    // alternate /add route to send in json object rather than path parameters
    @PostMapping(path="/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Map ONLY POST Requests
    public @ResponseBody String addNewProgramsObject (@RequestBody Programs newProgram){
        Programs program = programsRepository.save(newProgram);
        return program.getProgramName() + " has been added.";
    }

    // path to List all programs
    @GetMapping(path="/list")
    public @ResponseBody Iterable<Programs> getAllPrograms() {
        // This returns a JSON or XML with the users
        return programsRepository.findAll();
    }

    // path to View one program based on ID
    @GetMapping(path="/view/{id}")
    public @ResponseBody Programs getProgram(@PathVariable Integer id) {
        // This returns a JSON or XML with the users
        return programsRepository.findProgramsByPid(id);
    }

    // path to Modify a program
    @PutMapping(path="/modify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String modifyProgram(@RequestBody Programs modifiedProgram){
        Programs program = programsRepository.findProgramsByPid(modifiedProgram.getPid());

        if (program == null){
            return "Program pid: " + modifiedProgram.getPid() + " does not exist.";
        }

        program.setProgramName(modifiedProgram.getProgramName());
        program.setCampus(modifiedProgram.getCampus());

        final Programs updatedProgram = programsRepository.save(program);

        return "Program pid: " + updatedProgram.getPid() + " has been modified.";
    }

    // path to Delete a program
    @DeleteMapping(path="/delete")
    public @ResponseBody String deleteProgram(@RequestParam Integer pid){
        Programs program = programsRepository.findProgramsByPid(pid);

        if (program == null){
            return "Program pid: " + pid + " does not exist.";
        }

        System.out.println("ProgramId: " + program.getPid());
        List<Course> coursesToRemove = courseRepository.getCourseByPid(pid);
        System.out.println("Course ID: " + coursesToRemove);

        for (Course course : coursesToRemove){
            System.out.println(course.getCourseId());
            List<Enrollment> enrollmentsToRemove = enrollmentRepository.getEnrollmentByCourseId(course.getCourseId());
            List<Grades> gradesToRemove = gradesRepository.getGradesByCourseId(course.getCourseId());

            for (Enrollment enrollment : enrollmentsToRemove){
                System.out.println("Enrollment ID: " + enrollment.getEid());
                enrollmentRepository.delete(enrollment);
            }

            for (Grades grade : gradesToRemove){
                System.out.println("Grade ID: " + grade.getGid());
                gradesRepository.delete(grade);
            }

            courseRepository.delete(course);
        }

        programsRepository.delete(program);
        return "Program pid: " + pid + " has been successfully deleted.";
    }
}