package com.taskreminder.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserResponse {
    private long ownerId;
    private String userName;
    private String email;
    private String phoneNo;
}
