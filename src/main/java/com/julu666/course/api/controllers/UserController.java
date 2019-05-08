package com.julu666.course.api.controllers;

import com.julu666.course.api.constants.Global;
import com.julu666.course.api.jpa.Admin;
import com.julu666.course.api.jpa.ApplyTeacher;
import com.julu666.course.api.jpa.User;
import com.julu666.course.api.repositories.AdminRepository;
import com.julu666.course.api.repositories.ApplyTeacherRepository;
import com.julu666.course.api.repositories.ApprovalTeacherRequest;
import com.julu666.course.api.repositories.UserRepository;
import com.julu666.course.api.requests.user.AdminUserRequest;
import com.julu666.course.api.requests.user.ApplyToBeTeacherRequest;
import com.julu666.course.api.requests.user.SaveUserInfoRequest;
import com.julu666.course.api.response.Response;
import com.julu666.course.api.response.Wrapper;
import com.julu666.course.api.services.UserService;
import com.julu666.course.api.utils.JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@RestController

@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Resource(name = "applyTeacherRepository")
    private ApplyTeacherRepository applyTeacherRepository;

    @Resource(name = "userRepository")
    private UserRepository userRepository;

    @Resource(name = "adminRepository")
    private AdminRepository adminRepository;

    @GetMapping(path = "/user")
    public @ResponseBody
    User user(@RequestParam Long id) {
        return userService.getUser(id);
    }


    @PostMapping(path = "/saveUserInfo")
    public Response<String> saveUserInfo(@RequestHeader(value = "Token") String token, @RequestBody SaveUserInfoRequest request) {
        String userId = JWTToken.userId(token);
        if (!userRepository.findByUserId(userId).isPresent()) {
            return Wrapper.failActionResp("信息错误", "");
        }
        User user = userRepository.findByUserId(userId).get();
        user.setAvatarUrl(request.getAvatarUrl());
        user.setGender(request.getGender());
        user.setCity(request.getCity());
        user.setCountry(request.getCountry());
        user.setLanguage(request.getLanguage());
        user.setProvince(request.getProvince());
        user.setWxNickName(request.getNickName());
        userRepository.save(user);
        return Wrapper.okActionResp("", "保存成功");
    }


    @PostMapping(path="/apply")
    public Response<String> apply(@RequestHeader(value = "Token") String token, @RequestBody ApplyToBeTeacherRequest request) {
        String userId = JWTToken.userId(token);

        // 申请中
        if (applyTeacherRepository.findByUserIdAndStatus(userId, 0).isPresent()) {
            return Wrapper.failActionResp("申请中，无需重复申请", "");
        }
        if (applyTeacherRepository.findByUserIdAndStatus(userId, 2).isPresent()) {
            return Wrapper.failActionResp("您已申请为老师了～", "");
        }
        ApplyTeacher applyTeacher = new ApplyTeacher();
        applyTeacher.setFileId(request.getFileId());
        applyTeacher.setContent(request.getContent());
        applyTeacher.setUserId(userId);
        applyTeacher.setAdminId(Global.AdminId);
        applyTeacherRepository.save(applyTeacher);
        return Wrapper.okActionResp("申请成功，请等待审核", "");
    }

    @Transactional
    @PostMapping(path="/approval_teacher")
    public Response<String> approvalTeacher(@RequestHeader(value = "Token") String token, @RequestBody ApprovalTeacherRequest request) {
        String userId = JWTToken.userId(token);
        if (!applyTeacherRepository.findById(request.getId()).isPresent()) {
            return Wrapper.failActionResp("无相关数据","");
        }
        ApplyTeacher applyTeacher = applyTeacherRepository.findById(request.getId()).get();
        applyTeacher.setStatus(request.getStatus());
        applyTeacher.setReason(request.getReason());
        applyTeacher.setAdminId(userId);
        applyTeacherRepository.save(applyTeacher);

        if (!userRepository.findByUserId(applyTeacher.getUserId()).isPresent()) {
            return Wrapper.failActionResp("无相关用户","");
        }
        User user = userRepository.findByUserId(applyTeacher.getUserId()).get();
        if (request.getStatus() == 2) {
            user.setUserRole(1);
        }
        userRepository.save(user);
        return Wrapper.okActionResp("审批成功", "");
    }

    @PostMapping(path = "/adminUser")
    public Response<String> adminUser(@RequestBody AdminUserRequest adminUserRequest) {
        Admin admin = adminRepository.findByPhone(adminUserRequest.getPhone()).orElse(new Admin());
        admin.setPhone(adminUserRequest.getPhone());
        admin.setPassword(adminUserRequest.getPassword());
        adminRepository.save(admin);
        return Wrapper.okActionResp("注册成功","");
    }

    @GetMapping(path = "/userInfo")
    public Response<User> userInfo(@RequestHeader(value = "token") String token) {
        String userId = JWTToken.userId(token);
        if (userRepository.findByUserId(userId).isPresent()) {
            User user = userRepository.findByUserId(userId).get();
            return new Response<>(200, "", user);
        }
        return new Response<>(400, "", null);
    }
}
