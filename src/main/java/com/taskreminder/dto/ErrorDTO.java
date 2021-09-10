package com.taskreminder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ErrorDTO {
    private String message;
    private HttpStatus status;
    private ZonedDateTime occurredAt;

}
