package com.julu666.course.api.requests.answer;


import lombok.Data;

@Data
public class SaveAnswerRequest {
    private String[] fileIds;
    private String title;
    private String content;
}
