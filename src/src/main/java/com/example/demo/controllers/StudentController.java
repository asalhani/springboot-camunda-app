package com.example.demo.controllers;

import com.example.demo.models.Student;
import com.example.demo.respositories.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class StudentController {

    private StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping("/student")
    private ResponseEntity AddStudent(@RequestBody Student dto){
        studentRepository.save(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/student")
    private ResponseEntity<Optional<Student> > GetAll(){
        Optional<Student> students = studentRepository.findAll().stream().findAny();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

}
