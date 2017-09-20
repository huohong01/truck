package com.hsdi.NetMe.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.util.DateUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManagedMessage implements Comparable<ManagedMessage>, Parcelable{

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_RECEIVED, TYPE_LOAD, TYPE_SENT})
    @interface Type{}

    /** When a contact sends a message */
    public static final int TYPE_RECEIVED = 0;
    /** When a message from a chat is being loaded */
    public static final int TYPE_LOAD = 1;
    /** When this user sends a message ONLY */
    public static final int TYPE_SENT = 2;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_NONE, STATUS_FAILED, STATUS_IN_PROGRESS, STATUS_READY})
    @interface Status {}

    /** The message doesn't have this item */
    public static final int STATUS_NONE = 0;
    /** The message failed to load completely or failed to send */
    public static final int STATUS_FAILED = 1;
    /** The message is being loaded or is being sent */
    public static final int STATUS_IN_PROGRESS = 2;
    /** The message has finished successfully sending or completely loading */
    public static final int STATUS_READY = 3;

    private final String AESTag;

    private Message message             = null;
    private long id                     = -1;
    private String decryptedText        = null;
    private List<Media> decryptedMedia  = new ArrayList<>();
    private final int type;
    private int textStatus;
    private int mediaStatus;

    public ManagedMessage(long managedId, Message message, String AESTag) {
        this.id = managedId;
        this.type = TYPE_RECEIVED;
        this.message = message;
        this.AESTag = AESTag;
    }

    public ManagedMessage(long managedId, Message message, @Type int type, @Status int textStatus, @Status int mediaStatus, String AESTag) {
        this.id = managedId;
        this.message = message;
        this.type = type;
        this.textStatus = textStatus;
        this.mediaStatus = mediaStatus;
        this.AESTag = AESTag;
    }

    public ManagedMessage(Message message, @Status int textStatus, @Status int mediaStatus, String AESTag) {
        this.message = message;
        this.type = TYPE_LOAD;
        this.textStatus = textStatus;
        this.mediaStatus = mediaStatus;
        this.AESTag = AESTag;
    }

    public ManagedMessage(long id, String decryptedText, List<Media> decryptedMedia, @Type int type, @Status int textStatus, @Status int mediaStatus, String AESTag) {
        this.id = id;
        this.decryptedText = decryptedText;
        this.decryptedMedia = decryptedMedia;
        this.type = type;
        this.textStatus = textStatus;
        this.mediaStatus = mediaStatus;
        this.AESTag = AESTag;
    }

    public ManagedMessage(Parcel parcel) {
        AESTag = parcel.readString();
        message = parcel.readParcelable(Message.class.getClassLoader());
        decryptedText = parcel.readString();
        parcel.readList(decryptedMedia, Media.class.getClassLoader());
        type = parcel.readInt();
        textStatus = parcel.readInt();
        mediaStatus = parcel.readInt();
    }

//--------------------------------------------------------------------------------------------------


    /** Returns the managed message {@link Type} */
    @Type
    public int getType() {
        return type;
    }

    /** Returns the {@link Status} for the text part of this managed message */
    @Status
    public int getTextStatus() {
        return textStatus;
    }

    /**
     * Updates the {@link Status} for the text part of this managed message unless the passed
     * status is {@code STATUS_NONE}, in which case, the current type is kept.
     * @param textStatus    the new status for the text part of the managed message
     * @return this instance of the managed message
     * */
    public ManagedMessage setTextStatus(@Status int textStatus) {
        if(textStatus != STATUS_NONE) this.textStatus = textStatus;
        return this;
    }

    /** Returns the {@link Status} for the media pert of this managed messags */
    @Status
    public int getMediaStatus() {
        return mediaStatus;
    }

    /**
     * Updates the {@link Status} for the media part of this managed message unless the passed
     * status is {@code STATUS_NONE}, in which case, the current type is kept.
     * @param mediaStatus    the new status for the text part of the managed message
     * @return this instance of the managed message
     * */
    public ManagedMessage setMediaStatus(@Status int mediaStatus) {
        if(mediaStatus != STATUS_NONE) this.mediaStatus = mediaStatus;
        return this;
    }

    /**
     * Returns the {@link Status} of the managed message as a whole.
     * If any part of the message is marked as {@code STATUS_FAILED}, the whole message is
     * considered to have failed.
     * If any part of the message is marked as (@code STATIS_IN_PROGRESS), the whole message
     * is considered to be in progress.
     * In any other case the message is considered ready.
     * @return the {@link Status} of the whole managed message
     * */
    @Status
    public int getOverallStatus() {
        if(textStatus == STATUS_FAILED || mediaStatus == STATUS_FAILED) return STATUS_FAILED;
        else if (textStatus == STATUS_IN_PROGRESS || mediaStatus == STATUS_IN_PROGRESS) return STATUS_IN_PROGRESS;
        else return STATUS_READY;
    }

    /**
     * Sets the text after it has been decrypted in a local variable
     * @return this instance of the managed message
     * */
    public ManagedMessage setDecryptedText(String decryptedMessage) {
        this.decryptedText = decryptedMessage;
        return this;
    }

    /** Gets the decrypted value of the text */
    public String getDecryptedText() {
        return decryptedText;
    }

    /**
     * Returns {@code TRUE} if this message has a valid value for the
     * decrypted text, {@code FALSE} otherwise.
     * */
    public boolean hasDecryptedText() {
        return decryptedText != null && !decryptedText.isEmpty();
    }

    /**
     * Gets all the decrypted {@link Media} objects for this managed message
     * @return a list of {@link Media} objects
     * */
    public List<Media> getDecryptedMedia() {
        return decryptedMedia;
    }

    /**
     * Returns {@code TRUE} if this managed message has any decrypted media,
     * {@code FALSE} otherwise.
     * */
    public boolean hasDecryptedMedia() {
        return decryptedMedia != null && decryptedMedia.size() > 0;
    }

    /**
     * Adds media which has already been decrypted decrypted to the local variable
     * @param decryptedMedia    a list of decrypted {@link Media} objects
     * @return this instance of the managed message
     * */
    public ManagedMessage addDecryptedMedia(List<Media> decryptedMedia) {
        this.decryptedMedia.addAll(decryptedMedia);
        return this;
    }

    /**
     * Adds media which has already been decrypted decrypted to the local variable
     * @param decryptedMedia    a single decrypted {@link Media} object
     * @return this instance of the managed message
     * */
    public ManagedMessage addDecryptedMedia(Media decryptedMedia) {
        this.decryptedMedia.add(decryptedMedia);
        return this;
    }

    /**
     * Gets the id for this managed message
     * *Note: this id may not be the same and the message id from the backend message
     * @return the id
     * */
    public long getId() {
        return id;
    }

    /**
     * Updates the managed message id
     * @param tempMsgId    the new managed message id
     * @return this instance of the managed message
     * */
    public ManagedMessage setId(long tempMsgId) {
        id = tempMsgId;
        return this;
    }

    /**
     * Gets the original message from the backend
     * @return the backend {@link Message} object
     * */
    public Message getMessage() {
        return message;
    }

    /**
     * Updates the message received from the backend
     * @param message    the backend {@link Message} object
     * @return this instance of the managed message
     * */
    public ManagedMessage setMessage(Message message) {
        this.message = message;
        return this;
    }

    /**
     * Updates the message received from the backend and resets the managed message id
     * @param message    the new backend {@link Message} object
     * @return this instance of the managed message
     * */
    public ManagedMessage updateWithMessage(Message message) {
        this.id = -1;
        this.message = message;
        return this;
    }


//--------------------------------------------------------------------------------------------------

    /** Returns the id of the{@link Message}, if the{@link Message} exists, -1 otherwise */
    public long getMessageId() {
        if(message != null) return message.getId();
        else return -1;
    }

    /** Returns the id for the chat of a{@link Message}, if the{@link Message} exists, returns the {@link ManagedMessage} chat id otherwise */
    public long getChatId() {
        if(message != null) return message.getChatId();
        else return -1;
    }

    /** Returns the username for the person who sent the {@link Message} (used as the indexer when decrypting), if the{@link Message} exists, null otherwise */
    @Nullable
    public String getSenderUsername() {
        if(message != null) return message.getSenderUsername();
        else return null;
    }

    /** Returns the subject of the{@link Message} (used as the salt when decrypting), if the{@link Message} exists, null otherwise */
    @Nullable
    public String getSubject() {
        if(message != null) return message.getSubject();
        else return null;
    }

    /** Returns the original{@link Message} text (usually encrypted), null otherwise */
    @Nullable
    public String getOriginalMessage() {
        if(message != null) return message.getMessage();
        else return null;
    }

    /** Sets the{@link Message} */
    public void setOriginalMessage(String message) {
        this.message.setMessage(message);
    }

    /** Returns the{@link Message} date if the{@link Message} exists, null otherwise */
    @Nullable
    public String getDate() {
        if(message != null) return message.getDate();
        else return null;
    }

    /** Returns a string array of media if there is a{@link Message}, null otherwise */
    @Nullable
    public String[] getMedia() {
        if(message != null) return message.getMedia();
        else return null;
    }


//--------------------------------------------------------------------------------------------------


    /** Returns true if there is an original {@link Message} text (usually encrypted), false otherwise */
    public boolean hasText() {
        return getOriginalMessage() != null && !getOriginalMessage().isEmpty();
    }

    /** Returns true if the {@link Message} contains media/attachments */
    public boolean hasMedia() {
        return getMedia() != null && getMedia().length > 0;
    }

    /** Returns true if there is an original {@link Message} text that may have been encrypted, false otherwise */
    public boolean hasEncryptedText() {
        return hasText() && !TextUtils.isEmpty(getSubject());
    }


//--------------------------------------------------------------------------------------------------


    /** Returns true if this {@link Message} is from someone other then this user */
    public boolean isFromContact() {
        return type == TYPE_RECEIVED ||
                (
                        message != null &&
                        message.getSenderUsername() != null &&
                        !message.getSenderUsername().equalsIgnoreCase(NetMeApp.getCurrentUser())
                );
    }

    /** Returns a formatted Date (weekday month (day number)) */
    @Nullable
    public String getFormattedDate() {
        Date msgDate = DateUtil.getDateFromStringUTC(getDate());
        if(msgDate != null) return DateUtil.getStringFromDate(msgDate, "EEE, MMM dd");
        else return null;
    }

    /** Returns a formatted Date (hour:minute am/pm) */
    @Nullable
    public String getFormattedHour() {
        Date msgDate = DateUtil.getDateFromStringUTC(getDate());
        return DateUtil.getStringFromDate(msgDate, "KK:mm aa");
    }

    /** Returns a formatted Date (weekday hour:minute am/pm) */
    @Nullable
    public String getFormattedTime() {
        Date msgDate = DateUtil.getDateFromStringUTC(getDate());
        return DateUtil.getStringFromDate(msgDate, "EEE KK:mm aa");
    }


//--------------------------------------------------------------------------------------------------


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(AESTag);
        parcel.writeParcelable(message, i);
        parcel.writeString(decryptedText);
        parcel.writeList(decryptedMedia);
        parcel.writeInt(type);
        parcel.writeInt(textStatus);
        parcel.writeInt(mediaStatus);
    }

    public static final Creator<ManagedMessage> CREATOR = new Creator<ManagedMessage>() {
        @Override
        public ManagedMessage createFromParcel(Parcel in) {
            return new ManagedMessage(in);
        }

        @Override
        public ManagedMessage[] newArray(int size) {
            return new ManagedMessage[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ManagedMessage that = (ManagedMessage) o;

        return id == that.id &&
                type == that.type &&
                textStatus == that.textStatus &&
                mediaStatus == that.mediaStatus &&
                (message != null ?
                        message.equals(that.message) :
                        that.message == null &&
                                (decryptedText != null ?
                                        decryptedText.equals(that.decryptedText) :
                                        that.decryptedText == null &&
                                                (
                                                        decryptedMedia != null ?
                                                        decryptedMedia.equals(that.decryptedMedia) :
                                                        that.decryptedMedia == null
                                                )
                                )
                );

    }

    @Override
    public int compareTo(@NonNull ManagedMessage another) {
        // both items have messages, so compare their dates
        if(getDate() != null && another.getDate() != null) return another.getDate().compareTo(getDate());
        // this item has a message but the other does not, so this is older (higher up in the msg list)
        else if(message != null && getDate() != null) return 1;
        // the other item has a message but this doesn't, so this is newer (lower down on the list)
        else if(another.message != null && another.getDate() != null) return -1;
        // neither has a message but have ids, compare ids
        else if(id > -1 && another.getId() > -1) return Long.compare(another.getId(), id);
        // nothing can be compared, return that they are then same
        else return 0;
    }

    /**
     * Returns whether if this is the same as another managed message.
     * In order to be considered the same, the both managed messages must have the same
     * id or both must have a {@link Message} with the same id.
     * @return {@code TRUE} is the managed messages are the same, {@code FALSE} otherwise
     * */
    public boolean sameAs(@NonNull ManagedMessage another) {
        return  (
                        id > -1 &&
                        another.getId() > -1 &&
                        id == another.getId()
                ) ||
                (
                        message != null &&
                        another.getMessage() != null &&
                        getMessageId() == another.getMessageId()
                );
    }
}
