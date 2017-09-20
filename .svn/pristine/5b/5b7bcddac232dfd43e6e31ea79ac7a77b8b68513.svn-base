package com.hsdi.NetMe.ui.chat;

/** Interface used to communicate with the ChatActivity */
public interface ChatInterface {

    /**
     * Called in the videoFragment only!
     * Switches of everything except for the subscriber/contact's view
     * @param fullScreenOn    whether to hide the text chat or show it
     * */
    void toggleFullScreen(boolean fullScreenOn, int selectType);

    /** Requests to hides the keyboard */
    void hideKeyboard();

    /**
     * Alerts that the video should be ended
     * @param performEndVerification    whether to verify that the user wants to end the video call
     * */
    void finishVideo(boolean performEndVerification);

    /**
     * Alerts to update the text chat fragment to show the chat with the passed id
     * @param chatId    the id for the chat to start
     * */
    void startChat(long chatId);

    /**
     * Called in the TextFragment only!
     * Requests that the ChatActivity switch from just text chat to a video call
     * @param invited    the user to invite to the call
     * @param chatId     the chat id to use to continue the active chat
     * */
    void switchToVideo(String invited, long chatId, int meetingType);

}
