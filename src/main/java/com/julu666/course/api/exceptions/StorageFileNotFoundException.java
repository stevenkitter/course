package com.julu666.course.api.exceptions;

public class StorageFileNotFoundException extends StorageException {
    public StorageFileNotFoundException(String msg) {
        super(msg);
    }
    public StorageFileNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
