package com.julu666.course.api.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class RequestCookieToken {

    public static String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("token")){
                    return cookie.getValue();
                }
            }
        }
        return "";
    }
}
