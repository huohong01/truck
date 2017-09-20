package com.hsdi.NetMe.models.events;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Event which is used with EventBus to alert registered classes of updates to managed messages.
 */
public class EventManMsgUpdate {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LIST_ACTION_UNKNOWN, LIST_ACTION_INSERTED, LIST_ACTION_CHANGED, LIST_ACTION_REMOVED,})
    public @interface ListAction {}

    public static final int LIST_ACTION_UNKNOWN = 0;
    public static final int LIST_ACTION_INSERTED = 1;
    public static final int LIST_ACTION_CHANGED = 2;
    public static final int LIST_ACTION_REMOVED = 3;

    private final long chatId;
    private final long messageId;
    private final int listIndex;
    private final int listAction;

    public EventManMsgUpdate(long chatId, long messageId, int listIndex, @ListAction int listAction) {
        this.chatId = chatId;
        this.messageId = messageId;
        this.listIndex = listIndex;
        this.listAction = listAction;
    }

    public long getChatId() {
        return chatId;
    }

    public int getChatIdInt() {
        return (int) chatId;
    }

    public long getMessageId() {
        return messageId;
    }

    public int getMessageIdInt() {
        return (int) messageId;
    }

    public int getListIndex() {
        return listIndex;
    }

    @ListAction
    public int getListAction() {
        return listAction;
    }
}
