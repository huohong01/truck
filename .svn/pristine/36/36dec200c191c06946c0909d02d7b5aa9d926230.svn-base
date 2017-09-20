package com.hsdi.NetMe.network;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.database.ContactsManager;
import com.hsdi.NetMe.models.events.EventMessageLog;
import com.hsdi.NetMe.ui.chat.ChatActivity;
import com.hsdi.NetMe.util.Constants;
import com.hsdi.NetMe.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;

/**
 * Created by huohong.yi on 2017/7/4.
 */

public class NotificationService extends IntentService {


    public NotificationService() {
        super("notification");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotificationService(String name) {
        super("notification");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String senderUsername = intent.getStringExtra(Constants.SENDER_NAME);
        String message = intent.getStringExtra(Constants.MESSAGE);
        long chatId = intent.getLongExtra(Constants.CHAT_ID,0);

        Log.i("yuyong_message", String.format("onHandleIntent: --->%s--->%s--->%d",senderUsername,message,chatId));

        showNotification(getBaseContext(),senderUsername,message,chatId);
    }

    private void showNotification(Context context, String username, String message, long chatId) {
        // trying to find the contact's name to use as the title, if not found, just using the address
        String title = ContactsManager.getDisplayName(context, username);
        if (title == null || title.isEmpty()) title = username;

        // getting a user icon to use as the large icon, if not found using default user icon
        Bitmap largeIcon;
        try {
            Uri thumbnailUri = ContactsManager.getThumbnailUri(context, username);
            InputStream picStream = ContactsManager.openThumbnailStream(context, thumbnailUri);
            Bitmap squarePicture = BitmapFactory.decodeStream(picStream);
            largeIcon = Utils.getCircleShape(squarePicture, squarePicture.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("GcmBroadCastReceiver", "Failed to get contact avatar", e);
            largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_pic_avatar);
        }

        // create the intent that will be run when this is clicked
        Intent chatIntent = new Intent(context, ChatActivity.class);
        chatIntent.putExtra(ChatActivity.EXTRA_CHAT_TYPE, ChatActivity.TYPE_TEXT_ONLY);
        chatIntent.putExtra(ChatActivity.EXTRA_CHAT_ID, chatId);
        chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent chatPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        chatIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        // building the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setLargeIcon(largeIcon);
        notificationBuilder.setContentTitle(title);
        // notificationBuilder.setContentTitle(context.getString(R.string.new_message));
        notificationBuilder.setContentText(message);
        notificationBuilder.setSmallIcon(R.drawable.messenger_48_icon);
        notificationBuilder.setContentIntent(chatPendingIntent);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setCategory(Notification.CATEGORY_MESSAGE);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notificationBuilder.setVibrate(new long[]{500});

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) chatId, notificationBuilder.build());

        //update the chatLogs
        EventBus.getDefault().post(new EventMessageLog());
    }
}
