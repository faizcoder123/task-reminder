package com.taskreminder.repository;


import com.taskreminder.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Transactional
    List<TaskEntity> findByOwnerEmail(String email);

    @Transactional
    @Query(value="delete from TaskEntity task where task.email = ?1")
    void deleteByOwnerEmail(String email);
}
