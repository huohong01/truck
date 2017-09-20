package com.hsdi.NetMe.ui.ContactListUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.util.CircleTransformation;
import com.hsdi.theme.basic.BaseThemeActivity;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.contact_fav_icon)
    ImageView favIcon;
    @Bind(R.id.contact_avatar)
    ImageView avatar;
    @Bind(R.id.contact_avatar_letter)
    TextView avatarLetter;
    @Bind(R.id.contact_alphabet_indicator)
    TextView alphabetIndicator;
    @Bind(R.id.contact_name)
    TextView name;
    @Bind(R.id.contact_divider)
    View divider;
    @Bind(R.id.contact_registered_marker)
    View registeredMarker;

    private final Context context;
    private Contact contact;

    public ContactHolder(Context newContext, View itemView, final OnContactClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.context = newContext;

        // if this contact is clicked
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(contact);
            }
        });
    }

    public void bind(Contact newContact, boolean indexIsVisible, boolean dividerIsVisible) {
        // storing the contact for latter use
        this.contact = newContact;

        if (contact != null) {
            // adjust view visibilities
            avatarLetter.setVisibility(contact.getThumbnailUri() == null ? View.VISIBLE : View.INVISIBLE);
            alphabetIndicator.setVisibility(indexIsVisible ? View.VISIBLE : View.INVISIBLE);
            divider.setVisibility(dividerIsVisible ? View.VISIBLE : View.INVISIBLE);
            favIcon.setVisibility(contact.isFavorite() ? View.VISIBLE : View.INVISIBLE);
            registeredMarker.setVisibility(contact.isRegistered() ? View.VISIBLE : View.INVISIBLE);

            String firstLetter = String.valueOf(contact.getName().charAt(0));

            // setting name and group letter indicator
            alphabetIndicator.setText(firstLetter);
            name.setText(contact.getName());

            avatarLetter.setText(contact.getInitials());
            if (contact.getThumbnailUri() != null) {
                // load the contact's avatar
                Picasso.with(context)
                        .load(contact.getThumbnailUri())
                        .placeholder(R.drawable.empty_avatar)
                        .transform(new CircleTransformation())
                        .into(avatar);
            } else {
                avatar.setImageResource(R.drawable.empty_avatar);
                ((BaseThemeActivity) context).applyTheme(avatar, R.drawable.empty_avatar);
            }


        }
    }
}
