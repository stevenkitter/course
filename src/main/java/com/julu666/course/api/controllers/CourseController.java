package com.julu666.course.api.controllers;

import com.julu666.course.api.constants.Global;
import com.julu666.course.api.jpa.Course;
import com.julu666.course.api.jpa.TKFile;
import com.julu666.course.api.repositories.CourseRepository;
import com.julu666.course.api.repositories.FileRepository;
import com.julu666.course.api.requests.course.CourseAddRequest;
import com.julu666.course.api.response.Response;
import com.julu666.course.api.response.Wrapper;

import com.julu666.course.api.utils.JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/course")
public class CourseController {



    @Autowired
    private CourseRepository courseRepository;

    @Resource
    private FileRepository fileRepository;

    @Transactional
    @PostMapping(path = "/add")
    public Response<String> addCourse(@RequestHeader(value="Token") String authorization, @RequestBody CourseAddRequest courseAddRequest) {

        if (!fileRepository.getByFileIdAndFileIdNotNull(courseAddRequest.getFileId()).isPresent()) {
            return Wrapper.failActionResp("创建失败", "");
        }

        String userId = JWTToken.userId(authorization);
        Course course = new Course();
        course.setUserId(userId);
        course.setTitle(courseAddRequest.getTitle());
        course.setDescription(courseAddRequest.getDescription());
        courseRepository.save(course);


        TKFile tkFile = fileRepository.getByFileIdAndFileIdNotNull(courseAddRequest.getFileId()).get();
        tkFile.setSourceId(course.getCourseId());
        fileRepository.save(tkFile);
        return Wrapper.okActionResp("新建成功","");
    }

    @GetMapping(path = "/all")
    public Response<List<Course>> allCourses(@RequestHeader(value="Token") String authorization, @RequestParam(value = "page") Integer page) {
        Page<Course> pageCourse = courseRepository.findTop10(PageRequest.of(page, 10, Sort.Direction.DESC, "created_at"));

        appendUrl(pageCourse.getContent());

        return new Response<>(200, "", pageCourse.getContent());
    }

    @GetMapping(path = "/my")
    public Response<List<Course>> myCourses(@RequestHeader(value="Token") String authorization, @RequestParam(value = "page") Integer page) {
        String userId = JWTToken.userId(authorization);
//        List<Course> courses = courseRepository.findAllByUserId(userId);
        Page<Course> pageCourse = courseRepository.findByUserId(userId, PageRequest.of(page, 10, Sort.Direction.DESC, "created_at"));

        appendUrl(pageCourse.getContent());


        return new Response<>(200, "", pageCourse.getContent());
    }

    private void appendUrl(List<Course> pageCourse) {
        for (Course course : pageCourse) {
            TKFile tkFile = course.getTkFile();
            if (tkFile == null) {
                continue;
            }
            String fileThumbDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(tkFile.getThumbnailName())
                    .toUriString();
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(tkFile.getFileName())
                    .toUriString();
            tkFile.setThumbnailName(fileThumbDownloadUri);
            tkFile.setFileName(fileDownloadUri);
            course.setTkFile(tkFile);
        }
    }

    @Modifying
    @Transactional
    @DeleteMapping("/delete")
    public Response<String> deleteMyCourse(@RequestHeader(value="Token") String authorization, @RequestParam(value = "courseId") String courseId) {
        String userId = JWTToken.userId(authorization);

        if (!courseRepository.findByCourseId(courseId).isPresent()) {
            return Wrapper.failActionResp("无相关数据","");
        }

        Course course = courseRepository.findByCourseId(courseId).get();

        Integer affected = courseRepository.deleteByCourseIdAndUserId(courseId, userId);

        if (affected == 0) {
            return Wrapper.failActionResp("无法删除","");
        }

        if (course.getTkFile() != null) {
            Integer aff = fileRepository.deleteByFileId(course.getTkFile().getFileId());
            if (aff == 0) {
                return Wrapper.failActionResp("无法删除","");
            }
        }



        return Wrapper.okActionResp("删除成功", "");
    }

    @GetMapping("/famous")
    public Response<List<Course>> famousList(@RequestHeader(value="Token") String token) {
        List<Course> courses = new ArrayList<>();
        for(String id : Global.FamousCourseIds) {
            Course course = courseRepository.findByCourseId(id).get();
            courses.add(course);
        }
        appendUrl(courses);
        return new Response<>(200, "", courses);
    }
}
