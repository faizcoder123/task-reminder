package com.taskreminder.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "task")
public class TaskEntity {

    @Id
    @GeneratedValue
    @Column(updatable = false)
    private long id;

    @NotBlank
    @Column(name = "subject")
    private String subject;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "created_by")
    private ZonedDateTime createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "dead_line")
    @NotBlank
    private ZonedDateTime deadline;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "modified_time")
    private ZonedDateTime modifiedTime;

    @Column(name = "status")
    private Status status;

    @Column(length = 65450, columnDefinition = "text", name = "description")
    private String description;

    @Column(name = "priority")
    private boolean priority;

}
