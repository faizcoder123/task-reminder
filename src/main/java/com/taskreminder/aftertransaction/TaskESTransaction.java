package com.taskreminder.aftertransaction;

import com.taskreminder.entities.TaskEntity;
import com.taskreminder.essync.TaskReminderESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.Entity;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
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
                    SecurityContextHolder.clearContext();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @PostRemove
    public void removeOperations(TaskEntity task) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization(){
            public void afterCommit(){
                try {
                    taskReminderESService.onDeleteRequest(task);
                    SecurityContextHolder.clearContext();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}