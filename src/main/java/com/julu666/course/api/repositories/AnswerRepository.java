package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnswerRepository extends CrudRepository<Answer, Long> {
    @Query("SELECT m FROM Answer m WHERE m.id>0 AND m.userId = :userId")
    Page<Answer> findByUserIdTop(@Param("userId") String userId, Pageable pageable);

    @Query("SELECT m FROM Answer m WHERE m.id>0")
    Page<Answer> findAllTop10(Pageable pageable);

    Optional<Answer> findByAnswerIdAndUserId(String answerId, String userId);
}
