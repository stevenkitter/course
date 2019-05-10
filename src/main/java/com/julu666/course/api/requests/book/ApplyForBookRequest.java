package com.julu666.course.api.requests.book;


import lombok.Data;

@Data
public class ApplyForBookRequest {
    private Long[] bookIds;
    private Integer status;
}
