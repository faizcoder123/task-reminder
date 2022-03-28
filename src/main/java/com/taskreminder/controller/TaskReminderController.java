package com.taskreminder.controller;

import com.taskreminder.dto.PaginationDTO;
import com.taskreminder.dto.SearchParams;
import com.taskreminder.dto.TaskESDTO;
import com.taskreminder.entities.TaskEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/taskReminder")
@CrossOrigin(
        allowCredentials = "true",
        origins = "http://localhost:3000",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PATCH}
)
public class TaskReminderController {

    @Autowired
    private TaskService taskService;

    @PostMapping(value = "/addTask")
    public ResponseEntity<TaskEntity> addTask(@RequestBody TaskEntity task, Principal principal) throws ApiRequestException {
        return new ResponseEntity<>(taskService.addOrUpdateTask(task, principal), HttpStatus.OK);
    }

    @PatchMapping(value = "/updateTask/{id}")
    public ResponseEntity<TaskEntity> updateTask(@RequestBody TaskEntity task, @PathVariable long id, Principal principal) throws ApiRequestException {
        return new ResponseEntity<>(taskService.updateTask(task, id, principal), HttpStatus.OK);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskEntity> getTask(@PathVariable long id, Principal principal) throws ApiRequestException {
        return new ResponseEntity<>(taskService.getTask(id, principal), HttpStatus.OK);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskEntity>> getAllTasksBelongsToUser(Principal principal) throws ApiRequestException {
        return new ResponseEntity<>(taskService.getAllTasks(principal), HttpStatus.OK);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable long id, Principal principal) throws ApiRequestException, IOException {
        taskService.deleteTask(id, principal);
        return new ResponseEntity<>("{DELETED}", HttpStatus.OK);
    }


    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<List<TaskESDTO>> search(
            @Valid @RequestBody SearchParams searchParams,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer perPage,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder, Principal principal) throws ApiRequestException {
        PaginationDTO pagination = new PaginationDTO().buildPagination(searchParams, page, perPage, sortBy, sortOrder);
        List<TaskESDTO> tasks = taskService.search(pagination, searchParams.getSearchCriteria(), principal.getName());
        return ResponseEntity.ok().body(tasks);
    }
}