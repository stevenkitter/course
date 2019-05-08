package com.julu666.course.api.requests.user;

import lombok.Data;

@Data
public class SaveUserInfoRequest {
    private String avatarUrl;
    private String city;
    private String country;
    private Integer gender;
    private String language;
    private String nickName;
    private String province;
}
