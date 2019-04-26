package com.julu666.course.api.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SessionResponse {
    private Integer errCode;
    @SerializedName("errmsg") private String errMsg;
    @SerializedName("openid") private String openId;
    @SerializedName("session_key") private String sessionKey;
    @SerializedName("unionid") private String unionid;
}
