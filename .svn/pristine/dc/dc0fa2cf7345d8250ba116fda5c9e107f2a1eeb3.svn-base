package com.hsdi.NetMe.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.util.PhoneUtils;

import java.util.Locale;

public class Participant implements Parcelable {
    public static final String STATUS_STARTED = "started";
    public static final String STATUS_ENDED = "ended";
    public static final String STAUS_ACTIVE = "active";
    public static final String STATUS_INVITED = "invited";
    public static final String STATUS_UNINVITED = "uninvited";
    public static final String STATUS_DECLINED = "declined";

    @SerializedName("creator")
    private String creator;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("unread")
    private int unread;
    @SerializedName("email")
    private String email;
    @SerializedName("avatar")
    private String avatarUrl;
    @SerializedName("firstname")
    private String firstName;
    @SerializedName("lastname")
    private String lastName;
    @SerializedName("username")
    private String username;
    @SerializedName("started")
    private String startedDate;
    @SerializedName("ended")
    private String ended;
    @SerializedName("status")
    private String status;


//--------------------------------------------------------------------------------------------------


    public Participant(Boolean creator, String userId, String email, String avatarUrl, String firstName, String lastName, String username) {
        this.creator = creator ? "1" : "0";
        this.avatarUrl = avatarUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;

        try {this.userId = Integer.parseInt(userId);}
        catch (Exception e) {this.userId = -1;}
    }

    public Participant(Parcel parcel) {
        creator = parcel.readString();
        userId = parcel.readInt();
        unread = parcel.readInt();
        email = parcel.readString();
        avatarUrl = parcel.readString();
        firstName = parcel.readString();
        lastName = parcel.readString();
        username = parcel.readString();
        startedDate = parcel.readString();
        ended = parcel.readString();
        status = parcel.readString();
    }

    public boolean isCreator() {
        return creator != null && creator.equals("1");
    }

    public int getUnread() {
        return unread;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getStartedDate() {
        return startedDate;
    }

    public String getEnded() {
        return ended;
    }

    public String getStatus() {
        return status;
    }


//--------------------------------------------------------------------------------------------------


    public boolean isCurrentUser()  {
        return username != null && !username.isEmpty() && username.equals(NetMeApp.getCurrentUser());
    }

    public boolean hasValidAvatarUrl(Context context) {
        return getAvatarUrl() != null && !getAvatarUrl().contains(context.getString(R.string.default_avatar));
    }

    public String getFormattedUsername(Context context) {
        String formattedUsername = PhoneUtils.formatPhoneNumber(context, username);

        if(formattedUsername != null) return formattedUsername;

        return username;
    }

    public String getInitials() {
        // start with blank initials
        String firstLetter = "";
        String lastLetter = "";

        // get the first letter if the person has a first name
        if(!TextUtils.isEmpty(firstName)) {
            firstLetter = firstName.substring(0, 1).toUpperCase(Locale.getDefault());
        }

        // get the last letter if the person has a last name
        if(!TextUtils.isEmpty(lastName)) {
            lastLetter = lastName.substring(0, 1).toUpperCase(Locale.getDefault());
        }

        // return the initials
        return firstLetter + lastLetter;
    }


//--------------------------------------------------------------------------------------------------


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(creator);
        parcel.writeInt(userId);
        parcel.writeInt(unread);
        parcel.writeString(email);
        parcel.writeString(avatarUrl);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(username);
        parcel.writeString(startedDate);
        parcel.writeString(ended);
        parcel.writeString(status);
    }

    public static final Creator<Participant> CREATOR = new Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };

    @Override
    public String toString() {
        return "Participant{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}