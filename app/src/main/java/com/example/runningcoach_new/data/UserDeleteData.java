package com.example.runningcoach_new.data;

import com.google.gson.annotations.SerializedName;

public class UserDeleteData {
    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;

    public UserDeleteData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
