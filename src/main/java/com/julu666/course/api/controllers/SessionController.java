package com.julu666.course.api.controllers;


import com.julu666.course.api.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController //注解 Annotation
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @GetMapping(path = "/code2session")
    public String code2Session(@RequestParam String code) {
        return sessionService.code2session(code);
    }
}
