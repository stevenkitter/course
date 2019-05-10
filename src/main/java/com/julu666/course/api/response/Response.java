package com.julu666.course.api.response;

import lombok.Data;

import java.io.Serializable;

/*
* Response
* 封装返回值
* code 返回码
* msg 消息
* data 消费数据
*  */
@Data
public class Response<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public Response(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }



}




