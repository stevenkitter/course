package com.julu666.course.api.controllers;

import com.julu666.course.api.requests.course.CourseAddRequest;
import com.julu666.course.api.response.Response;
import com.julu666.course.api.response.Wrapper;
import com.julu666.course.api.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping(path = "/course")
    public Response<String> addCourse(@RequestBody CourseAddRequest courseAddRequest) {
        return courseService.addCourse(courseAddRequest) ?
                Wrapper.okActionResp("新建成功","") :
                Wrapper.failActionResp("创建失败", "")
                ;
    }
}
