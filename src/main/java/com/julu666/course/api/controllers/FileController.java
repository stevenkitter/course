package com.julu666.course.api.controllers;

import com.julu666.course.api.exceptions.StorageFileNotFoundException;
import com.julu666.course.api.jpa.TKFile;
import com.julu666.course.api.repositories.FileRepository;

import com.julu666.course.api.response.Response;
import com.julu666.course.api.response.UploadFileResponse;
import com.julu666.course.api.services.FileService;
import com.julu666.course.api.utils.FileNameExt;
import com.julu666.course.api.utils.JWTToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @javax.annotation.Resource(name="fileRepository")
    private FileRepository fileRepository;


    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, String userId) {
        String filename = fileService.store(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().scheme("https")
                .path("/downloadFile/")
                .path(filename)
                .toUriString();
        TKFile tkFile = new TKFile();
        tkFile.setUserId(userId);
        tkFile.setThumbnailName(FileNameExt.getThumbnailPath(filename));
        tkFile.setFileName(filename);
        tkFile.setSourceId("");
        fileRepository.save(tkFile);
        return new UploadFileResponse(tkFile.getFileId(), filename, fileDownloadUri,
                file.getContentType(), FileNameExt.getThumbnailPath(filename), file.getSize());
    }

    @PostMapping("/uploadOneFile")
    public Response<UploadFileResponse> uploadOneFile(@RequestHeader(value="Token") String token, @RequestParam("file") MultipartFile file) {
        String userId = JWTToken.userId(token);
        UploadFileResponse uploadFileResponse = uploadFile(file, userId);
        return new Response<>(200, "", uploadFileResponse);
    }


    @PostMapping("/uploadFiles")
    public Response<List<UploadFileResponse>> uploadFiles(@RequestHeader(value="Token") String authorization, @RequestParam("files") MultipartFile[] files) {
        String userId = JWTToken.userId(authorization);
        List<UploadFileResponse> uploaded = Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file, userId))
                .collect(Collectors.toList());
        return new Response<>(200, "", uploaded);
    }

    @GetMapping("/downloadFile/**")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request) {
        String fileName = getExtractPath(request);
        System.out.printf("fileName %s", fileName);
        Resource resource = fileService.loadAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    private String getExtractPath(final HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }
}
