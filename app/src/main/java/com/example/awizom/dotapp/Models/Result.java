package com.example.awizom.dotapp.Models;

import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("Status")
    private Boolean Status;

    @SerializedName("Message")
    private String Message;


    public Result(String Message, Boolean Status) {

        this.Message = Message;
        this.Status = Status;

    }

    public Boolean getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }


}
