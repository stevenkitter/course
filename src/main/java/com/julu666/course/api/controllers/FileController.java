package com.julu666.course.api.controllers;

import com.julu666.course.api.exceptions.StorageFileNotFoundException;
import com.julu666.course.api.response.Response;
import com.julu666.course.api.response.UploadFileResponse;
import com.julu666.course.api.services.FileService;
import com.sun.deploy.uitoolkit.impl.awt.AWTFrameWindow;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.api.awt.AWTFrameGrab;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
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
import retrofit2.http.Multipart;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Array;
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

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String filename = fileService.store(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(filename)
                .toUriString();
        if (file.getContentType().contains("video")) {



        }
        return new UploadFileResponse(filename, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    public void saveThumbnail(MultipartFile file, String filename, String path ) {
        int frameNumber = 10;
        File fi = null;
        try {
            fi = new File(filename + path);
            file.transferTo(fi);
        } catch (IOException | NullPointerException ex) {
            logger.info("");
        }

        FileChannelWrapper in = null;
        try {
            in = NIOUtils.readableChannel(fi);
            AWTFrameGrab fg = AWTFrameGrab.createAWTFrameGrab(in);

        } catch ( IOException | JCodecException ex) {
            logger.error("");
        } finally {
            NIOUtils.closeQuietly(in);
        }

    }

    @PostMapping("/uploadFiles")
    public Response<List<UploadFileResponse>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        List<UploadFileResponse> uploaded = Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
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
