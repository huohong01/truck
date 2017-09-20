package com.hsdi.NetMe.models.response_models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseResponse {
    @SerializedName("success")
    int success;
    @SerializedName("user_message")
    private String errorMessage;
    @SerializedName("error_messages")
    private List<String> errorMessageList;


    public boolean isSuccess() {
        return success == 1;
    }

    public String getErrorMsg() {
        return errorMessage;
    }

    public List<String> getErrorMessageList() {
        return errorMessageList;
    }
}
