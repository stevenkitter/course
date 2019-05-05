package com.julu666.course.api.requests.course;


import lombok.Data;

@Data
public class FileAddRequest {
    private String userId;
    private String fileName;
    private String thumbnailName;
}
