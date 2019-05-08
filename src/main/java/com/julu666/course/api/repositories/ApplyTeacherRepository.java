package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.ApplyTeacher;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ApplyTeacherRepository extends CrudRepository<ApplyTeacher, Long> {
    Optional<ApplyTeacher> findByUserIdAndStatus(String userId, Integer status);
}
