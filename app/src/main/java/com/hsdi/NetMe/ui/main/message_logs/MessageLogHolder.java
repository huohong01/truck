package com.hsdi.NetMe.ui.main.message_logs;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.flurry.android.FlurryAgent;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.database.ChatTrackerManager;
import com.hsdi.NetMe.models.Chat;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.models.Message;
import com.hsdi.NetMe.models.Participant;
import com.hsdi.NetMe.ui.chat.ChatActivity;
import com.hsdi.NetMe.ui.chat.util.MessageManager;
import com.hsdi.NetMe.util.AESCrypt;
import com.hsdi.NetMe.util.CircleTransformation;
import com.hsdi.theme.basic.BaseThemeActivity;
import com.macate.csmp.CSMPCryptException;
import com.macate.csmp.CSMPIndexingKeyGenerator;
import com.macate.csmp.CSMPKeyGenerator;
import com.squareup.picasso.Picasso;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

class MessageLogHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.messages_content_container)
    View contentContainer;
    @Bind(R.id.messages_avatar)
    ImageView avatar;
    @Bind(R.id.messages_avatar_letter)
    TextView avatarLetter;
    @Bind(R.id.messages_name)
    TextView name;
    @Bind(R.id.messages_content)
    TextView content;
    @Bind(R.id.messages_date)
    TextView date;
    @Bind(R.id.messages_time)
    TextView time;
    @Bind(R.id.messages_delete)
    View delete;

    final SwipeRevealLayout swipeLayout;
    private final Context context;
    private Chat chat;

    MessageLogHolder(SwipeRevealLayout itemView, final MessageLogFragment msgFragment) {
        super(itemView);
        this.context = itemView.getContext();
        ButterKnife.bind(this, itemView);

        swipeLayout = itemView;
        swipeLayout.setLockDrag(true);

        // on long click, toggle the open/close state of the list
        contentContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                swipeLayout.open(true);
                return true;
            }
        });

        // if open, close. else start the chat
        contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swipeLayout.isOpened()) swipeLayout.close(true);
                else {
                    //starting chat activity
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra(ChatActivity.EXTRA_CHAT_TYPE, ChatActivity.TYPE_TEXT_ONLY);
                    intent.putExtra(ChatActivity.EXTRA_CHAT_ID, chat.getChatId());
                    context.startActivity(intent);
                }
            }
        });

        //setting on delete click listener
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perform the remove request
                msgFragment.removeChat(chat);
            }
        });
    }

    /**
     * Sets up this the view with the correct information from the chat
     *
     * @param chat the meeting to get info from
     */
    public void bind(final Chat chat) {
        this.chat = chat;

        setName();
        setAvatar();
        setTime();
        setContent();
        setReadStatus();
    }

    /**
     * Sets the Name for the message
     */
    private void setName() {
        //get the contact
        Contact contact = null;
        Participant participant = chat.getFirstOtherParticipant(context);
        if (participant != null) contact = NetMeApp.getContactWith(participant.getUsername());

        String chatName = ChatTrackerManager.getChatName(context, chat.getChatId());
        Log.i("yuyong_contact", "setName: " + participant + ",chatName = " + chatName + ",chat.isGroupChat() = " + chat.isGroupChat());
        //setting the contact's name
        if (!TextUtils.isEmpty(chatName)) name.setText(chatName);
        else if (chat.isGroupChat()) name.setText(R.string.group_title);
        else if (contact != null && contact.getName() != null) name.setText(contact.getName());
        else if (participant != null) {
            String nameText = participant.getFirstName() + " " + participant.getLastName();
            /*TODO change to also use username if no name set*/
            name.setText(nameText);
        } else name.setText(R.string.deleted_user_name);
    }

    /**
     * Sets the avatar for the message
     */
    private void setAvatar() {
        //get the other participant
        Participant participant = chat.getFirstOtherParticipant(context);
        avatarLetter.setText("");

        //use the group chat avatar if this chat has more then 2 participants
        if (chat.isGroupChat()) {
            avatar.setImageResource(R.drawable.group_avatar);
            ((BaseThemeActivity) context).applyTheme(avatar, R.drawable.group_avatar);
        }
        //if there is no other participants/other participants deleted their accounts show deleted user values
        else if (chat.hasNoOtherUsers()) {
            avatar.setImageResource(R.drawable.empty_avatar);
            ((BaseThemeActivity) context).applyTheme(avatar, R.drawable.empty_avatar);

            avatar.setColorFilter(R.color.msg_deleted_user);
        } else if (participant != null) {
            //try to find a matching contact
            Contact contact = NetMeApp.getContactWith(participant.getUsername());

            //try to load the local avatar or just use the no avatar default image for this app
            if (contact != null && contact.getAvatarUri() != null) {
                Uri avatarUri = contact.getAvatarUri();
                Log.i("yuyong_profile", String.format("MSG:bind: avatarUri = %s ", avatarUri));
                ((BaseThemeActivity) context).applyTheme(avatar,R.drawable.empty_avatar);
                Picasso.with(context)
                        .load(avatarUri)
                        .transform(new CircleTransformation())
                        .placeholder(R.drawable.empty_avatar)
                        .into(avatar);
            }
            //load the url from the server unless it is the default avatar
            else if (participant.getAvatarUrl() != null && !participant.getAvatarUrl().contains(context.getString(R.string.default_avatar))) {
                String avatarUrl = participant.getAvatarUrl();
                Log.i("yuyong_profile", String.format("MSG:bind: avatarUrl = %s ", avatarUrl));
                /*TODO switch this to use the "getSecureImage" url*/
                ((BaseThemeActivity) context).applyTheme(avatar,R.drawable.empty_avatar);
                Picasso.with(context)
                        .load(avatarUrl)
                        .transform(new CircleTransformation())
                        .placeholder(R.drawable.empty_avatar)
                        .into(avatar);
            } else {
                avatar.setImageResource(R.drawable.empty_avatar);
                ((BaseThemeActivity) context).applyTheme(avatar, R.drawable.empty_avatar);
                Log.i("yuyong_profile", String.format("MSG:bind: empty_avatar"));
                if (contact != null) avatarLetter.setText(contact.getInitials());
                else avatarLetter.setText(participant.getInitials());
            }
        } else {
            avatar.setImageResource(R.drawable.empty_avatar);
            ((BaseThemeActivity) context).applyTheme(avatar, R.drawable.empty_avatar);
        }
    }

    /**
     * Sets the date and time for the latest message from the chat
     */
    private void setTime() {
        //setting latest message content
        date.setText(chat.getFormattedLatestDate());
        time.setText(chat.getFormattedLatestHour());
    }

    /**
     * Set the value of the message
     */
    private void setContent() {
        //get the latest message
        Message message = chat.getLatestMessage();
        String decryptedMessage = message.getMessage();
        Log.e("yi", "setContent: ==================== decryptedMessage = " + decryptedMessage);
        try {
            // if the message has media and is using AES, figure out what is there from the
            //      file name in the url
            if (message.hasMedia() && message.mayBeAesEncrypted()) {
                String mediaUrl = message.getMedia()[0];

                // parse out the message name parts
                Map<String, String> fMap = MessageManager.parseEncryptedFileName(
                        new AESCrypt(message.getSenderUsername()),
                        context.getString(R.string.encrypted_file_extension),
                        mediaUrl.substring(mediaUrl.lastIndexOf("/") + 1, mediaUrl.length())
                );

                // just use the name of the file
                decryptedMessage = fMap.get(MessageManager.FILE_NAME_KEY);

            }
            // if the message contains media attachments and is not using a AES encryption
            //      system, use the default attachment message
            else if (message.hasMedia())
                decryptedMessage = context.getString(R.string.message_attachment);
                // if the message may be encrypted with AES try to decrypt
            else if (message.mayBeAesEncrypted()) {
                AESCrypt aesCrypt = new AESCrypt(message.getSenderUsername());
                decryptedMessage = aesCrypt.decryptString(message.getMessage());
                Log.i("CSMPCrypt", String.format("setContent: decryptedMessage--->%s", decryptedMessage));
            }
            // if the message may be encrypted with Codetel try to decrypt
            else if (message.mayBeCodetelEncrypted()) {
                Log.i("CSMPCrypt", String.format("setContent: --->%s--->%s--->%s", message.getSenderUsername(), message.getSubject(), message.getMessage()));
                CSMPKeyGenerator keyGen = new CSMPIndexingKeyGenerator(message.getSenderUsername().getBytes(), message.getSubject().getBytes());
              /*  decryptedMessage = CSMPCryptStringUtil.decrypt(
                        keyGen,
                        message.getMessage()
                );*/
            }
        } catch (CSMPCryptException cce) {
            Log.d("Messages", "Failed to Codetel decrypt message with id " + message.getId(), cce);
            FlurryAgent.onError("Messages", "Failed to Codetel decrypt message with id " + message.getId(), cce);
        } catch (Exception e) {
            Log.d("Messages", "Failed to AES decrypt message with id " + message.getId(), e);
            FlurryAgent.onError("Messages", "Failed to AES decrypt message with id " + message.getId(), e);
        }

        // just show the messages contents
        content.setText(decryptedMessage);
    }

    /**
     * Adjust the style of the chat's words depending on weather there are unread messages
     */
    private void setReadStatus() {
        Resources res = context.getResources();

        //making unread messages more visible
        if (chat.getTotalUnreadMessages() > 0) {
            name.setTypeface(null, Typeface.BOLD);
            name.setTextColor(res.getColor(R.color.primary_txt_inverse));
            content.setTypeface(null, Typeface.BOLD);
            content.setTextColor(res.getColor(R.color.primary_txt_inverse));
        }
        //if all messages have been read, make appearance normal
        else {
            name.setTypeface(null, Typeface.NORMAL);
            name.setTextColor(res.getColor(R.color.secondary_txt));
            content.setTypeface(null, Typeface.NORMAL);
            content.setTextColor(res.getColor(R.color.secondary_txt));
        }
    }
}
