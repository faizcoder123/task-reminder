package com.taskreminder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ErrorDTO {
    private String message;
    private ZonedDateTime occurredAt;

}
