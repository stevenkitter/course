package com.julu666.course.api.requests.answer;


import lombok.Data;

@Data
public class AddCommentRequest {
    private String content;
    private String answerId;
}
