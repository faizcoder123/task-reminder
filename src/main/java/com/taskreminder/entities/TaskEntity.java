package com.taskreminder.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.taskreminder.essync.TaskESTransaction;
import com.taskreminder.util.ZonedDateTimeDeserializer;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "task")
@EntityListeners(value = {TaskESTransaction.class})
public class TaskEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(updatable = false)
    private long id;

    @NotBlank
    @Column(name = "subject")
    private String subject;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @Column(name = "dead_line")
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @NotNull
    private ZonedDateTime deadline;

    @Column(name = "modified_time")
    private ZonedDateTime modifiedTime;

    @Column(name = "status")
    private Status status;

    @Column(length = 65450, columnDefinition = "text", name = "description")
    private String description;

    @Column(name = "owner_email")
    @NotBlank
    private String ownerEmail;

}
