package com.julu666.course.api.controllers;

import com.julu666.course.api.jpa.User;
import com.julu666.course.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/user")
    public @ResponseBody
    User user(@RequestParam Long id) {
        return userService.getUser(id);
    }
}
