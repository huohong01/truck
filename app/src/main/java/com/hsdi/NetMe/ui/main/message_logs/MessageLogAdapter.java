package com.hsdi.NetMe.ui.main.message_logs;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.Chat;

import java.util.ArrayList;
import java.util.List;

class MessageLogAdapter extends RecyclerView.Adapter<MessageLogHolder> {
    private static final String TAG = "messageLogAdapter";

    private final MessageLogFragment msgFragment;
    private final LayoutInflater inflater;
    private final List<Chat> chatList;
    private final SortedList<Chat> filteredChatList;
    private final MessageLogFilter filter;
    private CharSequence filterText;
    private final ViewBinderHelper viewBinderHelper;

    MessageLogAdapter(MessageLogFragment msgFragment) {
        this.msgFragment = msgFragment;
        this.inflater = LayoutInflater.from(msgFragment.getActivity());
        this.chatList = new ArrayList<>();
        this.filteredChatList = new SortedList<>(Chat.class, new SortedList.Callback<Chat>() {
            @Override
            public int compare(Chat o1, Chat o2) {
                return o1.compareTo(o2);
            }

            @Override
            public void onInserted(int position, int count) {
                notifyDataSetChanged();
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyDataSetChanged();
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyDataSetChanged();
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemChanged(position);
            }

            @Override
            public boolean areContentsTheSame(Chat oldItem, Chat newItem) {
                return oldItem.contentSameAs(newItem);
            }

            @Override
            public boolean areItemsTheSame(Chat item1, Chat item2) {
                return item1.getChatId() == item2.getChatId();
            }
        });
        this.filter = new MessageLogFilter(this);
        this.viewBinderHelper = new ViewBinderHelper();
    }

    @Override
    public MessageLogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SwipeRevealLayout itemView = (SwipeRevealLayout) inflater.inflate(R.layout.item_log_message, parent, false);
        return new MessageLogHolder(itemView, msgFragment);
    }

    @Override
    public void onBindViewHolder(MessageLogHolder holder, int position) {
        Chat chat = filteredChatList.get(position);

        // Save/restore the open/close state with the chat's id
        viewBinderHelper.bind(holder.swipeLayout, String.valueOf(chat.getChatId()));

        // bind the new info to the views
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return filteredChatList.size();
    }

    public int getTotalCount() {
        return chatList.size();
    }





    void addAll(List<Chat> chats) {
        //only add new chats to the list
        for(Chat newChat : chats) {
            if(chatList.contains(newChat)) {
                int position = chatList.indexOf(newChat);
                chatList.set(position, newChat);
            }
            else chatList.add(newChat);
        }

        //apply any filters to the new chats
        filter.filter(filterText);

        Log.d(TAG, "Pre-filtering counts:" +
                "\ntotal contacts = " + chatList.size() +
                "\nfiltered contacts = " + filteredChatList.size());
    }

    List<Chat> getAllChats() {
        return chatList;
    }

    void setFilteredChats(List<Chat> filteredChats) {
       if (filteredChats.size() == 0){
           return;
       }
        this.filteredChatList.clear();
        this.filteredChatList.addAll(filteredChats);

        Log.d(TAG, "Post-filtering counts:" +
                "\ntotal contacts = " + chatList.size() +
                "\nfiltered contacts = " + filteredChatList.size());
    }

    public void filter(CharSequence filterText) {
        this.filterText = filterText;

        if(filter != null) filter.filter(filterText);
    }

    public void remove(Chat chat) {
        // get position from the visible list
        int position = filteredChatList.indexOf(chat);

        //remove the item from both lists
        chatList.remove(chat);
        filteredChatList.remove(chat);

        //notify the item removed
        notifyItemRemoved(position);
    }

    long getOldestChatSeconds() {
        Chat oldestChat = chatList.get(chatList.size() - 1);
        if(oldestChat != null) return oldestChat.getLatestMessageSeconds();
        else return System.currentTimeMillis() / 1000;
    }
}
