package com.example.demo.controllers;

import com.example.demo.models.Student;
import com.example.demo.respositories.StudentRepository;
import org.camunda.bpm.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/camunda-services/")
public class CamundaActionController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/complete-user-task")
//    @Transactional(transactionManager = "domainTransactionManager", propagation = Propagation.REQUIRED)
    public ResponseEntity CompleteUserTask(@RequestBody CompleteUserTaskInputDto dto){
        var userTask = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();
        Objects.requireNonNull(userTask, "Task Id [%s] not exist..".formatted(dto.getTaskId()));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
