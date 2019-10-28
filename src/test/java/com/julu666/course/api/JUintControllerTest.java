package com.julu666.course.api;

import com.julu666.course.api.controllers.UserController;
import com.julu666.course.api.jpa.User;
import com.julu666.course.api.repositories.UserRepository;

import com.julu666.course.api.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JUintControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUserById() {

        Response<User> user = userController.userInfo("Bearer eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOiIyMzI4YmUxMS1jZWJmLTRjMDUtYjE2Ny01MGE0NzNlOGM4ZmYifQ.grT7D4VN9AckqK--RjBsImqrjQs20EryK0fXvYQjJcsfZnvtorxIWeBE22rpCUb1n3gddYpt0tNjyZYhXNquvA");

        Optional<User> op = userRepository.findByUserId("2328be11-cebf-4c05-b167-50a473e8c8ff");
        if (op.isPresent()) {
            User u = op.get();
            Assert.assertEquals("测试通过", u.getUserId(), 2);
        }
    }
}
