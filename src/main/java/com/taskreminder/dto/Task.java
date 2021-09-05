package com.taskreminder.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private String subject;
    private String description;
    private String status;
    private ZonedDateTime deadline;
    private boolean priority;
    private ZonedDateTime createdBy;
    private ZonedDateTime modifiedTime;
    private ZonedDateTime createdTime;
}
