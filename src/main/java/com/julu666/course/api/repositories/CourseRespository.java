package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.Course;
import com.julu666.course.api.jpa.User;
import org.springframework.data.repository.CrudRepository;

public interface CourseRespository extends CrudRepository<Course, Long> {

}
