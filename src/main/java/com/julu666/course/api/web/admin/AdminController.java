package com.julu666.course.api.web.admin;


import com.julu666.course.api.utils.JWTToken;
import com.julu666.course.api.utils.RequestCookieToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;



@Controller
public class AdminController {

    @GetMapping(value = "/admin")
    public String admin(HttpServletRequest request) throws UnsupportedEncodingException {
        String token = RequestCookieToken.getToken(request);
        if (token.length() == 0) {
            return "redirect:/login";
        }
        String tok = URLDecoder.decode(token, "utf-8");
        String adminId = JWTToken.userId(tok);
        if (adminId.length() > 0) {
            return "admin/admin";
        }
        return "redirect:/login";

    }
}
