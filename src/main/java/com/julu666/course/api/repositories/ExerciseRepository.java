package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
    @Query("SELECT m FROM Exercise m WHERE m.id>0")
    Page<Exercise> findAllTop10(Pageable pageable);


    @Query("SELECT m FROM Exercise m WHERE m.id>0 AND m.userId = :userId")
    Page<Exercise> findAllByUserId(String userId, Pageable pageable);

    Optional<Exercise> findByExerciseId(String  exerciseId);

    Integer deleteByExerciseIdAndUserId(String exerciseId, String userId);


    String FIND_BY_USER_ID = "SELECT e FROM users_exercises m WHERE m.user_id = :userId LEFT JOIN exercises e ON e.exercise_id = m.exercise_id";
    @Query(value = FIND_BY_USER_ID,nativeQuery = true)
    Page<Exercise> findBuyedAllTop10(@Param("userId") String userId, Pageable pageable);
}
