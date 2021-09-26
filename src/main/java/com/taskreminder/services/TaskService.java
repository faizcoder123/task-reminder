package com.taskreminder.services;

import com.taskreminder.entities.Status;
import com.taskreminder.entities.TaskEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
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

    public List<TaskEntity> getAllTasks(Principal principal) {
        List<TaskEntity> tasks = taskRepository.findByOwnerEmail(principal.getName());
        if(tasks.isEmpty()){
            throw new ApiRequestException("No Tasks found for this User");
        }
        return tasks;
    }

    public TaskEntity deleteTask(long id, Principal principal) {
        TaskEntity task = getTask(id, principal);
        taskRepository.deleteById(id);
        return task;
    }

    public TaskEntity addOrUpdateTask(TaskEntity task, Principal principal) {
        task.setOwnerEmail(principal.getName());
        if(Objects.isNull(task.getStatus())){
            task.setCreatedTime(ZonedDateTime.parse(formatter.format(ZonedDateTime.now()), formatter));
            task.setStatus(Status.STARTED);
        }
        task.setModifiedTime(ZonedDateTime.parse(formatter.format(ZonedDateTime.now()), formatter));
        taskRepository.save(task);
        return task;
    }

    public TaskEntity updateTask(TaskEntity task, long id, Principal principal) {
        TaskEntity taskFound = getTask(id, principal);
         if(!task.getOwnerEmail().equals(taskFound.getOwnerEmail())){
            throw new ApiRequestException("No such task found for this user");
         }
         task.setId(id);
         addOrUpdateTask(task, principal);
         return task;
    }

    public TaskEntity getTask(long id, Principal principal) {
        Optional<TaskEntity> task = taskRepository.findById(id);
        if(task.isPresent()){
            if(!principal.getName().equals(task.get().getOwnerEmail())) throw new ApiRequestException("Authentication failed");

            return task.get();
        }
        else {
            throw new ApiRequestException("task not found");
        }
    }

}
