package com.julu666.course.api.controllers;


import com.julu666.course.api.response.Response;
import com.julu666.course.api.response.Wrapper;
import com.julu666.course.api.services.SessionService;
import com.julu666.course.api.utils.JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController //注解 Annotation
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @GetMapping(path = "/code2session")
    public Response<String> code2Session(@RequestParam String code) {
        String userId = sessionService.code2session(code);
        if (userId != "") {
            return Wrapper.okActionResp("", JWTToken.generateToken(userId));
        }
        return Wrapper.failActionResp("服务器错误～", "");
    }
}
