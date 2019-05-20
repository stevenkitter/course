package com.julu666.course.api.controllers;


import com.julu666.course.api.jpa.Answer;
import com.julu666.course.api.jpa.Comment;
import com.julu666.course.api.jpa.TKFile;
import com.julu666.course.api.repositories.AnswerRepository;
import com.julu666.course.api.repositories.CommentRepository;
import com.julu666.course.api.repositories.FileRepository;
import com.julu666.course.api.requests.answer.AddCommentRequest;
import com.julu666.course.api.requests.answer.SaveAnswerRequest;
import com.julu666.course.api.response.MyAnswerResponse;
import com.julu666.course.api.response.Response;
import com.julu666.course.api.response.Wrapper;
import com.julu666.course.api.utils.DateOperation;
import com.julu666.course.api.utils.JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/answer")
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Resource
    private FileRepository fileRepository;

    @Resource
    private CommentRepository commentRepository;

    @GetMapping("/my")
    public Response<MyAnswerResponse> myAnswers(@RequestHeader(value = "Token") String token, @RequestParam(value = "page") Integer page) throws ParseException {
        String userId = JWTToken.userId(token);
        Page<Answer> pages = answerRepository.findByUserIdTop(userId, PageRequest.of(page, 10, Sort.Direction.DESC, "created_at"));
        appendUrl(pages.getContent());
        MyAnswerResponse myAnswerResponse = new MyAnswerResponse();
        myAnswerResponse.setCount(answerRepository.count());
        myAnswerResponse.setAnswers(pages.getContent());
        return new Response<>(200, "", myAnswerResponse);
    }

    private void appendUrl(List<Answer> pageCourse) throws ParseException {
        for (Answer answer : pageCourse) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
            Date date = format.parse(answer.getCreated_at().toString());
            answer.setCreateTime(DateOperation.format(date));
            List<TKFile> tkFiles = answer.getTkFiles();
            if (tkFiles.size() == 0) {
                continue;
            }
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
        return ServletUriComponentsBuilder.fromCurrentContextPath().scheme("https")
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

    @Transactional
    @DeleteMapping("/delete")
    public Response<String> deleteAnswer(@RequestHeader(value = "Token") String token, @RequestParam(value = "answerId") String answerId) {
        String userId = JWTToken.userId(token);
        Optional<Answer> answer = answerRepository.findByAnswerIdAndUserId(answerId, userId);
        if (!answer.isPresent()) {
            return Wrapper.failActionResp("无相关数据","");
        }
        Answer answer1 = answer.get();
        answerRepository.deleteById(answer1.getId());

        for (TKFile tkFile : answer1.getTkFiles()) {
            if (tkFile != null) {
                fileRepository.deleteByFileId(tkFile.getFileId());
            }
        }
        return Wrapper.okActionResp("删除成功","");

    }

    @PostMapping("/comment")
    public Response<String> comment(@RequestHeader(value = "Token") String token, @RequestBody AddCommentRequest addCommentRequest){
        String userId = JWTToken.userId(token);
        Comment comment = new Comment();
        comment.setAnswerId(addCommentRequest.getAnswerId());
        comment.setContent(addCommentRequest.getContent());
        comment.setUserId(userId);
        commentRepository.save(comment);
        return Wrapper.okActionResp("评论成功","");
    }

    @GetMapping("/commentList")
    public Response<List<Comment>> getCommentList(@RequestHeader(value = "Token") String token, @RequestParam(value = "page") Integer page, @RequestParam(value = "answerId") String answerId) {
        Page<Comment> comments = commentRepository.findByAnswerId(answerId, PageRequest.of(page, 10, Sort.Direction.DESC, "created_at"));
        return new Response<>(200,"", comments.getContent());
    }

    @GetMapping("/all")
    public Response<List<Answer>> allAnswer(@RequestHeader(value = "Token") String token, @RequestParam("page") Integer page) throws ParseException {
        Page<Answer> answers = answerRepository.findAllTop10(PageRequest.of(page, 10, Sort.Direction.DESC, "created_at"));
        appendUrl(answers.getContent());
        return new Response<>(200, "", answers.getContent());
    }
}
