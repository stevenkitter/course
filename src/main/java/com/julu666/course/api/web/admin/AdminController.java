package com.julu666.course.api.web.admin;


import com.julu666.course.api.jpa.ApplyTeacher;
import com.julu666.course.api.repositories.ApplyTeacherRepository;
import com.julu666.course.api.utils.JWTToken;
import com.julu666.course.api.utils.RequestCookieToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;


@Controller
public class AdminController {

    @Resource(name = "applyTeacherRepository")
    private ApplyTeacherRepository applyTeacherRepository;



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

    @GetMapping(value = "/approval")
    public String approval(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        Page<ApplyTeacher> pages = applyTeacherRepository.findTop10(PageRequest.of(0, 10, Sort.Direction.DESC, "created_at"));
        model.addAttribute("approvals", pages.getContent());
        return "admin/approval";
    }
}
