package com.julu666.course.api.controllers;

import com.julu666.course.api.jpa.Users;
import com.julu666.course.api.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/add")
    public @ResponseBody String addNewUser(@RequestParam String name) {
        Users user = new Users();
        user.setWxNickName(name);
        user.setAvatarUrl("xx");
        userRepository.save(user);
        return "Saved";
    }
}
