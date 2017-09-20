package com.hsdi.NetMe.ui.chat.util;

import com.hsdi.NetMe.models.Chat;
import com.hsdi.NetMe.models.Participant;

import java.io.File;
import java.util.List;

public interface MsgManagerCallback {
    void messageSavingResult(File savedMedia, boolean successful);
    void onChatLoaded(Chat chat,boolean isLoaded);
    void onChatFailed(String errorMessage);
    void onNewChatStarted(long chatId, List<Participant> participants);
}
