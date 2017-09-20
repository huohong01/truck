package com.hsdi.NetMe.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.util.DateUtil;

import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")
public class Chat implements Comparable<Chat>, Parcelable {

    @SerializedName("chatId")
    private long chatId;
    @SerializedName("Messages")
    private List<Message> messages;
    @SerializedName("Participants")
    private List<Participant> participants;
    @SerializedName("lastMessageDate")
    private String lastMessageDate;
    @SerializedName("totalMessages")
    private int totalMessage;
    @SerializedName("totalParticipants")
    private int totalParticipants;
    @SerializedName("totalUnreadMessages")
    private int totalUnreadMessages;

    private String chatNickname;


    public Chat(long chatId, List<Message> messages, List<Participant> participants, long lastMessageMillis, int totalMessage, int totalParticipants, int totalUnreadMessages, String chatNickname) {
        this.chatId = chatId;
        this.messages = messages;
        this.participants = participants;
        this.lastMessageDate = DateUtil.getUTCStringFromMillis(lastMessageMillis);
        this.totalMessage = totalMessage;
        this.totalParticipants = totalParticipants;
        this.totalUnreadMessages = totalUnreadMessages;
        this.chatNickname = chatNickname;
    }

    protected Chat(Parcel in) {
        chatId = in.readLong();
        messages = in.createTypedArrayList(Message.CREATOR);
        participants = in.createTypedArrayList(Participant.CREATOR);
        lastMessageDate = in.readString();
        totalMessage = in.readInt();
        totalParticipants = in.readInt();
        totalUnreadMessages = in.readInt();
    }

    public long getChatId() {
        return chatId;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public String getLastMessageDate() {
        return lastMessageDate;
    }

    public int getTotalMessage() {
        return totalMessage;
    }

    public int getTotalParticipants() {
        return totalParticipants;
    }

    public int getTotalUnreadMessages() {
        return totalUnreadMessages;
    }


//--------------------------------------------------------------------------------------------------


    public boolean isGroupChat() {
        return participants.size() > 2;
    }

    public boolean hasNoOtherUsers() {
        return participants.size() <= 1;
    }

    @Nullable
    public Participant getFirstOtherParticipant(Context context) {
        String currentUser = NetMeApp.getCurrentUser();

        //return the first participant which is not the current user
        if(participants != null) {
            for (Participant participant : participants) {
                if (participant.getUsername() != null && !participant.getUsername().equals(currentUser)) {
                    return participant;
                }
            }
        }

        return null;
    }

    public Message getLatestMessage() {
        return messages.size() > 0 ? messages.get(0) : null;
    }

    public String getFormattedLatestDate() {
        Date msgDate = DateUtil.getDateFromStringUTC(lastMessageDate);
        return DateUtil.getStringFromDate(msgDate, "EEE, MMM dd");
    }

    public String getFormattedLatestHour() {
        Date msgDate = DateUtil.getDateFromStringUTC(lastMessageDate);
        return DateUtil.getStringFromDate(msgDate, "hh:mm aa");
    }

    public long getLatestMessageSeconds() {
        Date msgDate = DateUtil.getDateFromStringUTC(lastMessageDate);
        return msgDate.getTime() / 1000;
    }

    public long getLatestMessageMillis() {
        Date msgDate = DateUtil.getDateFromStringUTC(lastMessageDate);
        return msgDate.getTime();
    }

    public String getTitle(Context context) {
        String currentUser = NetMeApp.getCurrentUser();

        // if the chat has a nickname, use that
        if(!TextUtils.isEmpty(chatNickname)) return chatNickname;
        // if participants are null or there is less then 2 user's show a general "Deleted User" title
        else if(participants == null || participants.size() < 2) return context.getString(R.string.deleted_user_name);
        // if there are only 2 user's get the other participants name for the title
        else if(participants.size() == 2) {
            if(!participants.get(0).getUsername().equals(currentUser)) return participants.get(0).getFullName();
            else return participants.get(1).getFullName();

        }
        // if there are multiple people, make the title all their first names
        else {
            StringBuilder titleBuilder = new StringBuilder();

            for(Participant participant : participants) {
                // ignore if this participant is the current user
                if(currentUser.equals(participant.getUsername())) continue;

                // if this this not the first user name, add a comma and space before the next name
                if(titleBuilder.length() != 0) titleBuilder.append(", ");

                // add the user first name
                titleBuilder.append(participant.getFirstName());
            }

            return titleBuilder.toString();
        }
    }


//--------------------------------------------------------------------------------------------------


    @Override
    public int compareTo(@NonNull Chat another) {
        return another.getLastMessageDate().compareTo(lastMessageDate);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Chat && chatId == ((Chat) o).chatId;
    }

    /**
     * Checks if this and the other chat have the same content
     * @param other    the contact to compare to this
     * @return true if the content is the same, false otherwise
     * */
    public boolean contentSameAs(Chat other) {
        return this == other || !(other == null ||
                getClass() != other.getClass()) &&
                chatId == other.chatId &&
                totalMessage == other.totalMessage &&
                totalParticipants == other.totalParticipants &&
                totalUnreadMessages == other.totalUnreadMessages &&
                messages.equals(other.messages) &&
                participants.equals(other.participants) &&
                lastMessageDate.equals(other.lastMessageDate);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(chatId);
        parcel.writeTypedList(messages);
        parcel.writeTypedList(participants);
        parcel.writeString(lastMessageDate);
        parcel.writeInt(totalMessage);
        parcel.writeInt(totalParticipants);
        parcel.writeInt(totalUnreadMessages);
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };
}
