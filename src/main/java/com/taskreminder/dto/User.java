package com.taskreminder.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    UUID id;
    String userName;
    String password;
    String email;
    String phoneNo;
    private List<Task> tasksOwned;
}
