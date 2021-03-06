package com.julu666.course.api.web.admin;


import com.julu666.course.api.jpa.ApplyBooks;
import com.julu666.course.api.jpa.ApplyTeacher;
import com.julu666.course.api.jpa.TKFile;
import com.julu666.course.api.repositories.ApplyBookRepository;
import com.julu666.course.api.repositories.ApplyTeacherRepository;
import com.julu666.course.api.utils.JWTToken;
import com.julu666.course.api.utils.RequestCookieToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


@Controller
public class AdminController {

    @Resource(name = "applyTeacherRepository")
    private ApplyTeacherRepository applyTeacherRepository;

    @Resource(name = "applyBookRepository")
    private ApplyBookRepository applyBookRepository;


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
        for (ApplyTeacher at : pages.getContent()) {
            TKFile tkFile = at.getTkFile();
            tkFile.setFileName(downloadUri(tkFile.getFileName()));
            tkFile.setThumbnailName(downloadUri(tkFile.getThumbnailName()));
            at.setTkFile(tkFile);
        }
        model.addAttribute("approvals", pages.getContent());
        return "admin/approval";
    }

    public String downloadUri(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().scheme("https")
                .path("/downloadFile/")
                .path(filename)
                .toUriString();
    }


    @GetMapping(value = "/book_apply")
    public String book_apply(HttpServletRequest request, Model model) {
        Page<ApplyBooks> pages = applyBookRepository.findTop10(PageRequest.of(0, 10, Sort.Direction.DESC, "created_at"));
        model.addAttribute("approvals", pages.getContent());
        return "admin/book_apply";
    }

    @GetMapping(value = "/uploadCourseware")
    public String uploadCourseware(HttpServletRequest request, Model model){
        return "uploadCourseware";
    }

    @GetMapping(value = "/uploadedCourseware")
    public String uploadedCourseware(HttpServletRequest request, Model model){
        return "uploadedCourseware";
    }
}
