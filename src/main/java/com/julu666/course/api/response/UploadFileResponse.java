package com.julu666.course.api.response;

import lombok.Data;

@Data
public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private String thumbnailName;
    private long size;

    public UploadFileResponse(String fileName, String fileDownloadUri, String fileType,String thumbnailName, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.thumbnailName = thumbnailName;
        this.size = size;
    }
}
