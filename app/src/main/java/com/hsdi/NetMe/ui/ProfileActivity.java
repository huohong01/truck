package com.hsdi.NetMe.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hsdi.NetMe.BaseActivity;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.response_models.AccountResponse;
import com.hsdi.NetMe.network.RestServiceGen;
import com.hsdi.NetMe.util.AccountInfoUtils;
import com.hsdi.NetMe.util.DeviceUtils;
import com.hsdi.NetMe.util.DialogUtils;
import com.hsdi.NetMe.util.PreferenceManager;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hsdi.theme.basic.ThemeLayoutInflaterFactory.ThemeTypeValue.toolbar_nav_icon;


/**
 * The activity which allows the user to view and change their avatar, first name, and last name.
 */
public class ProfileActivity extends BaseActivity {
    private final String TAG                            = "ProfileActivity";

    private static final int REQUEST_AVATAR_FILE        = 100;
    private static final int REQUEST_AVATAR_CAMERA      = 101;
    private static final int REQUEST_AVATAR_CROP        = 102;

    private static final String AVATAR_TEMP_FILE_NAME   = "temp_avatar.png";
    private static final String AVATAR_CAMERA_NAME      = "camera_file_name";
    private static final String AVATAR_CAMERA_SUFFIX    = ".jpg";

    private File avatarCameraFile                       = null;
    private File avatarTempFile                         = null;
    private boolean avatarModified                     = false;
    private PreferenceManager manager                    = null;

    @Bind(R.id.profile_toolbar)         Toolbar toolbar;
    @Bind(R.id.profile_avatar)          ImageView ivAvatar;
    @Bind(R.id.edit_text_first_name)    TextInputEditText etFirstName;
    @Bind(R.id.edit_text_last_name)     TextInputEditText etLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        applyTheme(toolbar,R.drawable.back,toolbar_nav_icon);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //creating the temporary avatar holder file
        avatarTempFile = new File(getFilesDir(), AVATAR_TEMP_FILE_NAME);

        //getting an instance of the shared preference manager
        manager = PreferenceManager.getInstance(this);

        //load the avatar
        initAvatar();
        //load the user's names
        initEditTextName();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_AVATAR_FILE:
                    //some data was returned
                    if (data != null && data.getData() != null) {
                        //deleting any old images
                        if(avatarTempFile.exists()) avatarTempFile.delete();
                        //sending the new stuff to be cropped and placed into the temp file
                        Crop.of(data.getData(), Uri.fromFile(avatarTempFile)).asSquare().start(this, REQUEST_AVATAR_CROP);
                        Log.d(TAG, "Sent device image to get cropped");
                        return;
                    }
                case REQUEST_AVATAR_CAMERA:
                    //camera succeeded in creating the image
                    if(avatarCameraFile.exists()){
                        //deleting any old images
                        if(avatarTempFile.exists()) avatarTempFile.delete();
                        //sending the new stuff to be cropped and placed into the temp file
                        Crop.of(Uri.fromFile(avatarCameraFile), Uri.fromFile(avatarTempFile)).asSquare().start(this, REQUEST_AVATAR_CROP);
                        Log.d(TAG, "Sent camera image to get cropped");
                        return;
                    }
                case REQUEST_AVATAR_CROP:
                    //set that the avatar has been updated
                    avatarModified = true;

                    //invalidate any previous picasso load of the temp file
                    Picasso.with(this).invalidate(avatarTempFile);

                    if (avatarTempFile != null){
                        //load the new avatar into the image view
                        Picasso.with(this)
                                .load(avatarTempFile)
                                .placeholder(R.drawable.empty_avatar)
                                .into(ivAvatar);
                    }else {
                        ivAvatar.setImageResource(R.drawable.empty_avatar);
                        applyTheme(ivAvatar,R.drawable.empty_avatar);
                    }

                    return;
            }
        }

        //something bad happened, show the user an error
       // Toast.makeText(this, R.string.error_unexpected, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) requestCamera();
        else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error)
                    .setMessage(R.string.error_camera_permissions)
                    .setNeutralButton(R.string.ok, null)
                    .show();
        }
    }

    private void initAvatar() {
        Log.i("yuyong_profile", String.format("initAvatar:----->%s--->%s",NetMeApp.currentUserHasValidAvatarUrl(),NetMeApp.getCurrentUserAvatarUrl()));
        if(NetMeApp.currentUserHasValidAvatarUrl()) {
            Picasso.with(this)
                    .load(NetMeApp.getCurrentUserAvatarUrl())
                    .placeholder(R.drawable.empty_avatar)
                    .into(ivAvatar);
            Log.i("yuyong_profile", String.format("initAvatar:----->%s",NetMeApp.getCurrentUserAvatarUrl()));
        }
        else {
            ivAvatar.setImageResource(R.drawable.empty_avatar);
            applyTheme(ivAvatar,R.drawable.empty_avatar);
        }
    }

    private void initEditTextName() {
        String firstName = manager.getFirstName();
        String lastName = manager.getLastName();

        etFirstName.setText(firstName);
        etLastName.setText(lastName);
    }


    public void onSaveClicked(View v) {
        // checking current first name and original
        String originalFirst = manager.getFirstName();
        Log.i("yuyong_profile", String.format("onSaveClicked: --->%s--->%s",manager.getFirstName(),manager.getLastName()));
        final String currentFirst = AccountInfoUtils.checkFirstName(this, etFirstName, originalFirst);
        // checking current last name
        final String currentLast = AccountInfoUtils.checkLastName(this, etLastName);
        Log.i("yuyong_profile", String.format("onSaveClicked: --->%s--->%s",currentFirst,currentLast));

        if(currentFirst == null || currentLast == null) {
            DialogUtils.getErrorDialog(this,"First name or last name can not be empty").show();
            return;
        }

        // creating a dialog to show while the profile updates, also prevents the user from messing with the app
        final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this);
        progressDialog.show();

        // creating a multipart body builder and adding all the parts to it
        MultipartBody.Builder MBB = new MultipartBody.Builder();
        MBB.addFormDataPart("countrycode", manager.getCountryCallingCode());
        MBB.addFormDataPart("username", manager.getPhoneNumber());
        MBB.addFormDataPart("password", manager.getPassword());
        MBB.addFormDataPart("newFirstname", currentFirst);
        MBB.addFormDataPart("newLastname", currentLast);
        MBB.addFormDataPart("token", manager.getGCMRegistrationId());
        MBB.addFormDataPart("mac", DeviceUtils.getDeviceId(this));
        MBB.addFormDataPart("appVersion", DeviceUtils.getAppVersion(this));
        MBB.addFormDataPart("pfVersion", DeviceUtils.getPlatformVersion());

        // if the avatar was changed, set it to upload the image file
        if(avatarModified) {
            MBB.addFormDataPart(
                    "avatar",
                    "image.jpeg",
                    RequestBody.create(
                            MediaType.parse("image/jpeg"),
                            avatarTempFile
                    )
            );
        }
        // if the avatar was not changed just upload an empty string so the backend won't change anything
        else MBB.addFormDataPart("avatar", "");

        // convert from builder to body
        MultipartBody MB = MBB.build();

        //performing update. *Note: the order of the parts doesn't really matter as long
        //  as the index doesn't go out of bounds but they are labeled for readability
        RestServiceGen.getUnCachedService()
                .userUpdate(
                        MB.part(0),     // country code part
                        MB.part(1),     // username/phone number part
                        MB.part(2),     // password part
                        MB.part(3),     // new first name part
                        MB.part(4),     // new last name part
                        MB.part(5),     // gcm token part
                        MB.part(6),     // mac address/device id part
                        MB.part(7),     // app version part
                        MB.part(8),     // pf version part
                        MB.part(9)      // avatar part
                )
                .enqueue(new Callback<AccountResponse>() {
                    @Override
                    public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                        // progress dialog is no longer needed
                        if(progressDialog != null) progressDialog.dismiss();

                        // get the response
                        AccountResponse accResponse = response.body();

                        // if the update succeeded, everything needs to be stored
                        if(accResponse.isSuccess()) {
                            //storing the updated names
                            manager.setFirstName(accResponse.getFirstName());
                            manager.setLastName(accResponse.getLastName());

                            // letting Picasso to reload all instances of the avatar
                            Picasso.with(ProfileActivity.this).invalidate(manager.getAvatarUrl());

                            // storing the new avatar url
                            manager.setAvatarUrl(accResponse.getAvatarUrl());

                            // closing the profile activity
                            ProfileActivity.this.finish();
                        }
                        // update failed, showing error message
                        else {
                            Log.i("yuyong_profile", String.format("ProfileActivity:onResponse----->"));
                            DialogUtils.getErrorDialog(
                                    ProfileActivity.this,
                                    R.string.error_unexpected
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        // progress dialog is no longer needed
                        if(progressDialog != null) progressDialog.dismiss();
                        Log.i("yuyong_profile", String.format("ProfileActivity:onFailure----->%s",t.getMessage()));
                        // if here, assume there was a connection issue
                        DialogUtils.getErrorDialog(
                                ProfileActivity.this,
                                R.string.error_unexpected
                        ).show();
                    }
                });

        // make sure the user variables get updated after the update
       NetMeApp.resetCurrentStaticUser();
    }

    public void onCancelClicked(View v) {
        finish();
    }

    public void onAvatarClicked(View v) {
        Log.i(TAG, "edit profile image requested!");

        new AlertDialog.Builder(this)
                .setItems(
                        new String[]{getString(R.string.take_photo), getString(R.string.choose_photo)},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0) requestCamera();
                                else Crop.pickImage(ProfileActivity.this, REQUEST_AVATAR_FILE);
                            }
                        }
                )
                .show();
    }

    private void requestCamera() {
        boolean camera = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean storage = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if(camera && storage) startCamera();
        else if (!camera) {
            Log.d(TAG, "requesting access to camera");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_AVATAR_CAMERA
            );
        }
        else {
            Log.d(TAG, "request access to external storage");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_AVATAR_CAMERA
            );
        }
    }

    private void startCamera() {
        Log.d(TAG, "starting camera");
        try {
            avatarCameraFile = File.createTempFile(
                    AVATAR_CAMERA_NAME + System.currentTimeMillis(),
                    AVATAR_CAMERA_SUFFIX,
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(avatarCameraFile));
            startActivityForResult(intent, REQUEST_AVATAR_CAMERA);
        }
        catch (Exception e){e.printStackTrace();}
    }
}
