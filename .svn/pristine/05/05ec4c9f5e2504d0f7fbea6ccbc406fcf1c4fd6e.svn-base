package com.hsdi.NetMe.ui.main.recent_logs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.database.ChatTrackerManager;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.models.Meeting;
import com.hsdi.NetMe.models.Participant;
import com.hsdi.NetMe.ui.chat.ChatActivity;
import com.hsdi.NetMe.util.CircleTransformation;
import com.hsdi.theme.basic.BaseThemeActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecentHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.recent_container)            View contentContainer;
    @Bind(R.id.recent_contact_image)        ImageView avatar;
    @Bind(R.id.recent_contact_image_letter) TextView avatarLetter;
    @Bind(R.id.recent_name)                 TextView name;
    @Bind(R.id.recent_number)               TextView number;
    @Bind(R.id.recent_date)                 TextView date;
    @Bind(R.id.recent_time)                 TextView time;
    @Bind(R.id.recent_delete)               View delete;

    public final SwipeRevealLayout swipeLayout;
    private final Context context;
    private Meeting meeting;

    public RecentHolder(SwipeRevealLayout itemView, final RecentFragment recentFragment) {
        super(itemView);
        context = itemView.getContext();
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

        // if open, close. else start the meeting
        contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swipeLayout.isOpened()) swipeLayout.close(true);
                else {
                    Participant participant = meeting.getOtherParticipant();

                    // bad other participants are not allowed, EVER!!!
                    if(participant == null || TextUtils.isEmpty(participant.getUsername())) return;

                    // starting meeting activity
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra(ChatActivity.EXTRA_CHAT_TYPE, ChatActivity.TYPE_START_VIDEO);
                    intent.putExtra(ChatActivity.EXTRA_INVITED, participant.getUsername());

                    // try to get the chat id
                    long chatId = meeting.getChatId();

                    if(chatId < 0) {
                        List<Participant> pList = new ArrayList<>();
                        pList.add(participant);
                        ChatTrackerManager.getChatIdFromPhoneNumber(context, participant.getUsername());
                    }

                    if(chatId >= 0) intent.putExtra(ChatActivity.EXTRA_CHAT_ID, chatId);

                    // start the chat
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

        //setting on delete click listener
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perform the remove request
                recentFragment.removeMeeting(meeting);
            }
        });
    }

    /**
     * Sets up this the view with the correct information from the meeting
     * @param meeting    the meeting to get info from
     * */
    public void bind(Meeting meeting) {
        this.meeting = meeting;

        //get the contact
        Contact contact = null;
        Participant participant = meeting.getOtherParticipant();
        if(participant != null) contact = NetMeApp.getContactWith(participant.getUsername());

        setNameAndNumber(contact, participant);
        setAvatar(contact, participant);
        setStatus(meeting);
        setTime();
    }

    /** Sets the Name and number from the recent
     * @param contact        contact for the other person involved in the
     * @param participant    the other participant (the none )
     * */
    private void setNameAndNumber(Contact contact, Participant participant) {
        //setting the contact's name
        if (contact != null && !TextUtils.isEmpty(contact.getName())) name.setText(contact.getName());
        else if(participant != null && !participant.getFullName().trim().isEmpty()){
            String nameText = participant.getFirstName() + " " + participant.getLastName();
            name.setText(nameText);
        }
        else name.setText(R.string.deleted_user_name);

        if(participant != null) number.setText(participant.getFormattedUsername(context));
        else number.setText(R.string.unknown);
    }

    /** Sets the avatar for the message
     * @param contact        contact for the other person involved in the
     * @param participant    the other participant (the none )
     * */
    private void setAvatar(Contact contact, Participant participant) {
        //if there is no other participants/other participants deleted their accounts show deleted user values
        if (meeting.hasNoOtherUsers()) {
            avatar.setImageResource(R.drawable.empty_avatar);
            ((BaseThemeActivity) context).applyTheme(avatar,R.drawable.empty_avatar);

            avatar.setColorFilter(R.color.tint_filter);
            avatarLetter.setText("");
        }
        //try to load the local avatar or just use the no avatar default image for this app
        else if(contact != null && contact.getAvatarUri() != null) {
            Uri avatarUri = contact.getAvatarUri();

            Picasso.with(context)
                    .load(avatarUri)
                    .transform(new CircleTransformation())
                    .placeholder(R.drawable.empty_avatar)
                    .into(avatar);

            avatarLetter.setText("");
        }
        //load the url from the server unless it is the default avatar
        else if(participant != null && participant.getAvatarUrl() != null && !participant.getAvatarUrl().contains(context.getString(R.string.default_avatar))) {
            String avatarUrl = participant.getAvatarUrl();

            Picasso.with(context)
                    .load(avatarUrl)
                    .transform(new CircleTransformation())
                    .placeholder(R.drawable.empty_avatar)
                    .into(avatar);
            avatarLetter.setText("");
        }
        else {
            avatar.setImageResource(R.drawable.empty_avatar);
            ((BaseThemeActivity) context).applyTheme(avatar,R.drawable.empty_avatar);

            if(contact != null) avatarLetter.setText(contact.getInitials());
            else if(participant != null) avatarLetter.setText(participant.getInitials());
        }
    }

    /**
     * Sets the icon which represents the status for this meeting
     * @param meeting    the meeting
     * */
    private void setStatus(Meeting meeting) {
        if(meeting.isSentStatus()) {
            number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_callto_icon, 0, 0, 0);
        }
        else if(meeting.isMissedStatus()) {
            number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.missed_call, 0, 0, 0);
        }
        else number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_callrec_icon, 0, 0, 0);
    }

    /** Sets the date and hour for the latest message from the meeting */
    private void setTime() {
        //setting latest message content
        date.setText(meeting.getFormattedStartDate());
        time.setText(meeting.getFormattedStartTime());
    }
}
