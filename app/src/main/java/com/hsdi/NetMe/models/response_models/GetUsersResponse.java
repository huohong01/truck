package com.hsdi.NetMe.models.response_models;

import com.google.gson.annotations.SerializedName;
import com.hsdi.NetMe.models.Participant;

import java.util.List;

public class GetUsersResponse extends BaseResponse{

    @SerializedName("TotalUsers")
    private int totalUsers;

    @SerializedName("Users")
    private List<Participant> users;

    public int getTotalUsers() {
        return totalUsers;
    }

    public List<Participant> getUsers() {
        return users;
    }
}
