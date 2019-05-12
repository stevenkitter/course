package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.ApplyTeacher;
import com.julu666.course.api.jpa.Course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Long> {

    @Query("SELECT m FROM Course m WHERE m.id>0")
    Page<Course> findTop10(Pageable pageable);

    @Query("SELECT m FROM Course m WHERE m.id>0 AND m.userId = :userId")
    Page<Course> findByUserId(@Param("userId") String userId, Pageable pageable);

    Integer deleteByCourseIdAndUserId(String courseId, String userId);

    Optional<Course> findByCourseId(String courseId);
}
