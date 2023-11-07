package com.example.runningcoach_new.data;

import com.google.gson.annotations.SerializedName;

public class RegisterData {
    @SerializedName("email")
    private String email;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("password")
    private String password;

    public RegisterData(String email, String nickname,String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
}
