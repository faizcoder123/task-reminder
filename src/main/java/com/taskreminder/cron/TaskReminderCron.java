package com.taskreminder.cron;


import com.taskreminder.entities.TaskEntity;
import com.taskreminder.entities.UserEntity;
import com.taskreminder.services.TaskService;
import com.taskreminder.services.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.Principal;
import java.util.List;


@Component
public class TaskReminderCron implements CommandLineRunner {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Override
    @Scheduled(cron = "0 */6 * * *")
    public void run(String... args) {

        System.out.println("Sending Email...");

        sendEmail();

        System.out.println("Done");

    }

    void sendEmail() {
        List<UserEntity> users = userService.getAllUser();

        for(UserEntity user : users) {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(user.getEmail());
            msg.setSubject("Daily Task Reminder");
            List<TaskEntity> tasks = taskService.getAllTasksForReminder(user.getEmail());
            String messageText = "";
            tasks.forEach( (task)-> messageText.concat(task.getDescription() + "\n"));
            msg.setText(messageText);
            javaMailSender.send(msg);
        }
    }
}