package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.ApplyTeacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import java.util.Optional;

public interface ApplyTeacherRepository extends CrudRepository<ApplyTeacher, Long> {
    Optional<ApplyTeacher> findByUserIdAndStatus(String userId, Integer status);

    @Query("SELECT m FROM ApplyTeacher m WHERE m.id>0")
    Page<ApplyTeacher> findTop10(Pageable pageable);
}
