package com.julu666.course.api.jpa;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users_exercises")
public class UserExercise extends Base {
    private String userId;
    private String exerciseId;



}
