package com.hsdi.NetMe.ui.chat.util;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.maps.model.LatLng;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.database.ChatMessageManager;
import com.hsdi.NetMe.database.ChatTrackerManager;
import com.hsdi.NetMe.models.Chat;
import com.hsdi.NetMe.models.ManagedMessage;
import com.hsdi.NetMe.models.Media;
import com.hsdi.NetMe.models.MediaFactory;
import com.hsdi.NetMe.models.Message;
import com.hsdi.NetMe.models.events.EventManMsgUpdate;
import com.hsdi.NetMe.models.events.EventMessageLog;
import com.hsdi.NetMe.models.events.EventMessageUpdate;
import com.hsdi.NetMe.models.events.EventScrollMsgList;
import com.hsdi.NetMe.models.response_models.BaseResponse;
import com.hsdi.NetMe.models.response_models.GetChatMessageResponse;
import com.hsdi.NetMe.models.response_models.GetChatResponse;
import com.hsdi.NetMe.models.response_models.SendMessageResponse;
import com.hsdi.NetMe.models.response_models.StartChatResponse;
import com.hsdi.NetMe.network.HttpDownloadUtility;
import com.hsdi.NetMe.network.RestServiceGen;
import com.hsdi.NetMe.util.AESCrypt;
import com.hsdi.NetMe.util.DeviceUtils;
import com.hsdi.NetMe.util.MyLog;
import com.hsdi.NetMe.util.PreferenceManager;
import com.hsdi.NetMe.util.ThreadPool;
import com.hsdi.NetMe.util.Utils;
import com.macate.csmp.CSMPCrypt;
import com.macate.csmp.CSMPCryptException;
import com.macate.csmp.CSMPDecryptResult;
import com.macate.csmp.CSMPIndexingKeyGenerator;
import com.macate.csmp.CSMPKeyGenerator;
import com.macate.csmp.CSMPMetaData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageManager {
    private static final String TAG = "MsgMgr";

    private final SortedMap<Long, ManagedMessage> messageMap;
    private final SortedList<Long> messageIdList;
    private final PreferenceManager manager;
    private final AESCrypt encryptAES;

    private long chatId;
    private final String AESTag;
    private final String encryptedFileExtension;
    private static final int millisecondChars = 13;
    public static final String FILE_NAME_KEY = "fileName";
    public static final String FILE_TYPE_KEY = "fileType";
    private Context mcontext;

    public MessageManager(Context context, long newChatId) throws Exception {
        this.mcontext = context;
        messageMap = new TreeMap<>(new Comparator<Long>() {
            @Override
            public int compare(Long lhs, Long rhs) {
                return rhs.compareTo(lhs);
            }
        });
        messageIdList = new SortedList<>(Long.class, new SortedList.Callback<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o2.compareTo(o1);
            }

            @Override
            public void onInserted(int position, int count) { }

            @Override
            public void onRemoved(int position, int count) { }

            @Override
            public void onMoved(int fromPosition, int toPosition) { }

            @Override
            public void onChanged(int position, int count) { }

            @Override
            public boolean areContentsTheSame(Long oldItem, Long newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Long item1, Long item2) {
                return item1.equals(item2);
            }
        });
        manager = PreferenceManager.getInstance(context);
        encryptAES = new AESCrypt(NetMeApp.getCurrentUser());

        Log.i("yuyong", String.format("MessageManager:aes--->%d---password--->%s",encryptAES.hashCode(),NetMeApp.getCurrentUser()));

        AESTag = context.getString(R.string.aes_tag);
        encryptedFileExtension = context.getString(R.string.encrypted_file_extension);
        chatId = newChatId;

        EventBus.getDefault().register(this);
    }

    public void prepareForGarbageCollection() {
        EventBus.getDefault().unregister(this);
    }

    /** Returns the chat id for this manager */
    public long getChatId() {
        return chatId;
    }

    /**
     * Returns the number of messages in the message list
     * @return the total number of messages in the list
     * */
    public int getMessageCount() {
        return messageMap.size();
    }

    /**
     * Returns a managed message with a matching id
     * @param messageId    the id of the message to look for
     * */
    @Nullable
    public ManagedMessage getMessageWithId(long messageId) {
        return messageMap.get(messageId);
    }

    /**
     * Returns a managed message at a specific position in the list for a specific chat.
     * @param index     the position/index of the item to get
     * @return the managed message if it exists, null otherwise
     * */
    @Nullable
    public ManagedMessage getMessageAtIndex(int index) {
        if(messageMap.size() > index && messageIdList.size() > index) {
            return messageMap.get(messageIdList.get(index));
        }
        else return null;
    }

    /**
     * Show the user an error message in the form of the toast
     * @param errorMsg    the error message to show the user
     * */
    private void showErrorToast(String errorMsg) {
        Toast.makeText(NetMeApp.getInstance(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    /////////////////////////////////////////////////////////////////////////////////
    public void messageUpdated(EventMessageUpdate event) {
        Log.d("yuyong_Msg", "messageUpdated : " + event.toString());
        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日    HH:mm:ss     ");
        Date curDate    =   new Date(System.currentTimeMillis());//获取当前时间
        String    str    =    formatter.format(curDate);
        Log.d("yuyong_Msg", "messageUpdated : " + str);
        // if the update is not for this chat, ignore it
       // if(event.getChatId() != chatId) return;
        if(event.getChatId() != chatId){
            return;
        }

        switch (event.getType()) {
            case EventMessageUpdate.newMsg:
            case EventMessageUpdate.editedMsg:
                loadMessage(event.getMessageId());
                break;

            case EventMessageUpdate.removedMsg:
                removeMessage(event.getMessageId());
                break;
        }
    }

    /**
     * Creates the name which the backend will use to save the file using the file name
     * and file type, plus
     * Encrypts the file name and type, along with a millisecond timestamp to avoid file
     * name collisions,
     * @param fileName    the name, including extension, of the file
     * @param type        the {@link com.hsdi.NetMe.models.Media.Type} of file
     * @return the name to use for the encrypted file
     * */
    private String createEncryptedFileName(String fileName, @Media.Type int type) throws Exception {
        // getting the time, will be used as an indexer to prevent collision with similar
        //      file names on the backend
        String timeString = String.valueOf(System.currentTimeMillis());

        // combining the names to their final form
        String combinedFileName = timeString + fileName + String.valueOf(type);

        // encrypting the name
        String encryptedFileName = encryptAES.encryptUrlSafeString(combinedFileName).trim();

        Log.d(TAG,
                "timeStamp: '" + timeString + "'" +
                        "\nfileName: '" + fileName + "'" +
                        "\ntype: '" + type + "'" +
                        "\ncombined: '" + combinedFileName + "'" +
                        "\nencrypted: '" + encryptedFileName + "'"
        );

        // adding the extension and returning the whole thing
        return encryptedFileName.trim() + encryptedFileExtension;
    }

    /**
     * Parses the name a AES encrypted file was saved as and returns the original file name
     * and {@link com.hsdi.NetMe.models.Media.Type}
     * @param encryptedFileName    the name the encrypted file was saved.
     * @return a String array containing the name of the file followed by the String value
     * of the int type, in that order
     * */
    private Map<String, String> parseEncryptedFileName(String encryptedFileName) throws Exception {
        Log.i("yuyong", String.format("parseEncryptedFileName:aes--->%d",encryptAES.hashCode()));
        return parseEncryptedFileName(encryptAES, encryptedFileExtension, encryptedFileName);
    }

    /**
     * Parses the name a AES encrypted file was saved as and returns the original file name
     * and {@link com.hsdi.NetMe.models.Media.Type}
     * @param aesCrypt                  an instance of {@link AESCrypt} to use for decryption
     * @param encryptedFileExtension    the extension suffix to give the encrypted file name
     * @param encryptedFileName         the name the encrypted file was saved.
     * @return a String array containing the name of the file followed by the String value
     * of the int type, in that order
     * */
    public static Map<String, String> parseEncryptedFileName(AESCrypt aesCrypt, String encryptedFileExtension, String encryptedFileName) throws Exception {
        String encryptedName = encryptedFileName.replace(encryptedFileExtension, "");
        Log.i("yuyong", String.format("parseEncryptedFileName=====aes--->%d",aesCrypt.hashCode()));
        if (1==0){
            String str = "149303794065420170424204528.voice.3gp3";
            String encryptName = aesCrypt.encryptUrlSafeString(str).trim();
            String fileName ="eNaZd8G699r-hdQjQe_WcG57nkX1CSNFkGjD-WkCipdBNYJ5O54XEoho8CC8IZPY.sdf";
            String enName = fileName.replace(".sdf","");
            String decryptedName = aesCrypt.decryptUrlSafeString(enName);
            Log.i("yuyong", String.format("encrypt:%s--->encryptedName:%s--->fileName:%s--->enName:%s--->decryptedName:%s",str,encryptedName,fileName,enName,decryptedName));
        }

        // decrypt the filename stored in the url
        String decryptedName = aesCrypt.decryptUrlSafeString(encryptedName);
        // separate the file name and type
        String fileName = decryptedName.substring(millisecondChars, decryptedName.length() - 1);
        String fileType = decryptedName.substring(decryptedName.length() - 1);

        // put everything in a map
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put(FILE_NAME_KEY, fileName);
        resultMap.put(FILE_TYPE_KEY, fileType);

        return resultMap;
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Creates a managed message that is being sent and adds it to the list
     * @param decryptedText     any decrypted text associated with this
     * @param decryptedMedia    any decrypted media associated with this
     * @return the created {@link ManagedMessage}
     * */
    @NonNull
    private ManagedMessage createSendingManagedMessage(String decryptedText, List<Media> decryptedMedia) {
        // figuring out what the test status should be
        int textStatus = ManagedMessage.STATUS_NONE;
        if(decryptedText != null && !decryptedText.isEmpty()) {
            textStatus = ManagedMessage.STATUS_IN_PROGRESS;
        }

        // figuring out what the media status should be
        int mediaStatus = ManagedMessage.STATUS_NONE;
        if(decryptedMedia != null && decryptedMedia.size() > 0) {
            mediaStatus = ManagedMessage.STATUS_IN_PROGRESS;
        }
        Log.e("media", "createSendingManagedMessage: ================textStatus = " +textStatus + ",mediaStatus" + mediaStatus );
        // create the managed message and add it
        ManagedMessage manMsg = new ManagedMessage(
                System.currentTimeMillis(),
                decryptedText,
                decryptedMedia,
                ManagedMessage.TYPE_SENT,
                textStatus,
                mediaStatus,
                AESTag
        );
        addMessage(manMsg);

        EventBus.getDefault().post(new EventScrollMsgList(EventScrollMsgList.SCROLL_TO_BOTTOM));

        return manMsg;
    }

    /**
     * Creates a managed message that is being loaded and adds it to the list
     * @param message    the message to add
     * @return the created {@link ManagedMessage}
     * */
    @NonNull
    public ManagedMessage createLoadingManagedMessage(@NonNull Message message) {
        // figuring out what the text and media status should be
        int textStatus = message.hasText() ? ManagedMessage.STATUS_IN_PROGRESS : ManagedMessage.STATUS_NONE;
        int mediaStatus = message.hasMedia() ? ManagedMessage.STATUS_IN_PROGRESS : ManagedMessage.STATUS_NONE;

        // create the managed message and add it
        ManagedMessage manMsg = new ManagedMessage(
                message,
                textStatus,
                mediaStatus,
                AESTag
        );
        addMessage(manMsg);

        return manMsg;
    }

    /**
     * Creates a managed message that was received and adds it to the list
     * @param messageId    the id from the back end for this message
     * @return the created {@link ManagedMessage}
     * */
    @NonNull
    public ManagedMessage createReceivedManagedMessage(long messageId) {
        // create the managed message and add it
        ManagedMessage manMsg = new ManagedMessage(
                messageId,
                new Message(messageId),
                AESTag
        );
        addMessage(manMsg);

        EventBus.getDefault().post(new EventScrollMsgList(EventScrollMsgList.ASK_TO_SCROLL));

        return manMsg;
    }

    /**
     * Adds the {@link ManagedMessage} to the list of messages
     * @param manMsg    the {@link ManagedMessage} to add
     * */
    private void addMessage(ManagedMessage manMsg) {
        // can't do anything without valid data
        if(manMsg == null) return;

        // getting the message id
        long msgId = manMsg.getMessageId() > -1 ? manMsg.getMessageId() : manMsg.getId();

        // get weather it already exists or not, this will be used to determine the action for the event update
        boolean changed = messageMap.containsKey(msgId);

        // update/add the message and message id
        messageMap.put(msgId, manMsg);
        messageIdList.add(msgId);

        // send an alert about the message being change
        EventBus.getDefault().post(
                new EventManMsgUpdate(
                        chatId,
                        msgId,
                        messageIdList.indexOf(msgId),
                        changed ?
                                EventManMsgUpdate.LIST_ACTION_CHANGED :
                                EventManMsgUpdate.LIST_ACTION_INSERTED
                )
        );
    }

    /**
     * Update a sent message, message map and message id list with the server information
     * @param oldMsgId    the id the managed message used when it was created
     * @param message     the new message object from the server
     * */
    private void updateSentMessage(long oldMsgId, Message message) {
        // remove the message from the mapping and the id from the list
        ManagedMessage manMsg = messageMap.remove(oldMsgId);
        int index = messageIdList.indexOf(oldMsgId);
        messageIdList.removeItemAt(index);

        // send an event that this item has been removed
        EventBus.getDefault().post(
                new EventManMsgUpdate(
                        chatId,
                        oldMsgId,
                        index,
                        EventManMsgUpdate.LIST_ACTION_CHANGED
                )
        );
        EventBus.getDefault().post(new EventMessageLog());

        // updating the managed message and have it added to the map and list
        manMsg.setMessage(message).setId(-1);
        updateMsgReady(manMsg);
    }

    /**
     * Removes a managed message from the list
     * @param manMsgId    the id of the message to remove
     * */
    private void removeMessage(long manMsgId) {
        // getting the position of the message id to use in the event update
        int index = messageIdList.indexOf(manMsgId);

        // removing the message and message ids
        messageMap.remove(manMsgId);
        messageIdList.remove(manMsgId);

        // send an alert about the message being removed
        EventBus.getDefault().post(
                new EventManMsgUpdate(
                        chatId,
                        manMsgId,
                        index,
                        EventManMsgUpdate.LIST_ACTION_REMOVED
                )
        );
    }

    /**
     * Attempt to resend the message
     * @param message    the message to resend
     * */
    public void resendMessage(ManagedMessage message) {
        removeMessage(message.getId());

        if(message.hasDecryptedMedia()) {
            // get the media, currently there is only one in the list
            Media media = message.getDecryptedMedia().get(0);

            // resend depending on the type
            switch (message.getDecryptedMedia().get(0).getType()) {
                case Media.TYPE_IMAGE:
                    if(media.getFile() != null) sendImageWithText(message.getDecryptedText(), media.getFile());
                    else sendImageWithText(message.getDecryptedText(), media.getFileName(), media.getFileDesc(),media.getUri());
                    break;

                case Media.TYPE_LOCATION:
                    sendLocationWithText(message.getDecryptedText(), media.getLocation());
                    break;

                case Media.TYPE_VOICE:
                    sendVoiceWithText(message.getDecryptedText(), media.getFile());
                    break;

                case Media.TYPE_AUDIO:
                case Media.TYPE_VIDEO:
                case Media.TYPE_FILE:
                    sendFileWithText(message.getDecryptedText(), media.getFileName(), media.getFileDesc());
                    break;

                case Media.TYPE_INVALID:
                    /*ignore*/
            }
        }
        else {
            sendTextOnly(message.getDecryptedText());
        }
    }

    /**
     * Attempt to reload the message
     * @param message    the message to resend
     * */
    public void reloadMessage(ManagedMessage message) {
        removeMessage(message.getMessageId());
        processMessage(message.getMessage());
    }

    /**
     * Updates the message with the passed id to failed status.
     * Updating the message should cause the list to send out an EventBus post about the update
     * @param manMsgId    the id of the message to be updated
     * */
    private void updateMsgFailed(long manMsgId) throws NullPointerException {
        updateMsgFailed(messageMap.get(manMsgId));
    }

    /**
     * Updates the message with the passed id to failed status.
     * Updating the message should cause the list to send out an EventBus post about the update
     * @param manMsg    the message to be update
     * */
    private void updateMsgFailed(ManagedMessage manMsg) throws NullPointerException {
        // updating the message in the list to failed status
//        try {
        manMsg.setTextStatus(ManagedMessage.STATUS_FAILED);
        manMsg.setMediaStatus(ManagedMessage.STATUS_FAILED);

        addMessage(manMsg);
//        }
//        catch (NullPointerException npe) {
//            Log.e("MsgManager", "Failed to update the message to failed status", npe);
//        }
    }

    /**
     * Updates the message the the passed id to in progress status.
     * Updating the message should cause the list to send out an EventBus post about the update
     * @param manMsgId    the id for the message to update
     * */
    private void updateMsgInProgress(long manMsgId) {
        updateMsgInProgress(messageMap.get(manMsgId));
    }

    /**
     * Updates the message the the passed id to in progress status.
     * Updating the message should cause the list to send out an EventBus post about the update
     * @param manMsg    the message to update
     * */
    private void updateMsgInProgress(ManagedMessage manMsg) {
        try {
            manMsg.setTextStatus(ManagedMessage.STATUS_IN_PROGRESS);
            manMsg.setMediaStatus(ManagedMessage.STATUS_IN_PROGRESS);

            addMessage(manMsg);
        }
        catch (NullPointerException npe) {
            Log.e("MsgManager", "Failed to update the message to in progress status", npe);
        }
    }

    /**
     * Updates the message with the passed id to ready/succeeded status.
     * Updating the message should cause the list to send out an EventBus post about the update
     * @param manMsgId    the id of the message to be updated
     * */
    private void updateMsgReady(long manMsgId) {
        updateMsgReady(messageMap.get(manMsgId));
    }

    /**
     * Updates the message with the passed id to ready/succeeded status.
     * Updating the message should cause the list to send out an EventBus post about the update
     * @param manMsg    the message to be update
     * */
    private void updateMsgReady(ManagedMessage manMsg) {
        // updating the message in the list to failed status
        try {
            manMsg.setTextStatus(ManagedMessage.STATUS_READY);
            manMsg.setMediaStatus(ManagedMessage.STATUS_READY);

            addMessage(manMsg);
        }
        catch (NullPointerException npe) {
            Log.e("MsgManager", "Failed to update the message to ready status", npe);
        }
    }


//--------------------------------------- loading methods ------------------------------------------

    /**
     * Adds a message to be loaded. Loading mean decrypting any text parts, as well as, downloading
     * and decrypting any media parts.
     * @param message   the message to be loaded
     * */
    private void processMessage(Message message) {
        if(message == null) return;

        // creating managed message
        ManagedMessage mMsg = createLoadingManagedMessage(message);

        // if the message maybe encrypted, try to decrypt it
        if(mMsg.hasEncryptedText()) {
            Log.e("yi", "processMessage: ================================= mMsg = " + mMsg );
            decryptTextMessage(mMsg);
        }

        // there is a message but is not encrypted, just set the original and update the status
        else if(mMsg.hasText()) {
            mMsg.setDecryptedText(mMsg.getOriginalMessage())
                    .setTextStatus(ManagedMessage.STATUS_READY);
            addMessage(mMsg);
        }
        // is there is no text, set the status to none
        else {
            mMsg.setTextStatus(ManagedMessage.STATUS_NONE);
            addMessage(mMsg);
        }

        // load any attachments
        if(mMsg.hasMedia()) {
            mMsg.setMediaStatus(ManagedMessage.STATUS_IN_PROGRESS);
            loadMediaAttachment(mMsg);
        }
        else {
            mMsg.setMediaStatus(ManagedMessage.STATUS_NONE);
            addMessage(mMsg);
        }

        // adding it to the list
        addMessage(mMsg);
    }

    /**
     * Attempts to decrypt a message. Decryption is done on a background thread pool thread.
     * If decryption is unsuccessful, the original message is used instead.
     * Upon completion the message status is updated to ready and an EventBus event about the
     * update.
     * @param managedMessage    the managed message to be decrypted
     * */
    @SuppressWarnings("ConstantConditions")
    private void decryptTextMessage(final ManagedMessage managedMessage) {
        // this could be costly depending on how long it takes to decrypt, so make sure to run
        //      in the backend
        ThreadPool.doTask(new Runnable() {
            @Override
            public void run() {
                try {
                    // try to decrypt using AES
                    if(managedMessage.getMessage().mayBeAesEncrypted()) {
                        AESCrypt aesCrypt = new AESCrypt(managedMessage.getSenderUsername());

                        // attempt to decrypt the message
                        String decryptedMessage = aesCrypt.decryptString(managedMessage.getOriginalMessage());
                        Log.e("yi", "run: =============================decryptedMessage  = " + decryptedMessage );
                        // if decryption worked, set the message text
                        managedMessage.setDecryptedText(decryptedMessage)
                                .setTextStatus(ManagedMessage.STATUS_READY);
                    }
                    // try to decrypt using the Codetel lib
                    else if(managedMessage.getMessage().mayBeCodetelEncrypted()) {
                        // create the key gen for this message
                        CSMPKeyGenerator keyGen = new CSMPIndexingKeyGenerator(
                                // since the managed message was created using a message, this will never be null
                                managedMessage.getSenderUsername().getBytes(),
                                managedMessage.getSubject().getBytes()
                        );

                        String encryptedMsg = managedMessage.getOriginalMessage();
                        Log.e("CSMPCrypt", String.format("MessageManager:--->%s--->%s--->%s",managedMessage.getSenderUsername(),managedMessage.getSubject(),encryptedMsg));
                        // attempt to decrypt the message
                        // String decryptedMessage = CSMPCryptStringUtil.decrypt(keyGen, encryptedMsg);
                        // if decryption worked, set the message text
                        managedMessage.setDecryptedText(encryptedMsg)
                                .setTextStatus(ManagedMessage.STATUS_READY);
                    }
                    else throw new Exception("Couldn't determine the decryption method");
                }
                catch (Exception e) {
                    Log.e(TAG, "Failed to decrypt message with id " + managedMessage.getMessageId(), e);

                    // in the case where decryption didn't work, just set the original message
                    managedMessage.setDecryptedText(managedMessage.getOriginalMessage())
                            .setTextStatus(ManagedMessage.STATUS_READY);
                }

                addMessage(managedMessage);
            }
        });
       /* AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // try to decrypt using AES
                    if(managedMessage.getMessage().mayBeAesEncrypted()) {
                        AESCrypt aesCrypt = new AESCrypt(managedMessage.getSenderUsername());

                        // attempt to decrypt the message
                        String decryptedMessage = aesCrypt.decryptString(managedMessage.getOriginalMessage());
                        Log.e("yi", "run: =============================decryptedMessage  = " + decryptedMessage );
                        // if decryption worked, set the message text
                        managedMessage.setDecryptedText(decryptedMessage)
                                .setTextStatus(ManagedMessage.STATUS_READY);
                    }
                    // try to decrypt using the Codetel lib
                    else if(managedMessage.getMessage().mayBeCodetelEncrypted()) {
                        // create the key gen for this message
                        CSMPKeyGenerator keyGen = new CSMPIndexingKeyGenerator(
                                // since the managed message was created using a message, this will never be null
                                managedMessage.getSenderUsername().getBytes(),
                                managedMessage.getSubject().getBytes()
                        );

                        String encryptedMsg = managedMessage.getOriginalMessage();
                        Log.e("CSMPCrypt", String.format("MessageManager:--->%s--->%s--->%s",managedMessage.getSenderUsername(),managedMessage.getSubject(),encryptedMsg));
                        // attempt to decrypt the message
                       // String decryptedMessage = CSMPCryptStringUtil.decrypt(keyGen, encryptedMsg);
                        // if decryption worked, set the message text
                        managedMessage.setDecryptedText(encryptedMsg)
                                .setTextStatus(ManagedMessage.STATUS_READY);
                    }
                    else throw new Exception("Couldn't determine the decryption method");
                }
                catch (Exception e) {
                    Log.e(TAG, "Failed to decrypt message with id " + managedMessage.getMessageId(), e);

                    // in the case where decryption didn't work, just set the original message
                    managedMessage.setDecryptedText(managedMessage.getOriginalMessage())
                            .setTextStatus(ManagedMessage.STATUS_READY);
                }

                addMessage(managedMessage);
            }
        });*/
    }

    /**
     * Downloads, decrypts, and stores media attachments
     * @param managedMessage    the managed message with media to load
     * */
    @SuppressWarnings("ConstantConditions")
    private void loadMediaAttachment(final ManagedMessage managedMessage) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                if(managedMessage == null || managedMessage.getMedia() == null) return;

                // for every media url that the message has
                // since the managed message was created using a message, this will never be null
                for(String mediaUrl : managedMessage.getMedia()) {
                    // extracts file name from URL and make a local file from it
                    String fileName = mediaUrl.substring(mediaUrl.lastIndexOf("/") + 1, mediaUrl.length());
                    File mediaFile = new File(
                            DeviceUtils.getEncryptedMediaDirectory(NetMeApp.getInstance()),
                            fileName
                    );
                    Log.i("yuyong_image", String.format("loadMediaAttachment:mediaUrl = %s , mediaFile = %s ",mediaUrl,mediaFile.getAbsolutePath()));
                    byte[] data;

                    // check if file has not been downloaded already, if not, download it
                    if(!mediaFile.exists()) {
                        try {
                            HttpDownloadUtility.downloadFile(
                                    mediaUrl,
                                    mediaFile.getParent(),
                                    mediaFile.getName()
                            );

                            data = Utils.getBytesFromFile(mediaFile);
                        }
                        catch (IOException ioe) {
                            Log.e("MsgManager", "Failed to download " + mediaUrl, ioe);

                            // delete the file so that retries won't read a potentially bad file
                            Log.d("MsgManager", "File deleted = " + mediaFile.delete());
                            try { updateMsgFailed(managedMessage); }
                            catch (NullPointerException npe ) {
                                Log.e("MsgManager", "Failed to update the message to failed status", npe);
                               // Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                    }
                    else data = Utils.getBytesFromFile(mediaFile);

                    try {
                        // decrypting the file
                        if(data != null && managedMessage.getMessage().mayBeAesEncrypted()) {
                            // decrypt the data
                            AESCrypt aesCrypt = new AESCrypt(managedMessage.getSenderUsername());
                            byte[] decryptedData = aesCrypt.decryptBytes(data);
                            // get the file information
                            Map<String, String> fMap = parseEncryptedFileName(aesCrypt, encryptedFileExtension, fileName);
                            String fName = fMap.get(FILE_NAME_KEY);
                            String fType = fMap.get(FILE_TYPE_KEY);
                            int type = Integer.parseInt(fType);

                            // create the media object
                            Media media = MediaFactory
                                    .setAESType(type)
                                    .setData(decryptedData, fName)
                                    .create();

                            // add the media object to the managed message
                            managedMessage.addDecryptedMedia(media);
                        }
                        else if(data != null && managedMessage.getMessage().mayBeCodetelEncrypted()) {
                            Log.i("yi_CSMP", String.format("loadMediaAttachment:mayBeCodetelEncrypted()"));
                            // decrypt the data
                            CSMPKeyGenerator keyGen = new CSMPIndexingKeyGenerator(
                                    managedMessage.getSenderUsername().getBytes(),
                                    managedMessage.getSubject().getBytes()
                            );
                            CSMPDecryptResult result = CSMPCrypt.decrypt(keyGen, data);
                            CSMPMetaData metaData = result.getMetaData();

                            // getting the type, adjusting for mislabeled images
                            boolean hasWidth = metaData.getWidth() > 0;
                            int type = metaData.getType();
                            type = hasWidth ? 2 : type; // if the result has a width, mark it as an image
                            type = type == 2 && !hasWidth ? 3 : type ; // if the result is labeled as an image but doesn't have a width, it's a miss labeled file

                            // special procedure for legacy location media
                            if(type == 4) {
                                managedMessage.addDecryptedMedia(
                                        MediaFactory.setCodetelType(type)
                                                .setCodetelLocation(metaData.getCoordinate())
                                                .create()
                                );
                            }
                            // add a new media item to the managed message
                            else {
                                managedMessage.addDecryptedMedia(
                                        MediaFactory.setCodetelType(type)
                                        .setData(result.getContents(), metaData.getFileName())
                                        .create()
                                );
                            }
                        }
                        // no data was obtained or decryption method undetermined, some error happened
                        else throw new Exception("No Data found");
                    }
                    catch (NullPointerException npe) {
                        Log.e(TAG, "Failed to make media object", npe);
                        try { updateMsgFailed(managedMessage); }
                        catch (NullPointerException nnpe) {
                            Log.e("MsgManager", "Failed to update the message to failed status", npe);
                          //  Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    catch (CSMPCryptException cce) {
                        Log.e(TAG, "Failed decrypt the media using Codetel", cce);
                        try { updateMsgFailed(managedMessage); }
                        catch (NullPointerException npe ) {
                            Log.e("MsgManager", "Failed to update the message to failed status", npe);
                         //   Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    catch (OutOfMemoryError ome) {
                        Log.wtf(TAG, "NOOO!! Out of Memory");
                        FlurryAgent.onError(TAG, "Out of Memory", ome);
                        /*TODO do something here about the memory issue*/
                        try { updateMsgFailed(managedMessage); }
                        catch (NullPointerException npe ) {
                            Log.e("MsgManager", "Failed to update the message to failed status", npe);
                         //   Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e) {
                        Log.e(TAG, "General failed decrypt the media", e);
                        try { updateMsgFailed(managedMessage); }
                        catch (NullPointerException npe ) {
                            Log.e("MsgManager", "Failed to update the message to failed status", npe);
                           // Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                }
                updateMsgReady(managedMessage);
            }
        });
    }

    /**
     * Stores a single media item in the application's cache directory
     * @param managedMessage    the message to save
     * @param mediaIndex        the media item to save
     * @param callback          used to alert that the caller about the result
     * */
    @SuppressWarnings("ConstantConditions,ResultOfMethodCallIgnored")
    public void saveDecryptedMediaInternally(final ManagedMessage managedMessage, final int mediaIndex, final MsgManagerCallback callback) {
        Log.i("yuyong", "saveDecryptedMediaInternally: cache directory");
        if(managedMessage == null || managedMessage.getMedia() == null) {
            Log.d("yuyong", "null media");
            callback.messageSavingResult(null, false);
        }

        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                String mediaUrl = managedMessage.getMedia()[mediaIndex];
                Log.d(TAG, mediaUrl);

                // extracts file name from URL and make a local file from it
                String fileName = mediaUrl.substring(mediaUrl.lastIndexOf("/") + 1, mediaUrl.length());
                //這是創建文件引用。而不是創建文件
                File mediaFile = new File(
                        DeviceUtils.getEncryptedMediaDirectory(NetMeApp.getInstance()),
                        fileName
                );
                Log.i("yuyong_image", String.format("saveDecryptedMediaInternally:mediaUrl--->%s----fileName--->%s---mediaFile--->%s",mediaUrl,fileName,mediaFile.getAbsolutePath()));


                if(!mediaFile.exists()) {
                    try {
                        HttpDownloadUtility.downloadFile(
                                mediaUrl,
                                mediaFile.getParent(),
                                mediaFile.getName()
                        );
                    }
                    catch (IOException ioe) {
                        Log.e("MsgManager", "Failed to download " + mediaUrl, ioe);

                        // delete the file so that retries won't read a potentially bad file
                        Log.d("MsgManager", "File deleted = " + mediaFile.delete());
                        try { updateMsgFailed(managedMessage); }
                        catch (NullPointerException npe ) {
                            Log.e("MsgManager", "Failed to update the message to failed status", npe);
                            // Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                }
                try {
                    byte[] fileData;

                    // get decrypted data
                    if(managedMessage.getMessage().mayBeAesEncrypted()) {
                        byte[] encryptedData = Utils.getBytesFromFile(mediaFile);

                        AESCrypt aesCrypt = null;
                        if (managedMessage.isFromContact()) {
                            aesCrypt = new AESCrypt(managedMessage.getSenderUsername());
                        }
                        else {
                            aesCrypt = new AESCrypt(NetMeApp.getCurrentUser());
                        }

                           /* // decrypt the data
                            AESCrypt aesCrypt = new AESCrypt(managedMessage.getSenderUsername());*/
                        fileData = aesCrypt.decryptBytes(encryptedData);
                        Log.i("yuyong", String.format("saveDecryptedMediaInternally:password:%s",managedMessage.getSenderUsername()));
                        // figure out the original file name

                        Map<String, String> fMap = parseEncryptedFileName(aesCrypt, encryptedFileExtension, fileName);
                        //  Map<String, String> fMap = parseEncryptedFileName(fileName);
                        fileName = fMap.get(FILE_NAME_KEY);
                        Log.i("yuyong", String.format("saveDecryptedMediaInternally:mediaFile:%s--->fileName:%s",mediaFile,fileName));
                    }
                    else if(managedMessage.getMessage().mayBeCodetelEncrypted()) {
                        Log.i("yi_CSMP", String.format("saveDecryptedMediaInternally:mayBeCodetelEncrypted()"));
                        CSMPDecryptResult result = decryptMedia(
                                managedMessage.getSenderUsername(),
                                managedMessage.getSubject(),
                                mediaFile);
                        fileData = result.getContents();
                        fileName = result.getMetaData().getFileName();

                    }
                    else fileData = Utils.getBytesFromFile(mediaFile);

                    // save the file to change and return
                    File saveFile = DeviceUtils.saveToCache(NetMeApp.getInstance(), fileData, fileName, true);
                    Log.i("yuyong", "saveFile ----> " + saveFile.getAbsolutePath());
                    callback.messageSavingResult(saveFile, saveFile.exists());
                }
                catch (Exception e) {
                    Log.d("yuyong", "Failed to save", e);
                    callback.messageSavingResult(null, false);
                }
               /* if(mediaFile.exists()) {
                    try {
                        byte[] fileData;

                        // get decrypted data
                        if(managedMessage.getMessage().mayBeAesEncrypted()) {
                            byte[] encryptedData = Utils.getBytesFromFile(mediaFile);

                            AESCrypt aesCrypt = null;
                            if (managedMessage.isFromContact()) {
                                aesCrypt = new AESCrypt(managedMessage.getSenderUsername());
                            }
                            else {
                                aesCrypt = new AESCrypt(NetMeApp.getCurrentUser());
                            }

                           *//* // decrypt the data
                            AESCrypt aesCrypt = new AESCrypt(managedMessage.getSenderUsername());*//*
                            fileData = aesCrypt.decryptBytes(encryptedData);
                            Log.i("yuyong", String.format("saveDecryptedMediaInternally:password:%s",managedMessage.getSenderUsername()));
                            // figure out the original file name

                            Map<String, String> fMap = parseEncryptedFileName(aesCrypt, encryptedFileExtension, fileName);
                          //  Map<String, String> fMap = parseEncryptedFileName(fileName);
                            fileName = fMap.get(FILE_NAME_KEY);
                            Log.i("yuyong", String.format("saveDecryptedMediaInternally:mediaFile:%s--->fileName:%s",mediaFile,fileName));
                        }
                        else if(managedMessage.getMessage().mayBeCodetelEncrypted()) {
                            CSMPDecryptResult result = decryptMedia(
                                    managedMessage.getSenderUsername(),
                                    managedMessage.getSubject(),
                                    mediaFile);
                            fileData = result.getContents();
                            fileName = result.getMetaData().getFileName();
                        }
                        else fileData = Utils.getBytesFromFile(mediaFile);

                        // save the file to change and return
                        File saveFile = DeviceUtils.saveToCache(NetMeApp.getInstance(), fileData, fileName, true);
                        Log.i("yuyong", "saveFile ----> " + saveFile.getAbsolutePath());
                        callback.messageSavingResult(saveFile, saveFile.exists());
                    }
                    catch (Exception e) {
                        Log.d("yuyong", "Failed to save", e);
                        callback.messageSavingResult(null, false);
                    }
                }
                else {
                    Log.d("yuyong", "File doesn't exist");
                    callback.messageSavingResult(null, false);
                }*/
            }
        });
    }

    /**
     * Stores a single media item in the device's public downloads folder
     * @param managedMessage    the message to save
     * @param mediaIndex        the media item to save
     * @param callback          used to alert that the caller about the result
     * */
    @SuppressWarnings("ConstantConditions,ResultOfMethodCallIgnored")
    public void saveDecryptedMediaExternally(final ManagedMessage managedMessage, final int mediaIndex, final MsgManagerCallback callback) {
        Log.i("yuyong", "saveDecryptedMediaInternally: downloads folder");
        if(managedMessage == null || managedMessage.getMedia() == null) {
            Log.d("yuyong", "Bad managed message passed");
            callback.messageSavingResult(null, false);
        }

        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                String mediaUrl = managedMessage.getMedia()[mediaIndex];

                // extracts file name from URL and make a local file from it
                String fileName = mediaUrl.substring(mediaUrl.lastIndexOf("/") + 1, mediaUrl.length());
                File mediaFile = new File(
                        DeviceUtils.getEncryptedMediaDirectory(NetMeApp.getInstance()),
                        fileName
                );

                if(mediaFile.exists()) {
                    try {
                        byte[] fileData;

                        // get decrypted data using AES 128
                        if(managedMessage.getMessage().mayBeAesEncrypted()) {
                            byte[] encryptedData = Utils.getBytesFromFile(mediaFile);

                            // decrypt the data
                            AESCrypt aesCrypt = new AESCrypt(managedMessage.getSenderUsername());
                            fileData = aesCrypt.decryptBytes(encryptedData);

                            // figure out the original file name
                            Map<String, String> fMap = parseEncryptedFileName(aesCrypt, encryptedFileExtension, fileName);
                            fileName = fMap.get(FILE_NAME_KEY);
                        }
                        // get decrypted data using the Codetel library
                        else if(managedMessage.getMessage().mayBeCodetelEncrypted()) {
                            Log.i("yi_CSMP", String.format("saveDecryptedMediaExternally:mayBeCodetelEncrypted()"));
                            CSMPDecryptResult result = decryptMedia(
                                    managedMessage.getSenderUsername(),
                                    managedMessage.getSubject(),
                                    mediaFile);
                            fileData = result.getContents();
                            fileName = result.getMetaData().getFileName();
                        }
                        // data may not be encrypted, just save it out
                        else fileData = Utils.getBytesFromFile(mediaFile);

                        // save file to downloads
                        DeviceUtils.saveToDownloads(NetMeApp.getInstance(), fileData, fileName);

                        callback.messageSavingResult(null, true);
                    }
                    catch (Exception e) {
                        Log.d(TAG, "Failed to save file", e);
                        Log.i("yuyong", "Failed to save file ---> " + e.getMessage());
                        callback.messageSavingResult(null, false);
                    }
                }
                else {
                    Log.d(TAG, "Managed message has no media");
                    Log.i("yuyong", "Managed message has no media ---> ");
                    callback.messageSavingResult(null, false);
                }
            }
        });
    }

    private CSMPDecryptResult decryptMedia(String sender, String salt, File mediaFile) throws Exception {
        // check the passed stuff
        if(mediaFile == null || !mediaFile.exists()) throw new FileNotFoundException("Passed media file not found");
        if(sender == null || sender.isEmpty()) throw new Exception("Bad sender username passed");
        if(salt == null || salt.isEmpty()) throw new Exception("Bad salt passed");

        // get the data
        byte[] data = Utils.getBytesFromFile(mediaFile);

        // decrypt data
        if(data != null && data.length > 0) {
            CSMPKeyGenerator keyGen = new CSMPIndexingKeyGenerator(sender.getBytes(), salt.getBytes());
            return CSMPCrypt.decrypt(keyGen, data);
        }
        // if data is bad, throw exception
        else throw new Exception("Failed to read data bytes");
    }


//--------------------------------------- sending methods ------------------------------------------


    /**
     * Attempts to send an image file along with text in a single message
     * @param text              the text message, can be null
     * @param imageName         the name to give to the image file being sent
     * @param descriptor    the image file
     * */
    public void sendImageWithText(@Nullable final String text, final String imageName, final FileDescriptor descriptor, final Uri uri) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                long manMsgId = -1;

                try {

                    ContentResolver resolver = mcontext.getContentResolver();
                    Cursor cursor = resolver.query(uri, null, null, null, null);
                    cursor.moveToFirst();
                    String fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    cursor.close();

                    String mimeType = resolver.getType(uri);
                    FileDescriptor fileDescriptor = resolver.openFileDescriptor(uri, "r").getFileDescriptor();
                    // get the file info
                    Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    Log.e("yuyong_media", "run: ========================== bitmap = " + bitmap );
                    ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageStream);
                    byte[] imageBytes = imageStream.toByteArray();

                    // making adjustments to image name if necessary
                    String adjustedImageName;
                    if(!imageName.endsWith(".jpeg")) {
                        int periodIndex = imageName.lastIndexOf(".");
                        adjustedImageName = imageName.substring(0, periodIndex) + ".jpeg";
                    }
                    else adjustedImageName = imageName;
                    Log.e("yuyong_media", "run: =========================== adjustedImageName = " + adjustedImageName );
                    // creating the message; message is automatically added to it's list
                    List<Media> mediaList = new ArrayList<>();
                    mediaList.add(new Media(bitmap, adjustedImageName, fileDescriptor,uri));

                    manMsgId = createSendingManagedMessage(text, mediaList).getId();
                    updateMsgInProgress(manMsgId);

                    sendImageWithText(manMsgId, text, adjustedImageName, imageBytes, bitmap);
                }
                catch (Exception e) {
                    Log.d("newChat", "Failed to do something", e);
                    try {
                        updateMsgFailed(manMsgId);
                    }
                    catch (NullPointerException npe ) {
                        Log.e("MsgManager", "Failed to update the message to failed status", npe);
                        Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Attempts to send an image file along with text in a single message
     * @param text         the text message, can be null
     * @param imageFile    the image file
     * */
    public void sendImageWithText(@Nullable final String text, final File imageFile) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                long manMsgId;

                try {
                    // get the file info
                    Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    byte[] imageBytes = Utils.getBytesFromFile(imageFile);
                    String imageName = imageFile.getName();

                    // creating the message; message is automatically added to it's list
                    List<Media> mediaList = new ArrayList<>();
                    mediaList.add(new Media(bitmap, imageName, imageFile));

                    manMsgId = createSendingManagedMessage(text, mediaList).getId();
                    updateMsgInProgress(manMsgId);

                    MyLog.writeLog("OkHttp","sendImageWithText================================");
                    sendImageWithText(manMsgId, text, imageName, imageBytes, bitmap);
                }
                /*TODO catch out of memory exception here and in the send file section*/
                catch (Exception e) {
                    Log.d("newChat", "Failed to do something", e);
                }
            }
        });
    }

    /**
     * Attempts to send an image file along with text in a single message
     * @param manMsgId      the id of the managed message to be update after
     * @param text          the text message
     * @param imageName     the name to be given to the image file being sent
     * @param imageBytes    the bytes of the image being sent
     * @param bitmap        the bitmap of the image being sent, needed to get the dimensions
     * */
    private void sendImageWithText(long manMsgId, @Nullable String text, String imageName, byte[] imageBytes, Bitmap bitmap) throws Exception {
        // encrypting the text
        String encryptedText = null;
        if (text != null && !text.isEmpty()) encryptedText = encryptAES.encryptString(text);

        // encrypting the image
        boolean imageByteCheck = imageBytes != null && imageBytes.length > 0;
        boolean imageSizeCheck = bitmap.getWidth() > 0 && bitmap.getHeight() > 0;
        if(imageByteCheck && !imageName.isEmpty() && imageSizeCheck) {
            String encryptedFileName = createEncryptedFileName(imageName, Media.TYPE_IMAGE);

             //encrypting
            byte[] encryptedImage = encryptAES.encryptBytes(imageBytes);

            // placing into request body, since this is just a byte array user the octet-stream mime
            RequestBody mediaRequestBody = RequestBody.create(
                    MediaType.parse("application/octet-stream"),
                    encryptedImage
            );
            MyLog.writeLog("OkHttp","sendImageWithText**********************************");
            // sending encryptedMessage
            sendEncryptedMessage(manMsgId, encryptedText, encryptedFileName, mediaRequestBody);
        }
        else throw new Exception();
    }

    /**
     * Attempts to send location coordinates along with text in a single message
     * @param text      the text message
     * @param latLng    the coordinates to send
     * */
    public void sendLocationWithText(@Nullable final String text, final LatLng latLng) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                long manMsgId = -1;

                try {
                    // creating the message; message is automatically added to it's list
                    List<Media> mediaList = new ArrayList<>();
                    mediaList.add(new Media(latLng));
                    manMsgId = createSendingManagedMessage(text, mediaList).getId();
                    updateMsgInProgress(manMsgId);

                    // encrypting the text
                    String encryptedText = null;
                    if (!TextUtils.isEmpty(text)) encryptedText = encryptAES.encryptString(text);

                    if (latLng != null) {
                        // create the name in which the backend will store the file
                        String encryptedName = createEncryptedFileName("location", Media.TYPE_LOCATION);

                        // encrypting the location
                        byte[] encryptedLocation = encryptAES.encryptLocation(latLng);

                        // placing into request body, since this is just a byte array user the octet-stream mime
                        RequestBody mediaRequestBody = RequestBody.create(
                                MediaType.parse("application/octet-stream"),
                                encryptedLocation
                        );

                        // sending encryptedMessage
                        sendEncryptedMessage(manMsgId, encryptedText, encryptedName, mediaRequestBody);
                    }
                    else throw new Exception();
                }
                catch (Exception e) {
                    Log.d("newChat", "Failed to do something", e);
                    try { updateMsgFailed(manMsgId); }
                    catch (NullPointerException npe ) {
                        Log.e("MsgManager", "Failed to update the message to failed status", npe);
                        Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Attempts to send a file along with text in a single message
     * @param text              the text message
     * @param fileName          the name to give the file being sent
     * @param fileDescriptor    the file
     * */
    @SuppressWarnings("ConstantConditions")
    public void sendFileWithText(@Nullable final String text, final String fileName, final FileDescriptor fileDescriptor) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                long manMsgId = -1;

                try {
                    // creating the message; message is automatically added to it's list
                    List<Media> mediaList = new ArrayList<>();
                    mediaList.add(new Media(fileDescriptor, fileName));
                    manMsgId = createSendingManagedMessage(text, mediaList).getId();
                    updateMsgInProgress(manMsgId);

                    // encrypting the text
                    String encryptedText = null;
                    if (!TextUtils.isEmpty(text)) encryptedText = encryptAES.encryptString(text);

                    // get the file data
                    byte[] fileBytes = Utils.getBytesFromFileDescriptor(fileDescriptor);

                    String encryptedName = createEncryptedFileName(fileName, Media.TYPE_FILE);

                    // encrypting the file
                    byte[] encryptedFile = encryptAES.encryptBytes(fileBytes);

                    // placing into request body, since this is just a byte array user the octet-stream mime
                    RequestBody mediaRequestBody = RequestBody.create(
                            MediaType.parse("application/octet-stream"),
                            encryptedFile
                    );

                    // sending encryptedMessage
                    sendEncryptedMessage(manMsgId, encryptedText, encryptedName, mediaRequestBody);
                }
                catch (Exception e) {
                    Log.d("newChat", "Failed to do something", e);
                    try { updateMsgFailed(manMsgId); }
                    catch (NullPointerException npe ) {
                        Log.e("MsgManager", "Failed to update the message to failed status", npe);
                        Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Attempts to send a voice file along with text in a single message
     * @param text    the text message
     * @param file    the file
     * */
    @SuppressWarnings("ConstantConditions")
    public void sendVoiceWithText(@Nullable final String text, final File file) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                long manMsgId = -1;

                try {
                    // creating the message; message is automatically added to it's list
                    List<Media> mediaList = new ArrayList<>();
                    mediaList.add(new Media(file));
                    manMsgId = createSendingManagedMessage(text, mediaList).getId();
                    updateMsgInProgress(manMsgId);
                    // encrypting the text
                    String encryptedText = null;
                    if (!TextUtils.isEmpty(text)) encryptedText = encryptAES.encryptString(text);

                    if(file != null && file.exists() && file.length() > 0) {
                        // get the file data
                        byte[] fileBytes = Utils.getBytesFromFile(file);

                        // creating the encrypted file name (the name the file will be saved as)
                        String encryptedName = createEncryptedFileName(file.getName(), Media.TYPE_VOICE);

                        // encrypting the file
                        byte[] encryptedFile = encryptAES.encryptBytes(fileBytes);

                        // placing into request body, since this is just a byte array user the octet-stream mime
                        RequestBody mediaRequestBody = RequestBody.create(
                                MediaType.parse("application/octet-stream"),
                                encryptedFile
                        );

                        // sending encryptedMessage
                        sendEncryptedMessage(manMsgId, encryptedText, encryptedName, mediaRequestBody);
                    }
                    else throw new Exception();
                }
                catch (Exception e) {
                    Log.d("newChat", "Failed to do something", e);
                    try { updateMsgFailed(manMsgId); }
                    catch (NullPointerException npe ) {
                        Log.e("MsgManager", "Failed to update the message to failed status", npe);
                        Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Attempts to send text only message
     * @param text    the text message
     * */
    public void sendTextOnly(final String text) {
        // creating the message; message is automatically added to it's list
        final long manMsgId = createSendingManagedMessage(text, null).getId();

        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // encrypting the text
                    String encryptedText = encryptAES.encryptString(text);

                    // sending message
                    sendEncryptedMessage(manMsgId, encryptedText, null, null);
                }
                catch (Exception e) {
                    Log.d("newChat", "Failed to do something", e);
                    try { updateMsgFailed(manMsgId); }
                    catch (NullPointerException npe ) {
                        Log.e("MsgManager", "Failed to update the message to failed status", npe);
                        Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Attempts to send the message after it has been encrypted
     * @param manMsgId         the id for the managed message
     * @param encryptedText    the text message after encryption
     * @param mediaName        the name of the media items
     * @param mediaBody        the encrypted media to send, can be null
     * */
    private void sendEncryptedMessage(final long manMsgId, @Nullable String encryptedText, @Nullable String mediaName, @Nullable RequestBody mediaBody) {
        MultipartBody.Builder mmBodyBuilder = new MultipartBody.Builder();
        Log.i("yuyong", "sendEncryptedMessage: ");
        // adding basic parts
        mmBodyBuilder.addFormDataPart("countrycode", manager.getCountryCallingCode());
        mmBodyBuilder.addFormDataPart("username", manager.getPhoneNumber());
        mmBodyBuilder.addFormDataPart("password", manager.getPassword());
        mmBodyBuilder.addFormDataPart("chatId", String.valueOf(chatId));

        // adding AES tag
        mmBodyBuilder.addFormDataPart("subject", AESTag);

        // adding text if there is any, can be empty
        if (encryptedText != null && !encryptedText.isEmpty()) {
            mmBodyBuilder.addFormDataPart("message", encryptedText);
        }
        // if not text included, add empty message to keep indexing accurate
        else mmBodyBuilder.addFormDataPart("message", "");

        // adding media if there is any, can be empty
        if (mediaName != null && !mediaName.isEmpty() && mediaBody != null) {
            mmBodyBuilder.addFormDataPart("media", mediaName, mediaBody);
            MyLog.writeLog("OkHttp","sendEncryptedMessage=================================================mediaName = " + mediaName);
        }
        // if no media included, add empty message to keep indexing accurate
        else {
            MyLog.writeLog("OkHttp","sendEncryptedMessage=================================================failed");
            mmBodyBuilder.addFormDataPart("media", "");
        }

        // building the body
        MultipartBody mmBody = mmBodyBuilder.build();

        // sending the message
        RestServiceGen.getUnCachedService()
                .sendMessage(
                        mmBody.part(0), // calling code part
                        mmBody.part(1), // phone number part
                        mmBody.part(2), // password part
                        mmBody.part(3), // chat id part
                        mmBody.part(4), // subject part
                        mmBody.part(5), // text part
                        mmBody.part(6)  // media part
                )
                .enqueue(new Callback<SendMessageResponse>() {
                    @Override
                    public void onResponse(Call<SendMessageResponse> call, Response<SendMessageResponse> response) {
                        Log.i("yuyong", "sendEncryptedMessage: ---->onResponse");
                        // the message was sent successfully, so update to ready
                        if(response.body() != null && response.body().isSuccess() && response.body().haveMsgs()) {
                            updateSentMessage(manMsgId, response.body().getMessage());

                            ChatMessageManager.addMessage(
                                    NetMeApp.getInstance(),
                                    response.body().getMessage()
                            );
                        }
                        // the message failed to be sent, so update to failed
                        else {
                            try {
                                MyLog.writeLog("OkHttp","onResponse=====================================");
                                updateMsgFailed(manMsgId);
                            }
                            catch (NullPointerException npe ) {
                                Log.e("MsgManager", "Failed to update the message to failed status", npe);
                                Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SendMessageResponse> call, Throwable t) {
                        // the message failed to be sent, so update to failed
                        try {
                            Log.i("yuyong",String.format("sendEncryptedMessage onFailure t %s",t.getMessage()));
                            updateMsgFailed(manMsgId);
                        }
                        catch (NullPointerException npe ) {
                            Log.i("yuyong",String.format("sendEncryptedMessage onFailure npe %s",npe.getMessage()));
                            Log.e("MsgManager", "Failed to update the message to failed status", npe);
                           // Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Loads a chat.
     * Adds all the messages to the message loader.
     * Uses the MsgManagerCallback to return the details about the chat, such as: chatId, participants, etc.
     * @param callback       callback update to return the chat details
     * @param loadInitial    if true, load first page of messages, else load message older then the current oldest
     * */
    public void loadChat(final MsgManagerCallback callback, boolean loadInitial) {
        Log.i("yuyong_Msg", "loadChat: ");
        // figure out what message to load
        int oldestMsgPos = messageIdList.size() - 1;
        String requestMsgId = !loadInitial && (oldestMsgPos > 0) ?
                String .valueOf(messageIdList.get(oldestMsgPos)) :
                "";

        // loading the local messages
        List<Message> messages = ChatMessageManager.getChatMessages(NetMeApp.getInstance(), chatId);

        if (messages != null) {
            for (Message message : messages) {
                if (!messageMap.containsKey(message.getId())) processMessage(message);
            }
            Chat chat = ChatTrackerManager.getChatWithChatId(NetMeApp.getInstance(),chatId);
            callback.onChatLoaded(chat,true);
        }
        loadChatList(callback, requestMsgId);

    }

    private void loadChatList(final MsgManagerCallback callback, String requestMsgId) {
        Log.i("yuyong_message", "loadChatList: ");
        // loading from the server
        RestServiceGen.getCachedService()
                .getChat(
                        manager.getCountryCallingCode(),
                        manager.getPhoneNumber(),
                        manager.getPassword(),
                        chatId,
                        requestMsgId
                )
                .enqueue(new Callback<GetChatResponse>() {
                    @Override
                    public void onResponse(Call<GetChatResponse> call, Response<GetChatResponse> response) {
                        // successfully loaded the chat, pass all the messages to be loaded and return chat details in callback
                        if (response != null && response.body() != null && response.body().isSuccess()) {
                            Chat chat = response.body().getChat();

                            // add all the messages but only if the message has not been loaded yet
                            for (Message message : chat.getMessages()) {
                                if (!messageMap.containsKey(message.getId()))
                                    processMessage(message);
                            }

                            // add all the messages to the database
                            ChatMessageManager.addMessages(NetMeApp.getInstance(), chat.getMessages());
                            if (callback == null) {
                                return;
                            }
                            callback.onChatLoaded(chat,false);
                        }
                        // failed to load chat, alert user with the error message from the server
                        else if (response != null && response.body() != null) {
                            callback.onChatFailed(response.body().getErrorMsg());
                        }
                        // failed to load the chat, bad response
                        else callback.onChatFailed(null);
                    }

                    @Override
                    public void onFailure(Call<GetChatResponse> call, Throwable t) {
                        // failed to load chat
                        callback.onChatFailed(null);
                    }
                });
    }



    /**
     * Loads a specific message.
     * Adds the message to be loaded by message manager
     * @param messageId    the id of the message to load
     * */
    private void loadMessage(long messageId) {
        final ManagedMessage manMsg = createReceivedManagedMessage(messageId);

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
                        if(response.body() != null && response.body().isSuccess()) {
                            Message message = response.body().getMessage();
                            Log.e("yi", "onResponse: =========================================loadMessage : " + message.getMessage() + ","  + message.getSubject());
                            processMessage(message);

                            ChatMessageManager.addMessage(NetMeApp.getInstance(), message);
                        }
                        // failed to load details, show failed
                        else {
                            try {
                                updateMsgFailed(manMsg);
                            }
                            catch (NullPointerException npe ) {
                                Log.e("MsgManager", "Failed to update the message to failed status", npe);
                                Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetChatMessageResponse> call, Throwable t) {
                        try { updateMsgFailed(manMsg); }
                        catch (NullPointerException npe ) {
                            Log.e("MsgManager", "Failed to update the message to failed status", npe);
                            Toast.makeText(NetMeApp.getInstance(), R.string.error_send, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Requests that the message be deleted from the back end.
     * @param messageId    the id for the message to be removed
     * */
    public void deleteMessage(final long messageId) {
        updateMsgInProgress(messageId);

        RestServiceGen.getUnCachedService()
                .removeChatMessage(
                        manager.getCountryCallingCode(),
                        manager.getPhoneNumber(),
                        manager.getPassword(),
                        chatId,
                        messageId
                )
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        // successfully removed the message in the backend, so remove locally
                        if(response.body() != null && response.body().isSuccess()) {
                            removeMessage(messageId);
                            Message msg = ChatMessageManager.deleteMessage(NetMeApp.getInstance(), messageId);

                            // remove any media associated with the message
                            if (msg != null && msg.hasMedia()) {
                                String mediaUrl = msg.getMedia()[0];
                                // extracts file name from URL and make a local file from it
                                String fileName = mediaUrl.substring(mediaUrl.lastIndexOf("/") + 1, mediaUrl.length());

                                DeviceUtils.deleteEncryptedMediaFile(fileName);
                            }

                        }
                        // failed to remove the message from the backend, so return to normal locally
                        else {
                            updateMsgReady(messageId);
                            showErrorToast(NetMeApp.getInstance().getString(R.string.error_chat_delete));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        // failed to remove the message from the backend, so return to normal locally
                        updateMsgReady(messageId);
                        showErrorToast(NetMeApp.getInstance().getString(R.string.error_chat_delete));
                    }
                });
    }

    /**
     * Updates a message with new text using the original subject for encryption
     * @param manMsg      the original message to update
     * @param newText      the new text to update the message with
     * */
    @SuppressWarnings("ConstantConditions")
    public void editMessageText(final ManagedMessage manMsg, final String newText) {
        updateMsgInProgress(manMsg.getMessageId());

        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // try to encrypt the message
                    String newEncryptedText = encryptAES.encryptString(newText);

                    // sending
                    editMessage(manMsg, newText, newEncryptedText);
                }
                catch (Exception e) {
                    e.printStackTrace();

                    // message failed to encrypt with the new stuff, so return to normal and update the user
                    updateMsgReady(manMsg.getMessageId());
                    showErrorToast(NetMeApp.getInstance().getString(R.string.error_chat_edit));
                }

            }
        });
    }

    /**
     * Updates a message with new encrypted text
     * @param manMsg              the managed message to update
     * @param newText             the new text to use
     * @param newEncryptedText    the new encrypted text to use
     * */
    private void editMessage(final ManagedMessage manMsg, final String newText, final String newEncryptedText) {
        RestServiceGen.getUnCachedService()
                .editChatMessage(
                        manager.getCountryCallingCode(),
                        manager.getPhoneNumber(),
                        manager.getPassword(),
                        manMsg.getChatId(),
                        manMsg.getMessageId(),
                        newEncryptedText
                )
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        // successfully update the message on the backend, so update the message
                        //      with the new stuff
                        if(response.body() != null && response.body().isSuccess()) {
                            ChatMessageManager.addMessage(
                                    NetMeApp.getInstance(),
                                    manMsg.getMessageId(),
                                    manMsg.getChatId(),
                                    manMsg.getSenderUsername(),
                                    manMsg.getSubject(),
                                    manMsg.getDate(),
                                    newEncryptedText,
                                    Arrays.toString(manMsg.getMedia())
                            );

                            manMsg.setOriginalMessage(newEncryptedText);
                            manMsg.setDecryptedText(newText);
                            updateMsgReady(manMsg);
                        }
                        // failed to update the message, so update the message back to original
                        else {
                            updateMsgReady(manMsg);
                            showErrorToast(NetMeApp.getInstance().getString(R.string.error_chat_edit));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        // failed to update the message, so update the message back to original
                        updateMsgReady(manMsg);
                        showErrorToast(NetMeApp.getInstance().getString(R.string.error_chat_edit));
                    }
                });
    }


//------------------------------------------ chat starting methods ---------------------------------


    /**
     * Starts a new chat with the invited user(s) containing an image (and maybe a text message).
     * Uses a callback to return the details of the chat
     * @param invited       the user(s) invited to be a part of the chat
     * @param text          the text message to use as part of the initial message (optional)
     * @param imageFile     a file of an image to use as part of the initial message
     * @param callback      callback used to return the details of the chat
     * */
    public void startChatWithImage(final String invited, final String text, final File imageFile,  final MsgManagerCallback callback) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                long manMsgId = -1;
                Log.e("yuyong", "startChatWithImage : 4 ----- ");
                try {
                    // get the file info
                    Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    byte[] imageBytes = Utils.getBytesFromFile(imageFile);
                    String imageName = imageFile.getName();

                    List<Media> mediaList = new ArrayList<>();
                    mediaList.add(new Media(Media.TYPE_IMAGE, imageFile));

                    manMsgId = createSendingManagedMessage(text, mediaList).getId();
                    updateMsgInProgress(manMsgId);

                    startChatWithImage(manMsgId, invited, text, imageName, imageBytes, bitmap, callback);
                }
                catch (Exception e) {
                    Log.d("newChat", "Failed to do something", e);
                    removeMessage(manMsgId);
                    callback.onChatFailed(null);
                }
            }
        });
    }

    /**
     * Starts a new chat with the invited user(s) containing an image (and maybe a text message).
     * Uses a callback to return the details of the chat
     * @param invited       the user(s) invited to be a part of the chat
     * @param text          the text message to use as part of the initial message (optional)
     * @param imageName     the name of the image
     * @param descriptor    the file to send
     * @param callback      callback used to return the details of the chat
     * */
    public void startChatWithImage(final String invited, final String text, final String imageName, final FileDescriptor descriptor,final Uri uri, final MsgManagerCallback callback) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                long manMsgId = -1;
                //
                Log.e("yuyong", "startChatWithImage : 6 ----- ");
                try {
                    // get the file info
                    ContentResolver resolver = mcontext.getContentResolver();
                    Cursor cursor = resolver.query(uri, null, null, null, null);
                    cursor.moveToFirst();
                    String fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    cursor.close();

                    String mimeType = resolver.getType(uri);
                    FileDescriptor fileDescriptor = resolver.openFileDescriptor(uri, "r").getFileDescriptor();

                    Log.i("yuyong",String.format("startChatWithImage try %s",fileDescriptor));

                    Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);

                    Log.i("yuyong",String.format("startChatWithImage %d",bitmap.hashCode()));
                    ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageStream);
                    byte[] imageBytes = imageStream.toByteArray();

                    // making adjustments to image name if necessary
                    String adjustedImageName;
                    if(!imageName.endsWith(".jpeg")) {
                        int periodIndex = imageName.lastIndexOf(".");
                        adjustedImageName = imageName.substring(0, periodIndex) + ".jpeg";
                    }
                    else adjustedImageName = imageName;

                    // creating the message; message is automatically added to it's list
                    List<Media> mediaList = new ArrayList<>();
                    mediaList.add(new Media(bitmap, adjustedImageName, fileDescriptor,uri));

                    manMsgId = createSendingManagedMessage(text, mediaList).getId();
                    updateMsgInProgress(manMsgId);

                    startChatWithImage(manMsgId, invited, text, adjustedImageName, imageBytes, bitmap, callback);
                }
                catch (Exception e) {
                    Log.i("yuyong",String.format("startChatWithImage-->%s",e.getMessage()));
                    removeMessage(manMsgId);
                    callback.onChatFailed(null);
                }
            }
        });
    }

    /**
     * Starts a new chat with the invited user(s) containing an image (and maybe a text message).
     * Uses a callback to return the details of the chat
     * @param manMsgId      the id of the managed message representing this message to be sent
     * @param invited       the user(s) invited to be a part of the chat
     * @param text          the text message to use as part of the initial message (optional)
     * @param imageName     the name of the image
     * @param imageBytes    a byte array of the image to send
     * @param bitmap        a bitmap of the image to send
     * @param callback      callback used to return the details of the chat
     * */
    private void startChatWithImage(long manMsgId, String invited, String text, String imageName, byte[] imageBytes, Bitmap bitmap, MsgManagerCallback callback) throws Exception {
        // encrypting the text
        String encryptedText = null;
        if (!TextUtils.isEmpty(text)) {
            encryptedText = encryptAES.encryptString(text);
        }
        Log.e("yuyong", "startChatWithImage : 7 ----- ");
        // encrypting the image
        boolean imageByteCheck = imageBytes != null && imageBytes.length > 0;
        boolean imageSizeCheck = bitmap.getWidth() > 0 && bitmap.getHeight() > 0;
        if(imageByteCheck && !imageName.isEmpty() && imageSizeCheck) {
            // create the name in which the backend will store the file
            String encryptedFileName = createEncryptedFileName(imageName, Media.TYPE_IMAGE);
            // encrypting bytes
            byte[] encryptedImage = encryptAES.encryptBytes(imageBytes);
            // placing into request body, since this is just a byte array user the octet-stream mime
            RequestBody mediaRequestBody = RequestBody.create(
                    MediaType.parse("application/octet-stream"),
                    encryptedImage
            );

            // sending encryptedMessage
            startChat(manMsgId, invited, encryptedText, encryptedFileName, mediaRequestBody, callback);
        }
        else throw new Exception();
    }

    /**
     * Starts a new chat with the invited user(s) containing a location (and maybe a text message).
     * Uses a callback to return the details of the chat
     * @param invited       the user(s) invited to be a part of the chat
     * @param text          the text message to use as part of the initial message (optional)
     * @param latLng        the location to use as part of the initial message
     * @param callback      callback used to return the details of the chat
     * */
    public void startChatWithLocation(final String invited, final String text, final LatLng latLng, final MsgManagerCallback callback) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                long manMsgId = -1;

                try {
                    List<Media> mediaList = new ArrayList<>();
                    mediaList.add(new Media(latLng));

                    manMsgId = createSendingManagedMessage(text, mediaList).getId();
                    updateMsgInProgress(manMsgId);

                    // encrypting the text
                    String encryptedText = null;
                    if (!TextUtils.isEmpty(text)) encryptedText = encryptAES.encryptString(text);

                    if (latLng != null) {
                        // create the name in which the backend will store the file
                        String encryptedName = createEncryptedFileName("location", Media.TYPE_LOCATION);

                        // encrypting the location
                        byte[] encryptedLocation = encryptAES.encryptLocation(latLng);

                        // placing into request body, since this is just a byte array user the octet-stream mime
                        RequestBody mediaRequestBody = RequestBody.create(
                                MediaType.parse("application/octet-stream"),
                                encryptedLocation
                        );

                        // sending encryptedMessage
                        startChat(manMsgId, invited, encryptedText, encryptedName, mediaRequestBody, callback);
                    }
                    else throw new Exception();
                }
                catch (Exception e) {
                    Log.d("newChat", "Failed to do something", e);
                    removeMessage(manMsgId);
                    callback.onChatFailed(null);
                }
            }
        });
    }

    /**
     * Starts a new chat with the invited user(s) containing a file (and maybe a text message).
     * Uses a callback to return the details of the chat
     * @param invited       the user(s) invited to be a part of the chat
     * @param text          the text message to use as part of the initial message (optional)
     * @param fileName      the name of the included file
     * @param descriptor    the file to send
     * @param callback      callback used to return the details of the chat
     * */
    public void startChatWithFile(final String invited, final String text, final String fileName, final FileDescriptor descriptor, final MsgManagerCallback callback) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                long manMsgId = -1;

                try {
                    List<Media> mediaList = new ArrayList<>();
                    mediaList.add(new Media(Media.TYPE_FILE, fileName));

                    manMsgId = createSendingManagedMessage(text, mediaList).getId();
                    updateMsgInProgress(manMsgId);

                    // encrypting the text
                    String encryptedText = null;
                    if (!TextUtils.isEmpty(text)) encryptedText = encryptAES.encryptString(text);

                    byte[] fileBytes = Utils.getBytesFromFileDescriptor(descriptor);

                    if(fileBytes != null && fileBytes.length > 0 && fileName != null && !fileName.isEmpty()) {
                        // create the name in which the backend will store the file
                        String encryptedFileName = createEncryptedFileName(fileName, Media.TYPE_FILE);

                        // encrypting the file
                        byte[] encryptedFile = encryptAES.encryptBytes(fileBytes);

                        // placing into request body, since this is just a byte array user the octet-stream mime
                        RequestBody mediaRequestBody = RequestBody.create(
                                MediaType.parse("application/octet-stream"),
                                encryptedFile
                        );

                        // start chat
                        startChat(manMsgId, invited, encryptedText, encryptedFileName, mediaRequestBody, callback);
                    }
                    else throw new Exception();
                }
                catch (Exception e) {
                    Log.d("newChat", "Failed to do something", e);
                    removeMessage(manMsgId);
                    callback.onChatFailed(null);
                }
            }
        });
    }

    /**
     * Starts a new chat with the invited user(s) containing a voice message (and maybe a text message).
     * Uses a callback to return the details of the chat
     * @param invited       the user(s) invited to be a part of the chat
     * @param text          the text message to use as part of the initial message (optional)
     * @param voiceFile     a voice message file to use as part of the initial message
     * @param callback      callback used to return the details of the chat
     * */
    @SuppressWarnings("ConstantConditions")
    public void startChatWithVoice(final String invited, final String text, final File voiceFile, final MsgManagerCallback callback) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                long manMsgId = -1;

                try {
                    List<Media> mediaList = new ArrayList<>();
                    mediaList.add(new Media(Media.TYPE_FILE, voiceFile));

                    manMsgId = createSendingManagedMessage(text, mediaList).getId();
                    updateMsgInProgress(manMsgId);

                    // encrypting the text
                    String encryptedText = null;
                    if (!TextUtils.isEmpty(text)) encryptedText = encryptAES.encryptString(text);

                    if(voiceFile != null && voiceFile.exists() && voiceFile.length() > 0) {
                        // get the file data
                        byte[] fileBytes = Utils.getBytesFromFile(voiceFile);
                        String fileName = voiceFile.getName();

                        // create the name in which the backend will store the file
                        String encryptedFileName = createEncryptedFileName(fileName, Media.TYPE_VOICE);

                        // encrypting the file
                        byte[] encryptedFile = encryptAES.encryptBytes(fileBytes);

                        // placing into request body, since this is just a byte array user the octet-stream mime
                        RequestBody mediaRequestBody = RequestBody.create(
                                MediaType.parse("application/octet-stream"),
                                encryptedFile
                        );

                        // start chat
                        startChat(manMsgId, invited, encryptedText, encryptedFileName, mediaRequestBody, callback);
                    }
                    else throw new Exception();
                }
                catch (Exception e) {
                    Log.d("newChat", "Failed to do something", e);
                    removeMessage(manMsgId);
                    callback.onChatFailed(null);
                }
            }
        });
    }

    /**
     * Starts a new chat with the invited user(s) containing only text.
     * Uses a callback to return the details of the chat
     * @param invited       the user(s) invited to be a part of the chat
     * @param text          the text message to use as part of the initial message (optional)
     * @param callback      callback used to return the details of the chat
     * */
    public void startChatWithTextOnly(final String invited, final String text, final MsgManagerCallback callback) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                long manMsgId = -1;

                try {
                    manMsgId = createSendingManagedMessage(text, null).getId();
                    updateMsgInProgress(manMsgId);

                    // encrypting the text
                    String encryptedText = encryptAES.encryptString(text);

                    // start chat
                    startChat(manMsgId, invited, encryptedText, null, null, callback);
                }
                catch (Exception e) {
                    Log.d("newChat", "Failed to do something", e);
                    removeMessage(manMsgId);
                    callback.onChatFailed(null);
                }
            }
        });
    }

    /**
     * Starts a new chat with an invited user or users which contains the passed message as the first message.
     * @param manMsgId           the {@link ManagedMessage} to be sent
     * @param invited          the invited user(s)
     * @param encryptedText    the text to send in it's encrypted format
     * @param mediaName        the name for the attached media
     * @param mediaBody        a {@link RequestBody} containing any media to send
     * @param callback         a callback used to return the chat details (id, participants, etc.)
     * */
    private void startChat(final long manMsgId, String invited, String encryptedText, String mediaName, RequestBody mediaBody, final MsgManagerCallback callback) {
        MultipartBody.Builder mmBodyBuilder = new MultipartBody.Builder();
        Log.i("yuyong", "startChat: ----->6");
        // adding basic parts
        mmBodyBuilder.addFormDataPart("countrycode", manager.getCountryCallingCode());
        mmBodyBuilder.addFormDataPart("username", manager.getPhoneNumber());
        mmBodyBuilder.addFormDataPart("password", manager.getPassword());
        mmBodyBuilder.addFormDataPart("invite", invited);

        // adding the AES tag
        mmBodyBuilder.addFormDataPart("subject", AESTag);

        // adding text if there is any, can be empty
        if (encryptedText != null && !encryptedText.isEmpty()) {
            mmBodyBuilder.addFormDataPart("message", encryptedText);
        }
        // if not text included, add empty message to keep indexing accurate
        else mmBodyBuilder.addFormDataPart("message", "");

        // adding media if there is any, can be empty
        if (mediaName != null && !mediaName.isEmpty() && mediaBody != null) {
            mmBodyBuilder.addFormDataPart("media", mediaName, mediaBody);
        }
        // if no media included, add empty message to keep indexing accurate
        else mmBodyBuilder.addFormDataPart("media", "");

        // building the body
        MultipartBody mmBody = mmBodyBuilder.build();

        // start the chat
        RestServiceGen.getUnCachedService()
                .startChat(
                        mmBody.part(0), // calling code part
                        mmBody.part(1), // phone number part
                        mmBody.part(2), // password part
                        mmBody.part(3), // invited user part
                        mmBody.part(4), // subject part
                        mmBody.part(5), // text part
                        mmBody.part(6)  // media part
                )
                .enqueue(new Callback<StartChatResponse>() {
                    @Override
                    public void onResponse(Call<StartChatResponse> call, Response<StartChatResponse> response) {
                        Log.i("yuyong", "startChat: ----->onResponse" );
                        // if the response is successful
                        if(response != null && response.body() != null && response.body().isSuccess() && response.body().haveMsgs()) {
                            StartChatResponse scr = response.body();

                            // update the message
                            updateSentMessage(manMsgId, scr.getMessage());

                            // update the database
                            ChatMessageManager.addMessage(NetMeApp.getInstance(), scr.getMessage());

                            // setting the new chatId
                            chatId = response.body().getChatId();

                            // send the callback message
                            callback.onNewChatStarted(scr.getChatId(), scr.getParticipants());
                        }
                        // use the callback to tell the chat activity that the load failed
                        else {
                            callback.onChatFailed(
                                    response != null && response.body() != null ?
                                            response.body().getErrorMsg() :
                                            null
                            );

                            // since this is starting a new chat, the chat id used to create this is invalid
                            //      so just go ahead and remove
                            removeMessage(manMsgId);
                        }
                    }

                    @Override
                    public void onFailure(Call<StartChatResponse> call, Throwable t) {
                        removeMessage(manMsgId);
                        Log.i("yuyong", "onChatFailed: ============>>>>>>>" + t.getMessage());
                        callback.onChatFailed(null);
                    }
                });
    }
}

