package com.julu666.course.api.http;

import com.julu666.course.api.response.SessionResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WxService {
    @GET("sns/jscode2session")
    Call<SessionResponse> code2session(@Query("js_code") String code,
                                       @Query("appid") String appId,
                                       @Query("secret") String secret,
                                       @Query("grant_type") String grant_type);
}
