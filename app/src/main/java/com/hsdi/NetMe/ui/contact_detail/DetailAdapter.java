package com.hsdi.NetMe.ui.contact_detail;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.Contact;

public class DetailAdapter extends RecyclerView.Adapter<DetailHolder> {

    private final LayoutInflater inflater;
    private Contact contact;

    private int phoneQuantity = 0;
    private int emailQuantity = 0;

    public DetailAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        this.phoneQuantity = contact.getPhones().size();
        this.emailQuantity = contact.getEmails().size();
        notifyDataSetChanged();
    }

    @Override
    public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_contact_detail, parent, false);
        return new DetailHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailHolder holder, int position) {
        //bind phone if position is within phone list bounds
        if(contact != null && position >= 0 && position < phoneQuantity) {
            holder.bind(contact.getId(), contact.getPhones().get(position), position == 0);
        }

        //bind email is position is within email bounds plus phone quantity
        else if(contact != null && position >= phoneQuantity && position < getItemCount()) {
            holder.bind(contact.getEmails().get(position - phoneQuantity), position == phoneQuantity);
        }
    }

    @Override
    public int getItemCount() {
        //the total number of items is the number of phones and emails combined
        return phoneQuantity + emailQuantity;
    }
}
