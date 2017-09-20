package com.hsdi.NetMe.ui.main.favorites;

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

public class FavoritesHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final OnFavoritesSelectedListener listener;

    @Bind(R.id.favorite_avatar)
    ImageView ivAvatar;
    @Bind(R.id.favorite_name)
    TextView tvName;

    public FavoritesHolder(Context context, View itemView, OnFavoritesSelectedListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.context = context;
        this.listener = listener;
    }

    public void bind(final Contact contact) {
        // null contacts are not allowed
        if (contact == null) return;

        if (contact.getAvatarUri() != null) {
            //load the contact's avatar image
            Picasso.with(context)
                    .load(contact.getAvatarUri())
                    .placeholder(R.drawable.empty_avatar)
                    .transform(new CircleTransformation())
                    .into(ivAvatar);
        } else {
            ivAvatar.setImageResource(R.drawable.empty_avatar);
            ((BaseThemeActivity) context).applyTheme(ivAvatar, R.drawable.empty_avatar);
        }


        //set their first name
        tvName.setText(contact.getFirstName());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onSelected(contact);
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongSelected(contact);
                return true;
            }
        });
    }
}
