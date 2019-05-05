package com.julu666.course.api.utils;



public class FileNameExt {


    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
    public static String getThumbnailPath(String filename) {
        String noExt = getFileNameNoEx(filename);
        return "/thumbnail/" + noExt + ".png";
    }
}
