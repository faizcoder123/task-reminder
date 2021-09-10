package com.taskreminder.services;

import com.taskreminder.entities.Status;
import com.taskreminder.entities.TaskEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

    public List<TaskEntity> getAllTasks(String email) {
        List<TaskEntity> tasks = taskRepository.findByOwnerEmail(email);
        if(tasks.isEmpty()){
            throw new ApiRequestException("No Tasks found for this User");
        }
        return tasks;
    }

    public TaskEntity deleteTask(long id) {
        TaskEntity task = getTask(id);
        taskRepository.deleteById(id);
        return task;
    }

    public TaskEntity addOrUpdateTask(TaskEntity task) {
       if(Objects.isNull(task.getOwnerEmail())){
           throw  new ApiRequestException("Please Provide the Owner Mail");
       }
        if(Objects.isNull(task.getStatus())){
            task.setCreatedTime(ZonedDateTime.parse(formatter.format(ZonedDateTime.now()), formatter));
            task.setStatus(Status.STARTED);
        }
        task.setModifiedTime(ZonedDateTime.parse(formatter.format(ZonedDateTime.now()), formatter));
        taskRepository.save(task);
        return task;
    }

    public TaskEntity updateTask(TaskEntity task, long id) {
        TaskEntity taskFound = getTask(id);
         if(!task.getOwnerEmail().equals(taskFound.getOwnerEmail())){
            throw new ApiRequestException("No such task found for this user");
         }
         task.setId(id);
         addOrUpdateTask(task);
         return task;
    }

    public TaskEntity getTask(long id) {
        Optional<TaskEntity> task = taskRepository.findById(id);
        if(task.isPresent()){
            return task.get();
        }
        else {
            throw new ApiRequestException("task not found");
        }
    }

}
