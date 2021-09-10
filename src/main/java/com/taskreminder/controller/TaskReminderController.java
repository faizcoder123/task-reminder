package com.taskreminder.controller;

import com.taskreminder.entities.TaskEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskReminder")
public class TaskReminderController {

    @Autowired
    private TaskService taskService;

    @PostMapping(value = "/addTask")
    public ResponseEntity<TaskEntity> addTask(@RequestBody TaskEntity task) throws ApiRequestException {
        return new ResponseEntity<>(taskService.addOrUpdateTask(task), HttpStatus.OK);
    }

    @PatchMapping(value = "/updateTask/{id}")
    public ResponseEntity<TaskEntity> updateTask(@RequestBody TaskEntity task, @PathVariable long id) throws ApiRequestException{
        return new ResponseEntity<>(taskService.updateTask(task, id), HttpStatus.OK);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskEntity> getTask(@PathVariable long id) throws ApiRequestException {
        return new ResponseEntity<>(taskService.getTask(id), HttpStatus.OK);
    }

    @GetMapping("/task")
    public ResponseEntity<List<TaskEntity>> getAllTasksBelongsToUser(@RequestParam(required = true) String ownerEmail) throws ApiRequestException {
        return new ResponseEntity<>(taskService.getAllTasks(ownerEmail), HttpStatus.OK);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<TaskEntity> deleteTask(@PathVariable long id) throws ApiRequestException {
        return new ResponseEntity<>(taskService.deleteTask(id), HttpStatus.OK);
    }
}
