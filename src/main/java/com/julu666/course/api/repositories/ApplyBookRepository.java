package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.ApplyBooks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ApplyBookRepository extends CrudRepository<ApplyBooks, Long> {
    Optional<ApplyBooks> findByUserIdAndBookId(String userId, Long bookId);

    @Query("SELECT m FROM ApplyBooks m WHERE m.id>0")
    Page<ApplyBooks> findByUserIdTop10(String userId, Pageable pageable);

    @Query("SELECT m FROM ApplyBooks m WHERE m.id>0")
    Page<ApplyBooks> findTop10(Pageable pageable);

    Optional<ApplyBooks> findByBookId(Long bookId);
}
