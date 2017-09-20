package com.hsdi.NetMe.ui.chat.text.text_helpers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.Media;
import com.hsdi.NetMe.ui.chat.text.helper_activities.ViewLocationActivity;
import com.hsdi.NetMe.ui.chat.util.OnMessageClickListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileDescriptor;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

class PendingMediaHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

    @Bind(R.id.pending_media_close) View close;
    @Bind(R.id.pending_media_image) ImageView imageView;
    @Bind(R.id.pending_media_map)   MapView mapView;
    @Bind(R.id.pending_media_text)  TextView textView;

    private View mainView;
    private Media media;

    private GoogleMap map;
    private LatLng location;

    PendingMediaHolder(View itemView, final OnMessageClickListener listener) {
        super(itemView);
        Log.e("media", "PendingMediaHolder: ======================");
        this.mainView = itemView;
        ButterKnife.bind(this, itemView);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCloseClicked(media);
            }
        });
    }

    /**
     * Updates all the views with the new media item to show on the list
     * @param media    the new pending media item
     * */
    public void bind(Media media) {
        this.media = media;
        Log.e("media", "bind: ====================== " + media.getType() );
        switch (media.getType()) {
            case Media.TYPE_IMAGE:
                if(media.getBitmap() != null) {
                    Log.e("media", "bind: ======================1" + media.getBitmap() );
                    setImage(media.getBitmap());
                }
                else if (media.getFile() != null) {
                    Log.e("media", "bind: ======================2" + media.getFile() );
                    setImage(media.getFile());
                }
                else if (media.getFileDesc() != null) {
                    Log.e("media", "bind: ======================3" + media.getFileDesc() );
                    setImage(media.getFileDesc());
                }
                break;

            case Media.TYPE_LOCATION:
                Log.e("media", "bind: =============================4"  + media.getLocation());
                setLocation(media.getLocation());
                break;

            case Media.TYPE_AUDIO:
            case Media.TYPE_INVALID:
            case Media.TYPE_VIDEO:
            case Media.TYPE_VOICE:
            case Media.TYPE_FILE:
                Log.e("media", "bind: ===============================5" +  media.getFileName());
                if (media.getFileName() != null) setText(media.getFileName());
                else setText(media.getFile().getName());
        }
    }

    /**
     * Adds an image to this pending media holder
     * @param bitmap    the image
     * */
    private void setImage(Bitmap bitmap) {
        Log.e("media", "setImage: 1 ======================= bitmap = " + bitmap);
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);
        mapView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
    }

    /**
     * Adds an image to this pending media holder
     * @param file    the image file
     * */
    private void setImage(File file) {
        Log.e("media", "setImage: 2 ======================= file = " + file);
        Picasso.with(mainView.getContext())
                .load(file)
                .into(imageView);

        imageView.setVisibility(View.VISIBLE);
        mapView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
    }

    /**
     * Adds an image to this pending media holder
     * @param fileDesc    the image file descriptor
     * */
    private void setImage(FileDescriptor fileDesc) {
        Log.e("media", "setImage: 3 ===================== fileDesc = " + fileDesc );
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDesc);
        Log.e("media", "setImage: 3 =======================bitmap = " + bitmap );
        imageView.setImageBitmap(bitmap);

        imageView.setVisibility(View.VISIBLE);
        mapView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
    }

    /**
     * Adds a text title to this pending media holder
     * @param title    the text
     * */
    public void setText(String title) {
        Log.e("media", "setText: =========================== title =" + title );
        textView.setText(title);
        imageView.setVisibility(View.GONE);
        mapView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
    }

    /**
     * Adds a location map to this pending media holder
     * @param location    the location
     * */
    public void setLocation(LatLng location) {
        this.location = location;

        mapView.onCreate(null);
        mapView.getMapAsync(this);

        // setting the passed location
        if (location != null && map != null) setMapLocation(map, location);

        imageView.setVisibility(View.GONE);
        mapView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
    }

    /**
     * Sets the map location for the this pending media item when there is a location
     * @param map       the google map
     * @param latLng    the location
     * */
    private void setMapLocation(GoogleMap map, final LatLng latLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f));
        map.addMarker(new MarkerOptions().position(latLng));

        // Set the map type back to normal.
        map.getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(NetMeApp.getInstance().getApplicationContext());
        map = googleMap;

        if (location != null) {
            // Add a marker for this item and set the camera
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
            map.addMarker(new MarkerOptions().position(location));

            // Set the map type back to normal.
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.getUiSettings().setMapToolbarEnabled(false);

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng2) {
                    if (isGoogleMapsInstalled()) {
                        Log.i("msgr","google maps is installed");
                        String uri = String.format(
                                Locale.ENGLISH,
                                "geo:%f,%f?q=%f,%f",
                                location.latitude,
                                location.longitude,
                                location.latitude,
                                location.longitude
                        );
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        mainView.getContext().startActivity(intent);
                    }
                    else {
                        Log.i("msgr","google maps is not installed");
                        Intent intent = new Intent(mainView.getContext(), ViewLocationActivity.class);
                        intent.putExtra(ViewLocationActivity.EXTRA_LATLNG, location);
                        mainView.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    /** Checks if google maps is installed on the device */
    private boolean isGoogleMapsInstalled() {
        try {
            mainView.getContext()
                    .getPackageManager()
                    .getApplicationInfo("com.google.android.apps.maps", 0);

            return true;
        }
        catch(PackageManager.NameNotFoundException e) {return false;}
    }
}
