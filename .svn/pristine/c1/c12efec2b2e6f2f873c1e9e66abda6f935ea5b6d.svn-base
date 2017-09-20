package com.hsdi.NetMe.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/** Factory class which is used to create Media objects */
public class MediaFactory {

    private byte[] mData;
    private String mName;
    @Media.Type private int mType;
    private LatLng location;

    public int finalImageSize = 400;

    private MediaFactory() { }

    /**
     * Factory method which creates a factory to work with AES media (full media type range)
     * @param type    the type of media to be created
     * @return an instance of the MediaFactory which requires the data to finish creating the
     * {@link Media} object
     * */
    public static MediaFactory setAESType(int type) {
        MediaFactory mf = new MediaFactory();

        switch (type) {
            case 0:
                mf.mType = Media.TYPE_FILE;
                break;

            case 1:
                mf.mType = Media.TYPE_IMAGE;
                break;

            case 2:
                mf.mType = Media.TYPE_AUDIO;
                break;

            case 3:
                mf.mType = Media.TYPE_VOICE;
                break;

            case 4:
                mf.mType = Media.TYPE_VIDEO;
                break;

            case 5:
                mf.mType = Media.TYPE_LOCATION;
                break;

            default:
                mf.mType = Media.TYPE_INVALID;
        }

        mf.mType = type;
        return mf;
    }

    /**
     * Factory method which creates a factory to work with CodelTel media (limited media type range)
     * @param type    the type of Codetel media to be created
     * @return an instance of the MediaFactory which requires the data to finish creating the
     * {@link Media} object
     * */
    public static MediaFactory setCodetelType(int type) {
        MediaFactory mf = new MediaFactory();

        switch (type) {
            case 2: // image
                mf.mType = Media.TYPE_IMAGE;
                break;

            case 3: // file
                mf.mType = Media.TYPE_FILE;
                break;

            case 4: // location
                mf.mType = Media.TYPE_LOCATION;
                break;

            default: // invalid
                mf.mType = Media.TYPE_INVALID;
        }

        return mf;
    }

    /**
     * Sets the Codetel Media location information
     * @param latLng    the {@link LatLng} object containing the location information
     * @return the instance of the MediaFactory with the location information set and ready to
     * create the {@link Media} object
     * */
    public MediaFactory setCodetelLocation(LatLng latLng) {
        location = latLng;
        return this;
    }

    /**
     * Sets the decrypted data and file name information
     * @param data        the byte array file
     * @param fileName    the name of the file the data byte represents
     * @return the instance of the MediaFactory with the file information set and ready to
     * create the {@link Media} object
     * */
    public MediaFactory setData(byte[] data, String fileName) {
        mData = data;
        mName = fileName;
        return this;
    }

    /**
     * Creates the {@link Media} object using the set
     * @return the {@link Media} object
     * */
    public Media create() {
        // determine type and create the Media object of that type
        switch (mType) {
            case Media.TYPE_AUDIO:
                return createAudioMedia();

            case Media.TYPE_FILE:
                Log.i("yuyong", "create:Media.TYPE_FILE ");
                return createFileMedia();

            case Media.TYPE_IMAGE:
                return createImageMedia();

            case Media.TYPE_LOCATION:
                return createLocationMedia();

            case Media.TYPE_VIDEO:
                return createVideoMedia();

            case Media.TYPE_VOICE:
                return createVoiceMedia();

            case Media.TYPE_INVALID:
            default:
                return createInvalidMedia();
        }
    }

    /** Handles making the data into a bitmap and creating the new Media object */
    private Media createImageMedia() {
        // getting the image size without allocating the memory for the pixels
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(mData, 0, mData.length, options);
        int height = options.outHeight;
        int width = options.outWidth;

        // calculating the sample size divider
        int divider = 1;
        int largestSide = Math.max(height, width);
        if (largestSide > finalImageSize) divider = largestSide/finalImageSize;

        // update the options
        options.inSampleSize = divider;
        options.inJustDecodeBounds = false;

        // decode again with the scaled down version
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(mData, 0, mData.length, options);

        // return the media object
        return new Media(
                Media.TYPE_IMAGE,
                imageBitmap,
                imageBitmap.getWidth(),
                imageBitmap.getHeight(),
                null,
                null,
                null,
                mName
        );
    }

    /** Uses the file name to create the new audio Media object. *Currently treated as File Media */
    private Media createAudioMedia() {
        /*TODO update this to actually do something with this info*/
        return new Media(
                Media.TYPE_AUDIO,
                null,
                0,
                0,
                null,
                null,
                null,
                mName
        );
    }

    /** Uses the file name to create the new voice Media object. *Currently treated as File Media */
    private Media createVoiceMedia() {
        /*TODO update this to actually do something with this info*/
        return new Media(
                Media.TYPE_VOICE,
                null,
                0,
                0,
                null,
                null,
                null,
                mName
        );
    }

    /** Uses the file name to create the new video Media object. *Currently treated as File Media */
    private Media createVideoMedia() {
        /*TODO update this to actually do something with this info*/
        return new Media(
                Media.TYPE_VIDEO,
                null,
                0,
                0,
                null,
                null,
                null,
                mName
        );
    }

    /** Uses the location information to create the new location Media object */
    private Media createLocationMedia() {
        if(location == null) {
            try {
                // converting the data to a string
                String dataString = new String(mData, "UTF-8");
                String lat = dataString.substring(0, dataString.indexOf(","));
                String lng = dataString.substring(dataString.indexOf(",") + 1, dataString.length());

                // parse the data into latitude and longitude values
                double latitude = Double.parseDouble(lat);
                double longitude = Double.parseDouble(lng);

                // set the location using the parsed values
                location = new LatLng(latitude, longitude);
            }
            // return invalid media if bad encoding is used
            catch (Exception e) {
                Log.d("MediaFactory", "Failed to parse the location", e);
                return createInvalidMedia();
            }
        }

        return new Media(
                Media.TYPE_LOCATION,
                null,
                0,
                0,
                location,
                null,
                null,
                mName
        );
    }

    /** Uses the file name to create the new file Media object */
    private Media createFileMedia() {
        Log.e("yuyong", "createFileMedia: " );
        return new Media(
                Media.TYPE_FILE,
                null,
                0,
                0,
                null,
                null,
                null,
                mName
        );
    }

    /** Uses the file name of create a TYPE_INVALID Media object. *Currently treated as File Media */
    private Media createInvalidMedia() {
        return new Media(
                Media.TYPE_INVALID,
                null,
                0,
                0,
                null,
                null,
                null,
                mName
        );
    }
}
