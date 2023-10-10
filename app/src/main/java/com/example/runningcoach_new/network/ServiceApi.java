package com.example.runningcoach_new.network;

import com.example.runningcoach_new.data.LoginData;
import com.example.runningcoach_new.data.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/user/login")
    Call<LoginResponse> userLogin(@Body LoginData data);
}
