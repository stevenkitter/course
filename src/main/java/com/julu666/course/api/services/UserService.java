package com.julu666.course.api.services;

import com.julu666.course.api.jpa.User;
import com.julu666.course.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(Long id) {
        return userRepository.findById(id).get();
    }
    public Boolean newUser(User user) {
        return userRepository.save(user).getId() != 0;
    }
}
