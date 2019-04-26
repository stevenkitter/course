package com.julu666.course.api.services;

import com.julu666.course.api.jpa.Users;
import com.julu666.course.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Users getUser(Long id) {
        return userRepository.findById(id).get();
    }

}
