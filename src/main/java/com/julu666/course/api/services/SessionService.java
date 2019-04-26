package com.julu666.course.api.services;

import com.julu666.course.api.constants.Global;
import com.julu666.course.api.constants.WXUrl;
import com.julu666.course.api.http.WxService;
import com.julu666.course.api.response.SessionResponse;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.logging.Logger;


/*
* SessionService
* 微信session服务
* */
@Service
public class SessionService {
    public String code2session(String code) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WXUrl.WEIXIN_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WxService wxService = retrofit.create(WxService.class);
        Call<SessionResponse> call = wxService.code2session(code,
                Global.MpAppid,
                Global.MpSecret,
                "authorization_code");

        call.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, Response<SessionResponse> response) {
                SessionResponse body = response.body();
                System.out.printf("onResponse %s", body);
            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable throwable) {
                System.out.printf("onFailure %s", throwable.getMessage());
            }
        });
        return "";
    }
}
