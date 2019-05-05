package com.julu666.course.api.response;

public class Wrapper {

    public static Response<String> okActionResp(String msg, String data) {
        return new Response<>(200, msg, data);
    }

    public static Response<String> failActionResp(String msg, String data) {
        return new Response<>(200, msg, data);
    }


}
