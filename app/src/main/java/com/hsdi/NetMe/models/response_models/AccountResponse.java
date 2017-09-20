package com.hsdi.NetMe.models.response_models;

import com.google.gson.annotations.SerializedName;

public class AccountResponse extends BaseResponse{

    @SerializedName("user_id")
    private int userId;
    @SerializedName("countrycode")
    private int countryCode;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("firstname")
    private String firstName;
    @SerializedName("lastname")
    private String lastName;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("avatar_bg")
    private String avatarbg;
    @SerializedName("api_key")
    private String apiKey;


    public int getUserId() {
        return userId;
    }

    public String getUserIdString() {
        return String.valueOf(userId);
    }

    public int getCountryCode() {
        return countryCode;
    }

    public String getCOuntryCodeString() {
        return String.valueOf(countryCode);
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatarUrl() {
        return avatar;
    }

    public String getAvatarBgUrl() {
        return avatarbg;
    }

    public String getApiKey() {
        return apiKey;
    }
}