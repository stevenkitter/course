package com.julu666.course.api.requests.book;

import lombok.Data;

@Data
public class ApprovalBookRequest {
    private Long id;
    private Integer status;
}
