package com.taskreminder.controller;

import com.taskreminder.entities.TaskEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/taskReminder")
public class TaskReminderController {

    @Autowired
    private TaskService taskService;

    @PostMapping(value = "/addTask")
    public ResponseEntity<TaskEntity> addTask(@RequestBody TaskEntity task, Principal principal) throws ApiRequestException {
        return new ResponseEntity<>(taskService.addOrUpdateTask(task, principal), HttpStatus.OK);
    }

    @PatchMapping(value = "/updateTask/{id}")
    public ResponseEntity<TaskEntity> updateTask(@RequestBody TaskEntity task, @PathVariable long id,  Principal principal) throws ApiRequestException{
        return new ResponseEntity<>(taskService.updateTask(task, id, principal), HttpStatus.OK);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskEntity> getTask(@PathVariable long id,  Principal principal) throws ApiRequestException {
        return new ResponseEntity<>(taskService.getTask(id, principal), HttpStatus.OK);
    }

    @GetMapping("/task")
    public ResponseEntity<List<TaskEntity>> getAllTasksBelongsToUser(Principal principal) throws ApiRequestException {
        return new ResponseEntity<>(taskService.getAllTasks(principal), HttpStatus.OK);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<TaskEntity> deleteTask(@PathVariable long id,  Principal principal) throws ApiRequestException {
        return new ResponseEntity<>(taskService.deleteTask(id, principal), HttpStatus.OK);
    }
}
