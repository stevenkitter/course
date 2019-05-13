package com.julu666.course.api.requests.courseware;

import lombok.Data;

@Data
public class CoursewareAddRequest {
    private String title;
    private String content;
    private String fileId;
}
