package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.Course;

import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {

}
