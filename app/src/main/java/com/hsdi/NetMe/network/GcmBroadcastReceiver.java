package com.hsdi.NetMe.network;

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
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.database.ContactsManager;
import com.hsdi.NetMe.models.GcmPush;
import com.hsdi.NetMe.models.ManagedMessage;
import com.hsdi.NetMe.models.Message;
import com.hsdi.NetMe.models.events.EventMeetingUpdate;
import com.hsdi.NetMe.models.events.EventMessageLog;
import com.hsdi.NetMe.models.events.EventMessageUpdate;
import com.hsdi.NetMe.models.response_models.BaseResponse;
import com.hsdi.NetMe.models.response_models.GetChatMessageResponse;
import com.hsdi.NetMe.ui.chat.ChatActivity;
import com.hsdi.NetMe.ui.chat.text.IUpdateInputtingListener;
import com.hsdi.NetMe.ui.chat.util.DecryptMessageAsyntask;
import com.hsdi.NetMe.ui.chat.util.INotificationMessageListener;
import com.hsdi.NetMe.ui.chat.util.MessageManager;
import com.hsdi.NetMe.util.Constants;
import com.hsdi.NetMe.util.DeviceUtils;
import com.hsdi.NetMe.util.PreferenceManager;
import com.hsdi.NetMe.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "GcmBroadcastReceiver";

    private static final int REQUEST_GO_TO_CHAT = 102;
    private MessageManager msgManager;
    private static IUpdateInputtingListener inputtingListener;

    public static void setInputtingListener(IUpdateInputtingListener updateInputtingListener) {
        inputtingListener = updateInputtingListener;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        printPush(intent);
        GcmPush newPush = new GcmPush(intent);
        Log.d(TAG, newPush.toString());

        // if it has a command item, handle the message
        Log.d("yuyong_Msg", "onReceive GcmBroadcastReceiver");
        if (newPush.hasValidCommand()) handleIntent(context, newPush);
    }

    /**
     * Determines how to handle the intent from the received push notification
     *
     * @param context context
     * @param gcmPush GcmPush object which represents the contents of the push
     */
    protected void handleIntent(Context context, GcmPush gcmPush) {
        // getting the preference manager
        PreferenceManager prefManager = PreferenceManager.getInstance(context);

        // check the user's long status
        boolean userLoggedIn = NetMeApp.getInstance().isCurrentUserLoggedIn();
        boolean userRemembered = prefManager.userLoginRemembered();
        boolean loginCheck = userLoggedIn || userRemembered;

        // only continue if the user is logged in in some way and the token is valid
        if (loginCheck && !gcmPush.isFromCurrentUser() && handleToken(context, gcmPush)) {

            // get the "Do Not Disturb" state
            boolean doNotDisturb = prefManager.getDoNotDisturb();

            // get the command and determine what to do with it
            switch (gcmPush.getCommand()) {
                case GcmPush.NEW_MEETING_KEY: {
                    // ignore this message if "do not disturb" has been turned on or the user is not signed in
                    if (!doNotDisturb) handleStartMeeting(context, gcmPush);
                    break;
                }

                case GcmPush.ACCEPTED_MEETING_KEY: {
                    // tell the chat that the other person has joined if the chat is running
                    // send and alert to the video fragment that the other person has joined this meeting
                    //  *NOTE: will be ignored if the video fragment is not in loaded
                    EventBus.getDefault().post(
                            new EventMeetingUpdate(EventMeetingUpdate.TYPE_JOINED, gcmPush.getMeetingId())
                    );
                    break;
                }

                case GcmPush.REJECTED_MEETING_KEY: {
                    // send and alert to the video fragment that the meeting was rejected
                    //  *NOTE: will be ignored if the video fragment is not loaded
                    EventBus.getDefault().post(
                            new EventMeetingUpdate(EventMeetingUpdate.TYPE_REJECTED, gcmPush.getMeetingId())
                    );
                    break;
                }

                case GcmPush.LEAVE_MEETING_KEY: {
                    // send and alert to the video fragment that the meeting has ended since there are currently
                    //  only 2 people per meeting
                    //  *NOTE: will be ignored if the video fragment is not loaded
                    EventBus.getDefault().post(
                            new EventMeetingUpdate(EventMeetingUpdate.TYPE_LEFT, gcmPush.getMeetingId())
                    );
                    break;
                }
                case GcmPush.SEND_TYPE_KEY:
                    Log.i("yuyong_message", "handleIntent: " + GcmPush.SEND_TYPE_KEY);
                    if (NetMeApp.isChatVisible(gcmPush.getChatId())) {
                        Log.i("yuyong_message", "handleIntent: --->" + GcmPush.SEND_TYPE_KEY);
                        inputtingListener.onShowInputting(gcmPush.getChatId());
                    }
                    break;
                case GcmPush.NEW_CHAT_KEY:
                case GcmPush.NEW_MSG_KEY:
                case GcmPush.EDITED_MSG_KEY:
                case GcmPush.REMOVED_MSG_KEY: {
                    // ChatActivity is not visible and doNotDisturb is turned off, so notify the user
                    if (!doNotDisturb && !NetMeApp.isChatVisible(gcmPush.getChatId())) {
                        long chatId = gcmPush.getChatId();
                        try {
                            msgManager = NetMeApp.getInstance().getMsgManagerForChat(chatId);
                           // showNotification(context, gcmPush.getSenderUsername(), gcmPush.getMessage(), gcmPush.getChatId());
                            //显示通知需要查询数据库，属于耗时操作，会出现ANR，需要启动intentService
                            Intent intent = new Intent(context,NotificationService.class);
                            intent.putExtra(Constants.SENDER_NAME,gcmPush.getSenderUsername());
                            intent.putExtra(Constants.MESSAGE,gcmPush.getMessage());
                            intent.putExtra(Constants.CHAT_ID,gcmPush.getChatId());
                            context.startService(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e("yi", "handleIntent: ======================================== gcmPush.getSenderUsername()=" + gcmPush.getSenderUsername() + ", gcmPush.getMessage() = " + gcmPush.getMessage() + ",gcmPush.getChatId()=" + gcmPush.getChatId());

                    }
                    // otherwise send the event notification that the chat needs to be updated
                    else {
                        // send an alert to the text fragment about the new message
                        //  *NOTE: will be ignored if the text fragment is not loaded

                        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日    HH:mm:ss     ");
                        Date curDate    =   new Date(System.currentTimeMillis());//获取当前时间
                        String    str    =    formatter.format(curDate);
                        Log.d("yuyong_Msg", "handleIntent post" + str);
                        EventBus.getDefault().post(
                                new EventMessageUpdate(
                                        EventMessageUpdate.parseCommand(gcmPush.getCommand()),
                                        gcmPush.getChatId(),
                                        gcmPush.getMessageId(),
                                        gcmPush.getSenderUsername()
                                )
                        );
                    }

                    //sends an event notification that there is a new message to be accounted for in the logs
                    EventBus.getDefault().post(new EventMessageLog());
                    break;
                }

                default: {
                    // print that something when wrong
                    Log.e(TAG, "*************************************************************");
                    Log.e(TAG, "Unable to handle this push");
                    Log.e(TAG, gcmPush.toString());
                    Log.e(TAG, "*************************************************************");
                    break;
                }
            }
        }
    }

    private void getNotifitionMessage(final Context context, final String username, long messageId, final long chatId) {

        // final ManagedMessage manMsg = msgManager.createReceivedManagedMessage(messageId);

        PreferenceManager manager = NetMeApp.getInstance().getPrefManager();
        RestServiceGen.getCachedService()
                .getChatMessage(
                        manager.getCountryCallingCode(),
                        manager.getPhoneNumber(),
                        manager.getPassword(),
                        chatId,
                        messageId
                )
                .enqueue(new Callback<GetChatMessageResponse>() {
                    @Override
                    public void onResponse(Call<GetChatMessageResponse> call, Response<GetChatMessageResponse> response) {
                        // if successfully loaded, pass to message loading/decrypting logic
                        if (response.body() != null && response.body().isSuccess()) {
                            Message message = response.body().getMessage();
                            Log.e("yi", "onResponse: =========================================loadMessage : " + message.getMessage() + "," + message.getSubject());
                            //processMessage(message);
                            // creating managed message
                            ManagedMessage mMsg = msgManager.createLoadingManagedMessage(message);

                            // if the message maybe encrypted, try to decrypt it
                            if (mMsg.hasEncryptedText()) {
                                Log.e("yi", "onResponse: ================================= mMsg = " + mMsg);
                                //decryptTextMessage(mMsg);
                                new DecryptMessageAsyntask(new INotificationMessageListener() {
                                    @Override
                                    public void onMessageSuccess(String message) {
                                        showNotification(
                                                context,
                                                username,
                                                message,
                                                chatId
                                        );
                                    }

                                    @Override
                                    public void onMessageFail(String message) {

                                    }
                                }).execute(mMsg);
                            }


                        }
                        // failed to get details, show failed
                        else {
                            try {
                                //  updateMsgFailed(manMsg);
                            } catch (NullPointerException npe) {
                                Log.e("MsgManager", "Failed to update the message to failed status", npe);
                                Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetChatMessageResponse> call, Throwable t) {
                        try {
                            // updateMsgFailed(manMsg);
                        } catch (NullPointerException npe) {
                            Log.e("MsgManager", "Failed to update the message to failed status", npe);
                            Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


//------------------------------------- INTENT HANDLERS --------------------------------------------


    /**
     * pulls the needed information from the intent and starts a calling activity
     *
     * @param context context
     * @param gcmPush GcmPush object which represents the contents of the push
     */
    public void handleStartMeeting(Context context, GcmPush gcmPush) {
        // checking to make sure this notification isn't from this user, if so, ignore
        if (gcmPush.isFromCurrentUser()) return;

        // setting that this new call has not been handled yet
        NetMeApp.setCallHandledStatus(gcmPush.getMeetingId(), true);

        // calling the chat activity with the new meeting info
        Intent callIntent = new Intent(context, ChatActivity.class);
        //此处需要把通话类型放入callIntent中，从GcmPush获取，需要在实体类GcmPush中新增变量Int selectType
        callIntent.putExtra(ChatActivity.EXTRA_MEETING_TYPE,gcmPush.getMeetingType());
        callIntent.putExtra(ChatActivity.EXTRA_CHAT_TYPE, ChatActivity.TYPE_RING_VIDEO);
        callIntent.putExtra(ChatActivity.EXTRA_MEETING_ID, gcmPush.getMeetingId());
        callIntent.putExtra(ChatActivity.EXTRA_CHAT_ID, gcmPush.getChatId());
        callIntent.putExtra(ChatActivity.EXTRA_INVITED, gcmPush.getSenderUsername());
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(callIntent);
    }

    /**
     * pulls the needed information from the intent and alerts the chat to start a new chat
     *
     * @param context context
     * @param gcmPush GcmPush object which represents the contents of the push
     */
    public void handleStartChat(Context context, GcmPush gcmPush) {
        if (gcmPush.isFromCurrentUser()) return;

        // alerting of a new chat
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_CHAT_TYPE, ChatActivity.TYPE_TEXT_ONLY);
        intent.putExtra(ChatActivity.EXTRA_CHAT_ID, gcmPush.getChatId());
        intent.putExtra(ChatActivity.EXTRA_INVITED, gcmPush.getSenderUsername());
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                REQUEST_GO_TO_CHAT,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // create a system notification about this and make the device buzz
        DeviceUtils.generateNotification(
                context,
                context.getString(R.string.notification_new_msg_prefix) + gcmPush.getSenderDisplayName(),
                pendingIntent
        );
        DeviceUtils.vibrate(context);

        EventBus.getDefault().post(new EventMessageLog());
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Check if it is current or old token, for old token, logout that token quietly
     * in a separate thread
     *
     * @param gcmPush GcmPush object which represents the contents of the push
     * @return true if this is the current token, false if old tokens
     */
    public static boolean handleToken(final Context context, GcmPush gcmPush) {
        // make sure the passed token is valid. Return invalid if false
        if (gcmPush.getToken() == null || gcmPush.getToken().isEmpty()) return false;

        // compare the token to the store token, if it is equal to the currently stored token
        //      this is a good token, so return true
        PreferenceManager manager = PreferenceManager.getInstance(context);
        String gcmId = manager.getGCMRegistrationId();
        if (gcmPush.getToken().equals(gcmId)) return true;

        // logout the bad token
        RestServiceGen.getUnCachedService().userLogout(
                manager.getCountryCallingCode(),
                manager.getPhoneNumber(),
                manager.getPassword(),
                gcmPush.getToken(),
                DeviceUtils.getDeviceId(context),
                DeviceUtils.getAppVersion(context),
                DeviceUtils.getPlatformVersion()
        ).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    /*ignore, nothing needs to be done if the logout worked*/
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                    /*ignore, nothing should be done if it failed,
                    if the bad token appears again it will be attempted to be logged out again*/
            }
        });

        // return false since this is a bad token
        return false;
    }

    /**
     * Creates and shows a notification for text chat events showing that it is from the contact
     * with passed message and will open the chat whose id is passed.
     *
     * @param context  context
     * @param username the username from the user who sent this
     * @param message  the message to show the user
     * @param chatId   the chat id for the chat to open when the notification is clicked
     */
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

    private void printPush(Intent intent) {
        Log.i(TAG, "---------------------------Received Notification Start-------------------");
        for (String key : intent.getExtras().keySet()) {
            Log.d(TAG, key + ":" + intent.getExtras().get(key));
        }
        Log.i(TAG, "---------------------------Received Notification End---------------------");
    }
}
