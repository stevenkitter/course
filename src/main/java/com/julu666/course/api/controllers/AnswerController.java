package com.julu666.course.api.controllers;


import com.julu666.course.api.jpa.Answer;
import com.julu666.course.api.jpa.TKFile;
import com.julu666.course.api.repositories.AnswerRepository;
import com.julu666.course.api.repositories.FileRepository;
import com.julu666.course.api.requests.answer.SaveAnswerRequest;
import com.julu666.course.api.response.Response;
import com.julu666.course.api.response.Wrapper;
import com.julu666.course.api.utils.JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/answer")
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Resource
    private FileRepository fileRepository;

    @GetMapping("/my")
    public Response<List<Answer>> myAnswers(@RequestHeader(value = "Token") String token, @RequestParam(value = "page") Integer page) {
        String userId = JWTToken.userId(token);
        Page<Answer> pages = answerRepository.findByUserIdTop(userId, PageRequest.of(page, 10, Sort.Direction.DESC, "created_at"));
        appendUrl(pages.getContent());
        return new Response<>(200, "", pages.getContent());
    }

    private void appendUrl(List<Answer> pageCourse) {
        for (Answer answer : pageCourse) {
            TKFile[] tkFiles = answer.getTkFiles();
            for (TKFile f : tkFiles) {
                if (f == null) {
                    continue;
                }
                String fileDownloadUri = downloadUri(f.getFileName());
                String fileThumbDownloadUri = downloadUri(f.getThumbnailName());
                f.setThumbnailName(fileThumbDownloadUri);
                f.setFileName(fileDownloadUri);
            }
            answer.setTkFiles(tkFiles);
        }
    }
    private String downloadUri(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(filename)
                .toUriString();
    }

    @PostMapping("/add")
    public Response<String> saveAnswer(@RequestHeader(value = "Token") String token, @RequestBody SaveAnswerRequest saveAnswerRequest) {
        String userId =JWTToken.userId(token);
        Answer answer = new Answer();
        answer.setContent(saveAnswerRequest.getContent());
        answer.setTitle(saveAnswerRequest.getTitle());
        answer.setUserId(userId);
        answerRepository.save(answer);

        for (String fileId : saveAnswerRequest.getFileIds()) {
            if (fileRepository.findByFileId(fileId).isPresent()) {
                TKFile tkFile = fileRepository.findByFileId(fileId).get();
                tkFile.setSourceId(answer.getAnswerId());
                fileRepository.save(tkFile);
            } else {
                return Wrapper.failActionResp("数据错误","");
            }

        }
        return Wrapper.okActionResp("新建成功", "");
    }
}
