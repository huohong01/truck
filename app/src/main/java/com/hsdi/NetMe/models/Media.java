package com.hsdi.NetMe.models;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileDescriptor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Media {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_INVALID, TYPE_FILE, TYPE_IMAGE, TYPE_AUDIO, TYPE_VOICE, TYPE_VIDEO, TYPE_LOCATION})
    public @interface Type {}

    public static final int TYPE_INVALID    = -1;
    public static final int TYPE_FILE       = 0;
    public static final int TYPE_IMAGE      = 1;
    public static final int TYPE_AUDIO      = 2;
    public static final int TYPE_VOICE      = 3;
    public static final int TYPE_VIDEO      = 4;
    public static final int TYPE_LOCATION   = 5;

    public static final int MAX_IMAGE_DIMEN = 500;

    private int type                        = TYPE_INVALID;
    private Bitmap bitmap                   = null;
    private int origImageWidth              = 0;
    private int origImageHeight             = 0;
    private LatLng location                 = null;
    private File file                       = null;
    private FileDescriptor fileDesc         = null;
    private String fileName                 = null;
    private Uri uri                          = null;

    Media(@Type int type, Bitmap bitmap, int origImageWidth, int origImageHeight, LatLng location, File file, FileDescriptor fileDesc, String fileName) {
        this.type = type;
        this.bitmap = bitmap;
        this.origImageWidth = origImageWidth;
        this.origImageHeight = origImageHeight;
        this.location = location;
        this.file = file;
        this.fileDesc = fileDesc;
        this.fileName = fileName;
    }

    public Media(Bitmap bitmap, String fileName, FileDescriptor fileDescriptor) {
        this(bitmap, fileName);
        this.fileDesc = fileDescriptor;
        Log.e("media", "Media: =================== fileDesc = " + fileDesc );
    }

    public Media(Bitmap bitmap, String fileName, FileDescriptor fileDescriptor,Uri uri) {
        this(bitmap, fileName);
        this.fileDesc = fileDescriptor;
        this.uri = uri;
        Log.e("media", "Media: =================== fileDesc = " + fileDesc );
    }

    public Media(Bitmap bitmap, String fileName, File file) {
        this(bitmap, fileName);
        this.file = file;
    }

    public Media(Bitmap bitmap, String fileName) {
        this.type = TYPE_IMAGE;
        this.bitmap = bitmap;
        if(bitmap != null) {
            this.origImageWidth = bitmap.getWidth();
            this.origImageHeight = bitmap.getHeight();
            setScaledBitmap(bitmap);
        }
        this.fileName = fileName;
    }

    public Media(LatLng location) {
        this.type = TYPE_LOCATION;
        this.location = location;
    }

    public Media(File file) {
        this.type = TYPE_VOICE;
        this.file = file;
        this.fileName = file.getName();
    }

    public Media(FileDescriptor assetFileDescriptor, String fileName) {
        this.type = TYPE_FILE;
        this.fileDesc = assetFileDescriptor;
        this.fileName = fileName;
    }

    public Media(@Type int type, File file) {
        this.type = type;
        this.file = file;
        this.fileName = file.getName();
    }

    public Media(@Type int type, String fileName) {
        this.type = type;
        this.fileName = fileName;
    }


//--------------------------------------------------------------------------------------------------

    /**
     * Sets Media's bitmap to a thumbnail size image
     * @param bitmap    the original bitmap
     * */
    private void setScaledBitmap(Bitmap bitmap) {
        // If the height is the larger dimension and it is more then the
        // max, scale it down with the height equal to our max.
        // This will make the width equal to or smaller then our max
        if(bitmap.getHeight() >= bitmap.getWidth() && bitmap.getHeight() > MAX_IMAGE_DIMEN) {
            float scalingValue = (float) MAX_IMAGE_DIMEN / (float) bitmap.getHeight();
            float newWidth = (float) bitmap.getWidth() * scalingValue;
            int newWidthInt = (int) newWidth;
            this.bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    newWidthInt,
                    MAX_IMAGE_DIMEN,
                    false
            );
        }
        // Otherwise, the width is the larger dimension. So scale by
        // making the width equal to the max.
        else if(bitmap.getWidth() > MAX_IMAGE_DIMEN) {
            float scalingValue = (float) MAX_IMAGE_DIMEN / (float) bitmap.getWidth();
            float newHeight = (float) bitmap.getHeight() * scalingValue;
            int newHeightInt = (int) newHeight;
            this.bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    MAX_IMAGE_DIMEN,
                    newHeightInt,
                    false
            );
        }
    }


//--------------------------------------------------------------------------------------------------


    @Type
    public int getType() {
        return type;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getOrigImageWidth() {
        return origImageWidth;
    }

    public int getOrigImageHeight() {
        return origImageHeight;
    }

    public LatLng getLocation() {
        return location;
    }

    public File getFile() {
        return file;
    }

    public FileDescriptor getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(FileDescriptor fileDesc) {
        this.fileDesc = fileDesc;
    }

    public String getFileName() {
        return fileName;
    }

    public Uri getUri(){
        return uri;
    }

    @Override
    public String toString() {
        return "Media{" +
                "bitmap=" + bitmap +
                ", file=" + file +
                ", fileName='" + fileName + '\'' +
                ", fileDesc=" + fileDesc +
                '}';
    }
}
