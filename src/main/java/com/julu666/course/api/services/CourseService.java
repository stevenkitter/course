package com.julu666.course.api.services;

import com.julu666.course.api.jpa.Course;
import com.julu666.course.api.jpa.TKFile;
import com.julu666.course.api.repositories.CourseRepository;
import com.julu666.course.api.repositories.FileRepository;
import com.julu666.course.api.requests.course.CourseAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository  courseRepository;

    @Resource
    private FileRepository fileRepository;


}
