package com.julu666.course.api.interceptor;

import com.julu666.course.api.utils.JWTToken;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");

        String[] tokens = authorization.split(" ");
        if (tokens.length != 2) {
            return false;
        }
        String token = tokens[1];
        Claims claims = JWTToken.parseJWT(token);
        if (claims.get("userId").toString() != "") {
            return true;
        }
        return false;
    }
}
