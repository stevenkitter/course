package com.julu666.course.api.web.login;

import com.julu666.course.api.jpa.Admin;
import com.julu666.course.api.repositories.AdminRepository;
import com.julu666.course.api.utils.JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
public class LoginController {

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping(value = "/login")
    public String Index(Model model) {
        model.addAttribute("phone", "");
        model.addAttribute("password", "");
        return "login";
    }

    @PostMapping(value = "/login")
    public String Login(@ModelAttribute(name = "loginForm") LoginForm loginForm, Model model, HttpServletResponse response) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        if (loginForm.getPhone() == null || loginForm.getPhone().trim().equals("")) {
            return "login";
        }

        if (!adminRepository.findByPhone(loginForm.getPhone()).isPresent()) {
            model.addAttribute("invalid", true);
            return "login";
        }

        Admin admin = adminRepository.findByPhone(loginForm.getPhone()).get();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(loginForm.getPassword().getBytes());
        byte[] digest = messageDigest.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toLowerCase();
        if (admin.getPassword().equals(myHash)) {
            String token = JWTToken.generateToken(admin.getUserId());
            String encode = URLEncoder.encode("Bearer " + token, "utf-8");
            Cookie cookie = new Cookie("token", encode);
            response.addCookie(cookie);
            return "redirect:/admin";
        }
        model.addAttribute("invalid", true);
        return "login";
    }


}
