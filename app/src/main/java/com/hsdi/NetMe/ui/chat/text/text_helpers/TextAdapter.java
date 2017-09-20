package com.hsdi.NetMe.ui.chat.text.text_helpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.models.ManagedMessage;
import com.hsdi.NetMe.models.Participant;
import com.hsdi.NetMe.models.events.EventManMsgUpdate;
import com.hsdi.NetMe.ui.chat.util.MessageManager;
import com.hsdi.NetMe.ui.chat.util.OnMessageClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextAdapter extends RecyclerView.Adapter<TextMsgHolder> {

    private final LayoutInflater inflater;
    private final MessageManager msgManager;
    private final OnMessageClickListener clickListener;
    private static Map<String, Participant> participantMap = new  HashMap<>();
    private long chatId;

    public TextAdapter(Context context, MessageManager msgManager, OnMessageClickListener clickListener, long chatId, List<Participant> participants) {
        Log.i("yuyong_profile", "TextAdapter: " + participants.size());
        this.inflater = LayoutInflater.from(context);
        this.msgManager = msgManager;
        this.clickListener = clickListener;
        this.chatId = chatId;
       // this.participantMap = new HashMap<>();
        for(Participant participant : participants) {
            this.participantMap.put(participant.getUsername(), participant);
            Log.i("yuyong_profile", "TextAdapter: " + participant.getUsername());
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public TextMsgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout itemView = (RelativeLayout) inflater.inflate(R.layout.item_chat_msg_container, parent, false);
        return new TextMsgHolder(inflater, itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(TextMsgHolder holder, int position) {
        Log.i("yuyong_profile", "onBindViewHolder: " + participantMap.size());
        ManagedMessage manMsg = msgManager.getMessageAtIndex(position);
        Participant participant = participantMap.get(manMsg.getSenderUsername());
        Contact contact = NetMeApp.getContactWith(manMsg.getSenderUsername());
        Log.i("yuyong_profile", String.format( "onBindViewHolder: %d ---> %s --->%s ---> %s",position,participant,contact,participantMap));
        holder.bind(manMsg, participant);
    }

    @Override
    public int getItemCount() {
        return msgManager.getMessageCount();
    }

    public void updateChatId(long chatId) {
        this.chatId = chatId;
    }

    public void addParticipants(List<Participant> participants) {
        if(participants != null) {
            for (Participant participant : participants) {
                participantMap.put(participant.getUsername(), participant);
                Log.i("yuyong_profile", "addParticipants: " + participant.getUsername());
            }
        }
    }

    public List<Participant> getParticipants() {
        return new ArrayList<>(participantMap.values());
    }


//--------------------------------------------------------------------------------------------------


    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void managedMessagesUpdated(EventManMsgUpdate event) {
        if(chatId == event.getChatId()) {
            switch (event.getListAction()) {
                // notify that the item was removed
                case EventManMsgUpdate.LIST_ACTION_REMOVED :
//                    notifyItemRemoved(event.getListIndex());
//                    break;

                // notify that the item was changed/updated
                case EventManMsgUpdate.LIST_ACTION_CHANGED :
//                    notifyItemChanged(event.getListIndex());
//                    break;

                // notify that the item was inserted in to the list
                case EventManMsgUpdate.LIST_ACTION_INSERTED :
//                    notifyItemInserted(event.getListIndex());
//                    break;

                // notify that something in the data set changed, so redo the entire thing
                case EventManMsgUpdate.LIST_ACTION_UNKNOWN :
                    notifyDataSetChanged();
                    break;
            }
        }
    }
}
