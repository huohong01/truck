package com.hsdi.NetMe.models.events;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class EventScrollMsgList {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SCROLL_TO_BOTTOM, ASK_TO_SCROLL})
    public @interface ScrollType {}

    public static final int SCROLL_TO_BOTTOM = 0;
    public static final int ASK_TO_SCROLL = 1;

    private final int scrollType;


    public EventScrollMsgList(@ScrollType int scrollType) {
        this.scrollType = scrollType;
    }

    @ScrollType
    public int getScrollType() {
        return scrollType;
    }
}
