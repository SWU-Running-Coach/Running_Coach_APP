package com.example.runningcoach_new.data;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("message")
    private String message;


    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
