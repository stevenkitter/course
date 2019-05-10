package com.julu666.course.api.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class UploadFileResponse implements Serializable {
    private String fileId;
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private String thumbnailName;
    private long size;

    public UploadFileResponse(String fileId, String fileName, String fileDownloadUri, String fileType,String thumbnailName, long size) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.thumbnailName = thumbnailName;
        this.size = size;
    }
}
