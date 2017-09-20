package com.hsdi.NetMe.models.response_models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PinRequestResponse extends BaseResponse {

    @SerializedName("userExists")
    private boolean userExists;

    @SerializedName("code")
    private String pin;

    public boolean userExists() {
        return userExists;
    }

    public String getPin() {
        return pin;
    }
}