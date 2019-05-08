package com.julu666.course.api.services;

import com.julu666.course.api.constants.Global;
import com.julu666.course.api.constants.WXUrl;
import com.julu666.course.api.http.WxService;
import com.julu666.course.api.jpa.User;
import com.julu666.course.api.repositories.UserRepository;
import com.julu666.course.api.response.SessionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.logging.Logger;


/*
* SessionService
* 微信session服务
* */
@Service
public class SessionService {

    @Autowired
    private UserRepository userRepository;

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
        try {
            Response<SessionResponse> response = call.execute();
            SessionResponse body = response.body();
            User user = userRepository.findByWxOpenId(body.getOpenId()).orElse(new User());
            user.setWxOpenId(body.getOpenId());
            user.setWxSessionKey(body.getSessionKey());
            user.setWxUnionid(body.getUnionid());
            userRepository.save(user);
            return user.getUserId();
        } catch (IOException ex) {
            return "";
        }
    }
}
