package com.julu666.course.api.requests.user;

import lombok.Data;

@Data
public class ApprovalTeacherRequest {
    private Long id;
    private Integer status;
    private String reason;
}
