package com.hsdi.NetMe.ui.chat.util;

import android.view.View;

import com.hsdi.NetMe.models.ManagedMessage;
import com.hsdi.NetMe.models.Media;

public interface OnMessageClickListener {
    void onAvatarClicked(String senderUsername);
    void onClick(ManagedMessage managedMessage);
    void onCloseClicked(Media media);
    boolean onLongClicked(View messageView, ManagedMessage managedMessage);
}
