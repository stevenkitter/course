package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.Exercise;
import org.springframework.data.repository.CrudRepository;

public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
}
