package com.hsdi.NetMe.models.response_models;

import android.util.Base64;

import com.google.gson.annotations.SerializedName;

public class AvatarResponse extends BaseResponse{

    @SerializedName("data")
    private String data;

    public String getData() {
        return data;
    }

    public byte[] getByteData() {
        if(data != null && !data.isEmpty()) return Base64.decode(data, Base64.DEFAULT);
        else return new byte[] {};
    }
}
