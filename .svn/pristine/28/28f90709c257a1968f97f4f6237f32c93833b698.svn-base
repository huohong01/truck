package com.hsdi.NetMe.models.events;

import android.support.annotation.IntDef;

import com.hsdi.NetMe.models.Contact;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class EventFavorite {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ADDED, REMOVED})
    public @interface Type {}

    public static final int ADDED = 0;
    public static final int REMOVED = 1;

    private int eventType;
    private Contact contact;

    public EventFavorite(@Type int eventType, Contact contact) {
        this.eventType = eventType;
        this.contact = contact;
    }

    @Type
    public int getEventType() {
        return eventType;
    }

    public Contact getContact() {
        return contact;
    }
}
