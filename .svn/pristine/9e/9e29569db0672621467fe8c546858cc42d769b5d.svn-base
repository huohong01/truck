package com.hsdi.NetMe.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.util.DateUtil;

import java.util.Date;

public class Message implements Comparable<Message>, Parcelable{

    @SerializedName("messageId")
    private long messageId;
    @SerializedName("chatId")
    private long chatId;
    @SerializedName("sender")
    private String senderUsername;
    @SerializedName("subject")
    private String subject;
    @SerializedName("message")
    private String message;
    @SerializedName("date")
    private String date;
    @SerializedName("media")
    private String[] media;


    public Message(long messageId) {
        this.messageId = messageId;
        this.chatId = -1;
        this.senderUsername = null;
        this.subject = null;
        this.message = null;
        this.date = null;
        this.media = null;
    }

    public Message(long messageId, long chatId, String senderUsername, String subject, String message, String date, String[] media) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.senderUsername = senderUsername;
        this.subject = subject;
        this.message = message;
        this.date = date;
        this.media = media;
    }

    protected Message(Parcel in) {
        messageId = in.readLong();
        chatId = in.readLong();
        senderUsername = in.readString();
        subject = in.readString();
        message = in.readString();
        date = in.readString();
        media = in.createStringArray();
    }

    public long getId() {
        return messageId;
    }

    public long getChatId() {
        return chatId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String[] getMedia() {
        return media;
    }

    public void setMessage(String message) {
        this.message = message;
    }


//--------------------------------------------------------------------------------------------------


    public boolean hasText() {
        return message != null && !message.isEmpty();
    }

    public boolean mayBeEncrypted() {
        return subject != null && !subject.isEmpty();
    }

    /** Returns true if the {@link Message} maybe encrypted using AES, false otherwise */
    public boolean mayBeAesEncrypted() {
        return !TextUtils.isEmpty(getSubject()) && getSubject().startsWith(NetMeApp.getInstance().getAesTAG());
    }

    /** Returns true if the {@link Message} maybe encrypted using Codetel, false otherwise */
    public boolean mayBeCodetelEncrypted() {
        return !TextUtils.isEmpty(getSubject()) && !getSubject().startsWith(NetMeApp.getInstance().getAesTAG());
    }

    public boolean hasMedia() {
        return media != null && media.length > 0;
    }


//--------------------------------------------------------------------------------------------------


    public boolean isFromContact() {
        return !senderUsername.equalsIgnoreCase(NetMeApp.getCurrentUser());
    }


//--------------------------------------------------------------------------------------------------


    public String getFormattedDate() {
        Date msgDate = DateUtil.getDateFromStringUTC(date);
        return DateUtil.getStringFromDate(msgDate, "EEE, MMM dd");
    }

    public String getFormattedHour() {
        Date msgDate = DateUtil.getDateFromStringUTC(date);
        return DateUtil.getStringFromDate(msgDate, "KK:mm aa");
    }

    public String getFormattedTime() {
        Date msgDate = DateUtil.getDateFromStringUTC(date);
        return DateUtil.getStringFromDate(msgDate, "EEE KK:mm aa");
    }


//--------------------------------------------------------------------------------------------------


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message rhsMsg = (Message) o;

        return messageId == rhsMsg.messageId &&
                chatId == rhsMsg.chatId &&
                message.equals(rhsMsg.message) &&
                subject.equals(rhsMsg.subject) &&
                date.equals(rhsMsg.date);

    }

    @Override
    public int compareTo(@NonNull Message another) {
        return Long.compare(messageId, another.getId());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(messageId);
        parcel.writeLong(chatId);
        parcel.writeString(senderUsername);
        parcel.writeString(subject);
        parcel.writeString(message);
        parcel.writeString(date);
        parcel.writeStringArray(media);
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}