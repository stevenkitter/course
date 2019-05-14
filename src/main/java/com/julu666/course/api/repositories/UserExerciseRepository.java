package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.UserExercise;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserExerciseRepository extends CrudRepository<UserExercise, Long> {
    List<UserExercise> findAllByUserId(String userId);
    List<UserExercise> findByUserId(String userId);
}
