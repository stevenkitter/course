package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AnswerRepository extends CrudRepository<Answer, Long> {
    @Query("SELECT m FROM Answer m WHERE m.id>0")
    Page<Answer> findByUserIdTop(String userId, Pageable pageable);
}
