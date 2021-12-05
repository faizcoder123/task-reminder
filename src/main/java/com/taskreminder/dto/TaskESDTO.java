package com.taskreminder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Getter
@Setter
@EqualsAndHashCode
@Document(indexName = "task-reminder-service", createIndex = false)

public class TaskESDTO {

    @Field(name = "id")
    @JsonProperty("id")
    private long id;

    @Field(name = "owner_email")
    @JsonProperty("owner_email")
    private String ownerEmail;

    @Field(name = "created_time")
    @JsonProperty("created_time")
    private String createdTime;

    @Field(name = "modified_time")
    @JsonProperty("modified_time")
    private String modifiedTime;

    @Field(name = "subject")
    @JsonProperty("subject")
    private String subject;

    @Field(name = "description")
    @JsonProperty("description")
    private String description;

    @Field(name = "status")
    @JsonProperty("status")
    private String status;

    @Field(name = "dead_line")
    @JsonProperty("dead_line")
    private String deadLine;

}
