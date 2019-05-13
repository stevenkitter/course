package com.julu666.course.api.controllers;


import com.julu666.course.api.jpa.Course;
import com.julu666.course.api.jpa.Courseware;
import com.julu666.course.api.jpa.TKFile;
import com.julu666.course.api.repositories.CoursewareRepository;
import com.julu666.course.api.repositories.FileRepository;
import com.julu666.course.api.requests.courseware.CoursewareAddRequest;
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
import java.util.List;

@RestController
@RequestMapping(value = "/courseware")
public class CoursewareController {

    @Autowired
    private CoursewareRepository coursewareRepository;

    @Resource
    private FileRepository fileRepository;

    @Transactional
    @PostMapping("/add")
    public Response<String> addCourseware(@RequestHeader(value="Token") String authorization, @RequestBody CoursewareAddRequest coursewareAddRequest) {
        String userId = JWTToken.userId(authorization);
        Courseware courseware = new Courseware();
        courseware.setTitle(coursewareAddRequest.getTitle());
        courseware.setContent(coursewareAddRequest.getContent());
        courseware.setUserId(userId);
        coursewareRepository.save(courseware);

        if (!fileRepository.findByFileId(coursewareAddRequest.getFileId()).isPresent()) {
            return Wrapper.failActionResp("无法创建","");
        }
        TKFile tkFile = fileRepository.findByFileId(coursewareAddRequest.getFileId()).get();
        tkFile.setSourceId(courseware.getCoursewareId());
        fileRepository.save(tkFile);
        return Wrapper.okActionResp("新建成功","");
    }


    @GetMapping("/all")
    public Response<List<Courseware>> allCourseware(@RequestHeader(value="Token") String authorization, @RequestParam(value = "page") Integer page) {
        Page<Courseware> pageCourse = coursewareRepository.findTop10(PageRequest.of(page, 10, Sort.Direction.DESC, "created_at"));


        appendUrl(pageCourse.getContent());

        return new Response<>(200, "", pageCourse.getContent());
    }

    @GetMapping("/my")
    public Response<List<Courseware>> myCourseware(@RequestHeader(value="Token") String authorization, @RequestParam(value = "page") Integer page) {
        String userId = JWTToken.userId(authorization);
        Page<Courseware> pageCourse = coursewareRepository.findByUserId(userId, PageRequest.of(page, 10, Sort.Direction.DESC, "created_at"));
        appendUrl(pageCourse.getContent());

        return new Response<>(200, "", pageCourse.getContent());
    }

    @Modifying
    @Transactional
    @DeleteMapping("/delete")
    public Response<String> deleteMyCourseware(@RequestHeader(value="Token") String authorization, @RequestParam(value = "coursewareId") String coursewareId) {
        String userId = JWTToken.userId(authorization);

        if (!coursewareRepository.findByCoursewareId(coursewareId).isPresent()) {
            return Wrapper.failActionResp("无相关数据","");
        }

        Courseware courseware = coursewareRepository.findByCoursewareId(coursewareId).get();


        if (coursewareRepository.deleteByCoursewareIdAndUserId(coursewareId, userId) == 0) {
            return Wrapper.failActionResp("无法删除","");
        }

        if (courseware.getTkFile() != null) {
            Integer aff = fileRepository.deleteByFileId(courseware.getTkFile().getFileId());
            if (aff == 0) {
                return Wrapper.failActionResp("无法删除","");
            }
        }

        return Wrapper.okActionResp("删除成功", "");
    }



    private String downloadUri(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(filename)
                .toUriString();
    }

    private void appendUrl(List<Courseware> pageCourse) {
        for (Courseware courseware : pageCourse) {
            TKFile tkFile = courseware.getTkFile();
            if (tkFile == null) {
                continue;
            }
            String fileThumbDownloadUri = downloadUri(tkFile.getThumbnailName());
            String fileDownloadUri = downloadUri(tkFile.getFileName());

            tkFile.setThumbnailName(fileThumbDownloadUri);
            tkFile.setFileName(fileDownloadUri);
            courseware.setTkFile(tkFile);
        }
    }
}
