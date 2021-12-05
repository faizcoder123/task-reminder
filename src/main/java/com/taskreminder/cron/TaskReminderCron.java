package com.taskreminder.cron;


import com.taskreminder.entities.TaskEntity;
import com.taskreminder.entities.UserEntity;
import com.taskreminder.services.TaskService;
import com.taskreminder.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class TaskReminderCron {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Scheduled(cron = "0 1 1 * * ?")
    void sendEmail() {
        List<UserEntity> users = userService.getAllUsers();
        for(UserEntity user : users) {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(user.getEmail());
            msg.setSubject("Daily Task Reminder");
            List<TaskEntity> tasks = taskService.getAllTasksOfUser(user.getEmail());
            if(tasks.isEmpty()){
                msg.setText("No tasks in the Bucket");
            }
            else{
                String messageText = "";
                tasks.forEach( (task)-> messageText.concat(task.getDescription() + "\n"));
                msg.setText(messageText);
            }
            javaMailSender.send(msg);
        }
    }
}