package com.hsdi.NetMe.ui.chat.text.participants_list;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.Contact;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantHolder> {

    private final LayoutInflater inflater;
    private final List<Contact> contacts;
    private final Activity activity;

    public ParticipantAdapter(Activity activity, List<Contact> contacts) {
        this.inflater = LayoutInflater.from(activity);
        this.contacts = contacts;
        this.activity = activity;
    }

    @Override
    public ParticipantHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_chat_participant, parent, false);
        return new ParticipantHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(ParticipantHolder holder, int position) {
        holder.bind(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
