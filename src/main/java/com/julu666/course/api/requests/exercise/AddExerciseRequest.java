package com.julu666.course.api.requests.exercise;


import lombok.Data;

@Data
public class AddExerciseRequest {
    private String fileId;
    private String title;
    private Long price;
}
