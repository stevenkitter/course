package com.julu666.course.api.response;

import com.julu666.course.api.jpa.Answer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MyAnswerResponse implements Serializable {
    private Long count;
    private List<Answer> answers;
}
