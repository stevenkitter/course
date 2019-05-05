package com.julu666.course.api.requests.course;


import lombok.Data;

@Data
public class CourseAddRequest {
//    private String userId;
    private String title;
    private String description;
    private String fileId;
}
