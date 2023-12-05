package com.example.runningcoach_new.network;

import com.example.runningcoach_new.data.FeedbackData;
import com.example.runningcoach_new.data.LoginData;
import com.example.runningcoach_new.data.LoginResponse;
import com.example.runningcoach_new.data.RegisterData;
import com.example.runningcoach_new.data.RegisterResponse;
import com.example.runningcoach_new.data.UserDeleteData;
import com.example.runningcoach_new.data.UserDeleteResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/users/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/users/join")
    Call<RegisterResponse> userRegister(@Body RegisterData data);

    @POST("/user/user_delete")
    Call<UserDeleteResponse> userDelete(@Body UserDeleteData data);

    @POST("/running")
    Call<FeedbackData> postData(@Body FeedbackData fbdata);

}
