package com.example.courseproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // This means that this class is a Controller
@RequestMapping(path="/student") // This means URL's start with /demo (after Application path)
public class StudentController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private StudentRepository studentRepository;

    // path to Add a particular student
    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewStudent (@RequestParam String firstName, @RequestParam String lastName,
                                               @RequestParam String email, @RequestParam String address,
                                               @RequestParam String city, @RequestParam String postal,
                                               @RequestParam String phone){
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setAddress(address);
        student.setCity(city);
        student.setPostal(postal);
        student.setPhone(phone);
        studentRepository.save(student);
        return "Saved";
    }

    // alternate /add route to send in json object rather than path parameters
    @PostMapping(path="/add",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE) // Map ONLY POST Requests
    public @ResponseBody String addNewStudentObject (@RequestBody Student newStudent){
        Student student = studentRepository.save(newStudent);
        return "Saved";
    }

    // path to List all students
    @GetMapping(path="/list")
    public @ResponseBody Iterable<Student> getAllStudents() {
        // This returns a JSON or XML with the users
        return studentRepository.findAll();
    }

    // path to View one student based on ID
    @GetMapping(path="/view/{id}")
    public @ResponseBody Student getStudent(@PathVariable Integer id) {
        // This returns a JSON or XML with the users
        return studentRepository.findStudentByStudentId(id);
    }

    @PutMapping(path="/modify",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Student modifyStudent(@RequestBody Student modifiedStudent){
        Student student = studentRepository.findStudentByStudentId(modifiedStudent.getStudentId());

        student.setFirstName(modifiedStudent.getFirstName());
        student.setLastName(modifiedStudent.getLastName());
        student.setEmail(modifiedStudent.getEmail());
        student.setAddress(modifiedStudent.getAddress());
        student.setCity(modifiedStudent.getCity());
        student.setPostal(modifiedStudent.getPostal());
        student.setPhone(modifiedStudent.getPhone());

        final Student updatedStudent = studentRepository.save(student);

        return updatedStudent;
    }

    @DeleteMapping(path="/delete")
    public @ResponseBody String deleteStudent(@RequestBody Student studentToDelete){
        Student student = studentRepository.findStudentByStudentId(studentToDelete.getStudentId());
        studentRepository.delete(student);
        return studentToDelete.getFirstName() + " " + studentToDelete.getLastName() + " has been deleted!";
    }
}
