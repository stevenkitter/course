package com.julu666.course.api.requests.user;

import lombok.Data;

@Data
public class AdminUserRequest {
    private String phone;
    private String password;
}
