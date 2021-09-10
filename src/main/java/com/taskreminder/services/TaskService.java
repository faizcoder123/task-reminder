package com.taskreminder.services;

import com.taskreminder.entities.TaskEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public List<TaskEntity> getAllTasks(String email) {
        userService.getUserByGmail(email);
        List<TaskEntity> tasks = taskRepository.findByEmail(email);
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

    public TaskEntity addOrUpdateTask(TaskEntity task, String email) {
        userService.getUserByGmail(email);
        taskRepository.save(task);
        return task;
    }

    public TaskEntity updateTask(TaskEntity task, long id) {
        TaskEntity taskFound = getTask(id);
         if(!task.getEmail().equals(taskFound.getEmail())){
            throw new ApiRequestException("No such task found for this user");
         }
         task.setId(id);
         addOrUpdateTask(task, task.getEmail());
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
