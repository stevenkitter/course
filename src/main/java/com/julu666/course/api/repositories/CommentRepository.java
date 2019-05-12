package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    @Query("SELECT m FROM Comment m WHERE m.id>0 AND m.answerId = :answerId")
    Page<Comment> findByAnswerId(@Param("answerId") String answerId, Pageable pageable);
}
