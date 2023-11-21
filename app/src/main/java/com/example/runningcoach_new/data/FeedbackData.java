package com.example.runningcoach_new.data;

import com.google.gson.annotations.SerializedName;

public class FeedbackData {

    @SerializedName("datetime")
    String datetime;

    @SerializedName("password")
    String password;

    public String getText() {
        return datetime;
    }
    public void setText(String datetime) {
        this.datetime = datetime;
    }

}
