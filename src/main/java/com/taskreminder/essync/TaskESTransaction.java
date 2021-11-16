package com.taskreminder.essync;

import com.taskreminder.entities.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.Entity;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import java.io.IOException;

@Entity
public class TaskESTransaction {

    @Autowired
    TaskReminderESService taskReminderESService;

    @PostPersist
    @PostUpdate
    public void operations(TaskEntity task) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization(){
            public void afterCommit(){
                try {
                    taskReminderESService.onUpdateRequest(task);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}