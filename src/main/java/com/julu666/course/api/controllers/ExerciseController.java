package com.julu666.course.api.controllers;


import com.julu666.course.api.jpa.*;
import com.julu666.course.api.repositories.*;
import com.julu666.course.api.requests.courseware.CoursewareAddRequest;
import com.julu666.course.api.requests.exercise.AddExerciseRequest;
import com.julu666.course.api.requests.exercise.BuyExerciseRequest;
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
import java.util.Optional;

@RestController
@RequestMapping(value = "/exercise")
public class ExerciseController {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Resource(name = "fileRepository")
    private FileRepository fileRepository;

    @Resource(name = "userExerciseRepository")
    private UserExerciseRepository userExerciseRepository;

    @Resource(name = "userRepository")
    private UserRepository userRepository;

    @Transactional
    @PostMapping("/add")
    public Response<String> addExercise(@RequestHeader(value="Token") String authorization, @RequestBody AddExerciseRequest addExerciseRequest) {
        String userId = JWTToken.userId(authorization);
        Exercise exercise = new Exercise();
        exercise.setUserId(userId);
        exercise.setTitle(addExerciseRequest.getTitle());
        exercise.setPrice(addExerciseRequest.getPrice());
        exerciseRepository.save(exercise);

        Optional<TKFile> optionalTKFile = fileRepository.findByFileId(addExerciseRequest.getFileId());
        if (!optionalTKFile.isPresent()) {
            return Wrapper.failActionResp("无法创建","");
        }
        TKFile tkFile = optionalTKFile.get();
        tkFile.setSourceId(exercise.getExerciseId());
        fileRepository.save(tkFile);
        return Wrapper.okActionResp("新建成功","");
    }

    @PostMapping("/buy")
    public Response<String> buyExercise(@RequestHeader(value="Token") String authorization, @RequestBody BuyExerciseRequest buyExerciseRequest) {
        String userId = JWTToken.userId(authorization);

        List<UserExercise> userExerciseOptional = userExerciseRepository.findByUserId(userId);
        if (userExerciseOptional.size() > 0) {
            for(UserExercise ue : userExerciseOptional) {
                if (ue.getExerciseId().equals(buyExerciseRequest.getExerciseId())) {
                    return Wrapper.failActionResp("无法重复购买","");
                }
            }
        }
        UserExercise userExercise = new UserExercise();
        userExercise.setExerciseId(buyExerciseRequest.getExerciseId());
        userExercise.setUserId(userId);
        userExerciseRepository.save(userExercise);
        return Wrapper.okActionResp("购买成功","");
    }


    @GetMapping("/all")
    public Response<List<Exercise>> allExercise(@RequestHeader(value="Token") String authorization, @RequestParam(value = "page") Integer page) {
        String userId = JWTToken.userId(authorization);
        Page<Exercise> pageCourse = exerciseRepository.findAllTop10(PageRequest.of(page, 10, Sort.Direction.DESC, "created_at"));


        appendUrl(pageCourse.getContent());

        return new Response<>(200, "", pageCourse.getContent());
    }

    @GetMapping("/my")
    public Response<List<Exercise>> myExercise(@RequestHeader(value="Token") String authorization, @RequestParam(value = "page") Integer page) {
        String userId = JWTToken.userId(authorization);
        Page<Exercise> pageCourse = exerciseRepository.findAllByUserId(userId, PageRequest.of(page, 10, Sort.Direction.DESC, "created_at"));
        appendUrl(pageCourse.getContent());

        return new Response<>(200, "", pageCourse.getContent());
    }

    @Modifying
    @Transactional
    @DeleteMapping("/delete")
    public Response<String> deleteMyCourseware(@RequestHeader(value="Token") String authorization, @RequestParam(value = "exerciseId") String exerciseId) {
        String userId = JWTToken.userId(authorization);

        if (!exerciseRepository.findByExerciseId(exerciseId).isPresent()) {
            return Wrapper.failActionResp("无相关数据","");
        }

        Exercise exercise = exerciseRepository.findByExerciseId(exerciseId).get();


        if (exerciseRepository.deleteByExerciseIdAndUserId(exerciseId, userId) == 0) {
            return Wrapper.failActionResp("无法删除","");
        }

        if (exercise.getTkFile() != null) {
            Integer aff = fileRepository.deleteByFileId(exercise.getTkFile().getFileId());
            if (aff == 0) {
                return Wrapper.failActionResp("无法删除","");
            }
        }

        return Wrapper.okActionResp("删除成功", "");
    }


    @GetMapping("/buyList")
    public Response<List<Exercise>> buyList(@RequestHeader(value="Token") String authorization) {
        String userId = JWTToken.userId(authorization);
        Optional<User> user = userRepository.findByUserId(userId);
        if (!user.isPresent()) {
            return new Response<>(400, "无相关用户", null);
        }
        User user1 = user.get();
        appendUrl(user1.getExercises());
        return new Response<>(200, "", user1.getExercises());
    }


    private String downloadUri(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(filename)
                .toUriString();
    }

    private void appendUrl(List<Exercise> pageCourse) {
        for (Exercise exercise : pageCourse) {
            TKFile tkFile = exercise.getTkFile();

            if (tkFile == null) {
                continue;
            }
            changeFileUrl(tkFile);

            exercise.setTkFile(tkFile);
        }
    }

    private void changeFileUrl(TKFile tkFile) {
        String fileThumbDownloadUri = downloadUri(tkFile.getThumbnailName());
        String fileDownloadUri = downloadUri(tkFile.getFileName());
        tkFile.setThumbnailName(fileThumbDownloadUri);
        tkFile.setFileName(fileDownloadUri);
    }

}
