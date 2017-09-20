package com.hsdi.NetMe.ui.chat.text.text_helpers;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.ManagedMessage;
import com.hsdi.NetMe.models.Media;
import com.hsdi.NetMe.ui.chat.util.OnMessageClickListener;

import java.io.File;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

public class PendingMediaAdapter extends RecyclerView.Adapter<PendingMediaHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Media> mediaList;


    public PendingMediaAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mediaList = new ArrayList<>();
        Log.i("yuyong",String.format("PendingMediaAdapter mediaList=%d",this.mediaList.hashCode()));
    }


    @Override
    public PendingMediaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_pending_media, parent, false);
        return new PendingMediaHolder(view, new OnMessageClickListener() {
            @Override
            public void onAvatarClicked(String senderUsername) { }

            @Override
            public void onClick(ManagedMessage managedMessage) { }

            @Override
            public void onCloseClicked(Media media) {
                Log.i("yuyong",String.format("onCloseClicked %s",media.getFileName()));
                mediaList.remove(media);
                notifyDataSetChanged();
            }

            @Override
            public boolean onLongClicked(View messageView, ManagedMessage managedMessage) {
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(PendingMediaHolder holder, int position) {
        holder.bind(mediaList.get(position));
    }

    @Override
    public int getItemCount() {
        if(mediaList == null) return 0;
        else return mediaList.size();
    }

//    public void setItemList(List<Media> mediaList){
//        Log.e("media", "setItemList: ========================== mediaList = " + mediaList.toString() );
//        this.mediaList = mediaList;
//    }

    /**
     * Gets a list of all the items on the pending media list
     * @return the pending media list
     * */
    public List<Media> getItemList() {
        Log.i("yuyong",String.format("getItemList %d",mediaList.hashCode()));
        for(Media media:mediaList){
            Log.i("yuyong",String.format("getItemList %s-->%s",media.getFileName(),media.getFileDesc()+""));
        }
        return mediaList;
    }

    /** Clears all the items from the pending media list */
    public void clearItemList() {
        mediaList.clear();
        notifyDataSetChanged();
        Log.i("yuyong",String.format("clearItemList %d",mediaList.hashCode()));
    }

    /**
     * Adds an image file to the pending media area
     * @param imageFile    the image file
     * */
    public void addImageFile(File imageFile) {
        Log.e("media", "addImageFile: 1===================file==>" + imageFile );
        Media media = new Media(Media.TYPE_IMAGE, imageFile);
        mediaList.add(media);
        Log.e("media", "addImageFile: 1===================file==>" + mediaList.toString() );
        notifyDataSetChanged();
        Log.i("yuyong",String.format("addImageFile %s",imageFile.getAbsolutePath()));
    }

    /**
     * Adds an image file from the system to the pending media area
     * @param fileName          the file name to show
     * @param fileDescriptor    the file descriptor for the file
     * */
    public void addImageFile(String fileName, final FileDescriptor fileDescriptor, Uri uri) {
        Media media = new Media(null, fileName, fileDescriptor,uri);
        Log.i("yuyong",String.format("addImageFile %s-->%d-->%s",media.getFileName(),media==null?-1111:media.getFileDesc().hashCode(),media.getFileDesc()+""));
        mediaList.add(media);
        for(Media _media:mediaList){
            Log.i("yuyong",String.format("addImageFile getItemList %s-->%d-->%s",_media.getFileName(),_media==null?-1111:_media.getFileDesc().hashCode(),_media.getFileDesc()+""));
        }
        notifyDataSetChanged();
        Log.i("yuyong",String.format("addImageFile FileDescriptor %s-->%d",media.getFileName(),media==null?-1111:media.getFileDesc().hashCode()));
    }


    /**
     * Adds a voice file to the pending media area
     * @param voiceFile    the voice file
     * */
    public void addVoiceFile(File voiceFile) {
        Media media = new Media(voiceFile);
        mediaList.add(media);
        notifyDataSetChanged();
        Log.i("yuyong",String.format("addVoiceFile voiceFile %s",voiceFile.getAbsolutePath()));
    }

    /**
     * Adds a file name with reference to the file to the pending media area
     * @param fileName          the filename to show
     * @param fileDescriptor    the file descriptor for the file
     * */
    public void addFile(String fileName, FileDescriptor fileDescriptor) {
        Media media = new Media(fileDescriptor, fileName);
        mediaList.add(media);
        notifyDataSetChanged();
        Log.i("yuyong",String.format("addFile FileDescriptor %d",fileDescriptor.hashCode()));
    }

    /**
     * Adds a location to the pending media area
     * @param latLng    the location to add
     * */
    public void addLocation(LatLng latLng) {
        Media media = new Media(latLng);
        mediaList.add(media);
        notifyDataSetChanged();
        Log.i("yuyong",String.format("addLocation FileDescriptor %d",latLng.hashCode()));
    }
}
