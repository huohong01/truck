package com.hsdi.NetMe.ui.chat.video;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.Contact;
import com.squareup.picasso.Picasso;

/**
 * shows a connecting screen while the user's are connecting to the application
 * */
public class ConnectingFragment extends Fragment {
    private static final String TAG = "CallFrag";

    public static final String EXTRA_NUMBER = "number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("yuyong_video", "ConnectingFragment:onCreateView: ");
        // Inflate the layout for this fragment
        View main = inflater.inflate(R.layout.fragment_connecting, container, false);
        ImageView imageView = (ImageView) main.findViewById(R.id.connecting_image);

        //setting the contacts avatar
        String number = getArguments().getString(EXTRA_NUMBER);
        Contact contact = NetMeApp.getContactWith(number);
        if(contact != null && contact.getAvatarUri() != null) {
            Picasso.with(getActivity())
                    .load(contact.getAvatarUri())
                    .into(imageView);
        }
        else Log.d(TAG, "Failed to load the contacts avatar");

        return main;
    }
}
