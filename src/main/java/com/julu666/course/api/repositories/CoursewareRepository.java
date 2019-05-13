package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.Course;
import com.julu666.course.api.jpa.Courseware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CoursewareRepository extends CrudRepository<Courseware, Long> {
    @Query("SELECT m FROM Courseware m WHERE m.id>0")
    Page<Courseware> findTop10(Pageable pageable);

    @Query("SELECT m FROM Courseware m WHERE m.id>0 AND m.userId = :userId")
    Page<Courseware> findByUserId(@Param("userId") String userId, Pageable pageable);

    Optional<Courseware> findByCoursewareId(String coursewareId);

    Integer deleteByCoursewareIdAndUserId(String courseId, String userId);
}
