package com.hsdi.NetMe.ui.chat.text.participants_list;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.database.ChatTrackerManager;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.ui.chat.ChatActivity;
import com.hsdi.NetMe.util.CircleTransformation;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

class ParticipantHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.chat_participant_avatar) ImageView avatar;
    @Bind(R.id.chat_participant_letters)TextView initials;
    @Bind(R.id.chat_participant_name)   TextView name;
    @Bind(R.id.chat_participant_number) TextView number;
    @Bind(R.id.chat_participant_chat)   View textChatBtn;
    @Bind(R.id.chat_participant_video)  View videoChatBtn;

    private final Activity activity;

    ParticipantHolder(View itemView, Activity activity) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.activity = activity;
    }

    public void bind(Contact contact) {
        // set the name
        name.setText(contact.getName());
        initials.setText("");

        // get the phone number and set it
        final String username = contact.getPhones().get(0).getPlainNumber();
        number.setText(username);

        // try to load the contact avatar
        if(contact.getAvatarUri() != null) {
            Log.d("oasdfh", contact.getAvatarUri().toString());
            Picasso.with(itemView.getContext())
                    .load(contact.getAvatarUri())
                    .placeholder(R.drawable.empty_avatar)
                    .transform(new CircleTransformation())
                    .into(avatar);
        }
        // try to load the participant avatar
        else if(!TextUtils.isEmpty(contact.getAvatarUrl())) {
            Picasso.with(itemView.getContext())
                    .load(contact.getAvatarUrl())
                    .placeholder(R.drawable.empty_avatar)
                    .transform(new CircleTransformation())
                    .into(avatar);
        }
        // set the contact initials
        else initials.setText(contact.getInitials());

        // setup button actions
        textChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTextChatWith(username);
            }
        });
        videoChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVideoChatWith(username);
            }
        });

        // but if the contact is this user, hide the start buttons
        if(username.equals(NetMeApp.getCurrentUser())) {
            textChatBtn.setVisibility(View.INVISIBLE);
            videoChatBtn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Starts a VIDEO chat with the passed number or continues a pre-existing one if it exists
     * @param phoneNumber    the number to start/continue the chat with
     * */
    private void startVideoChatWith(String phoneNumber) {
        // check if a chat already exists
        long chatId = ChatTrackerManager.getChatIdFromPhoneNumber(itemView.getContext(), phoneNumber);

        // add invited phone number
        Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_CHAT_TYPE, ChatActivity.TYPE_START_VIDEO);
        intent.putExtra(ChatActivity.EXTRA_INVITED, phoneNumber);

        // chat exists so add it
        if(chatId >= 0) intent.putExtra(ChatActivity.EXTRA_CHAT_ID, chatId);

        // start video chat activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        itemView.getContext().startActivity(intent);
    }

    /**
     * Starts a TEXT chat with the passed number or continues a pre-existing one if it exists
     * @param phoneNumber    the number to start/continue the chat with
     * */
    private void startTextChatWith(String phoneNumber) {
        // check if a chat already exists
        long chatId = ChatTrackerManager.getChatIdFromPhoneNumber(itemView.getContext(), phoneNumber);
        Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_CHAT_TYPE, ChatActivity.TYPE_TEXT_ONLY);

        // chat exists so continue it
        if(chatId >= 0) intent.putExtra(ChatActivity.EXTRA_CHAT_ID, chatId);
        // tell the chat activity to start a new chat
        else intent.putExtra(ChatActivity.EXTRA_INVITED, phoneNumber);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        itemView.getContext().startActivity(intent);
    }
}
