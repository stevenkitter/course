package com.julu666.course.api.requests.user;

import lombok.Data;

@Data
public class ApplyToBeTeacherRequest {
    private String fileId;
    private String content;
}
