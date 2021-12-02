package com.taskreminder.aftertransaction;

import com.taskreminder.entities.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.Entity;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;


@Entity
public class UserTransaction {

    @PostPersist
    @PostUpdate
    @PostRemove
    public void operations(UserEntity user) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization(){
            public void afterCommit(){
                SecurityContextHolder.clearContext();
            }
        });
    }
}