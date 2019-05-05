package com.julu666.course.api.services;

import com.julu666.course.api.jpa.Course;
import com.julu666.course.api.repositories.CourseRepository;
import com.julu666.course.api.requests.course.CourseAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @Autowired
    private CourseRepository  courseRepository;

    public boolean addCourse(String userId, CourseAddRequest courseAddRequest) {
        Course course = new Course();
        course.setUserId(userId);
        course.setTitle(courseAddRequest.getTitle());
        course.setDescription(courseAddRequest.getDescription());
        courseRepository.save(course);
        return true;
    }
}
