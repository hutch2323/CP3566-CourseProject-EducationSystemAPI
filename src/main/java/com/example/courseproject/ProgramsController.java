package com.example.courseproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


    // path to Add a particular course
    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewProgram (@RequestParam String programName, @RequestParam String campus){
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        Programs program = new Programs();
        program.setProgramName(programName);
        program.setCampus(campus);
        programsRepository.save(program);
        return "Saved";
    }

    // alternate /add route to send in json object rather than path parameters
    @PostMapping(path="/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Map ONLY POST Requests
    public @ResponseBody String addNewProgramsObject (@RequestBody Programs newProgram){
        Programs program = programsRepository.save(newProgram);
        return "Saved";
    }

    // path to List all courses
    @GetMapping(path="/list")
    public @ResponseBody Iterable<Programs> getAllPrograms() {
        // This returns a JSON or XML with the users
        return programsRepository.findAll();
    }

    // path to View one course based on ID
    @GetMapping(path="/view/{id}")
    public @ResponseBody Programs getProgram(@PathVariable Integer id) {
        // This returns a JSON or XML with the users
        return programsRepository.findProgramsByPid(id);
    }

    @PutMapping(path="/modify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Programs modifyProgram(@RequestBody Programs modifiedProgram){
        Programs program = programsRepository.findProgramsByPid(modifiedProgram.getPid());

        program.setProgramName(modifiedProgram.getProgramName());
        program.setCampus(modifiedProgram.getCampus());

        final Programs updatedProgram = programsRepository.save(program);

        return updatedProgram;
    }

    @DeleteMapping(path="/delete")
    public @ResponseBody String deleteProgram(@RequestParam Integer pid){
        Programs program = programsRepository.findProgramsByPid(pid);
        System.out.println("ProgramId: " + program.getPid());
        Iterable<Course> coursesToRemove = courseRepository.getCourseByPid(pid);
        System.out.println(coursesToRemove);

        for (Course course : coursesToRemove){
            System.out.println(course.getCourseId());
        }
//        try {
//            Course course = null;
//            while ((course = coursesToRemove.iterator().next()) != null) {
//
//                System.out.println("CourseId: " + course.getCourseId());
//            }
//        } catch (Exception e) {
//
//        }
//                    Iterable<Enrollment> enrollmentsToRemove = enrollmentRepository.getEnrollmentByCourseId(course.getCourseId());
//                    try {
//                        Enrollment enrollment = null;
//                        while ((enrollment = enrollmentsToRemove.iterator().next()) != null) {
//                            System.out.println("EnrollmentId: " + enrollment.getEid());
//                            enrollmentRepository.delete(enrollment);
//                        }
//                    } catch (Exception e){
//                        System.out.println(e);
//                    }
//
//                    Iterable<Grades> gradesToRemove = gradesRepository.getGradesByCourseId(course.getCourseId());
//
//                    try {
//                        while (gradesToRemove.iterator().hasNext()) {
//                            Grades grade = gradesToRemove.iterator().next();
//                            System.out.println("GradesId: " + grade.getGid());
//                            gradesRepository.delete(grade);
//                        }
//                    } catch (Exception e){
//                        System.out.println(e);
//                    }
//
//                    courseRepository.delete(course);

//            }
//        } catch (Exception e){
//            System.out.println(e);
//        }

//        programsRepository.delete(program);
        return "success";
    }
}