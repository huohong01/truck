package com.hsdi.NetMe.ui.chat.text.helper_activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hsdi.NetMe.BaseActivity;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.ManagedMessage;
import com.hsdi.NetMe.network.HttpDownloadUtility;
import com.hsdi.NetMe.ui.chat.util.MessageManager;
import com.hsdi.NetMe.util.AESCrypt;
import com.hsdi.NetMe.util.DeviceUtils;
import com.hsdi.NetMe.util.Utils;
import com.macate.csmp.CSMPCrypt;
import com.macate.csmp.CSMPCryptException;
import com.macate.csmp.CSMPDecryptResult;
import com.macate.csmp.CSMPIndexingKeyGenerator;

import java.io.File;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ImageViewActivity extends BaseActivity {
    private static final String TAG = "ImageViewActivity";

    public static final String EXTRA_CHAT_ID = "chatId";
    public static final String EXTRA_MESSAGE_ID = "messageId";

    @Bind(R.id.view_image_toolbar) Toolbar toolbar;
    @Bind(R.id.image_layout) RelativeLayout imageLayout;

    private ManagedMessage managedMessage;
    private LoadBitmapAsync loadBitmapAsync;
    private Bitmap bitmap;
    private String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getManagedMessage();

        // Load the image
        loadBitmapAsync = new LoadBitmapAsync(this);
        loadBitmapAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onBackPressed() {
        if (loadBitmapAsync != null) loadBitmapAsync.cancel(true);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_img:
                saveBitmap();
                return true;
        }
        return false;
    }

    /** Store the image in the user's external download folder */
    private void saveBitmap() {
        try {
            if (bitmap != null && imageName != null && !imageName.isEmpty()) {
                Toast.makeText(this, R.string.saving, Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, imageName, "image");
                        }
                        catch (Exception e) {e.printStackTrace();}
                    }
                }).start();
            }
        }
        catch (Exception e) {e.printStackTrace();}
    }

    /** Gets the managed message with the image to be shown */
    private void getManagedMessage() {
        long chatId = getIntent().getLongExtra(EXTRA_CHAT_ID, -1);
        long messageId = getIntent().getLongExtra(EXTRA_MESSAGE_ID, -1);
        MessageManager msgManager = null;
        Log.i("yuyong_image", String.format("chatId:%d----->messageId:%d",chatId,messageId));
        try {
            msgManager = NetMeApp.getInstance().getMsgManagerForChat(chatId);
        }
        catch (Exception e) {
            Log.e(TAG, "Failed to get a good message manager", e);
            Toast.makeText(this.getApplicationContext(), R.string.error_unexpected, Toast.LENGTH_SHORT).show();
            finish();
        }

        managedMessage = msgManager.getMessageWithId(messageId);
        Log.i("yuyong_image",String.format("getManagedMessage:sender--->%s",managedMessage.getSenderUsername()));
    }

    /** Loads and decrypts the image in the background */
    private class LoadBitmapAsync extends AsyncTask<String, Void, Bitmap> {
        private ProgressDialog progressDialog;
        private Context context;

        LoadBitmapAsync(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });

            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // get the media url
            String imageUrl = null;
            if(managedMessage != null && managedMessage.getMedia() != null) {
                imageUrl = managedMessage.getMedia()[0];
            }

            // if the url is bad exit and finish
            if(imageUrl == null || imageUrl.isEmpty()) return null;

            try {
                // extracts file name from URL and make a local file from it
                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
                File mediaFile = new File(
                        DeviceUtils.getEncryptedMediaDirectory(NetMeApp.getInstance()),
                        fileName
                );

                Log.i("yuyong_image", String.format("imageUrl:%s----->fileName:%s---->mediaFile:%s",imageUrl,fileName,mediaFile));
                byte[] imageFileBytes;
                // getting the image file bytes from local file
                if(mediaFile.exists()) imageFileBytes = Utils.getBytesFromFile(mediaFile);
                // downloading data
                else imageFileBytes = HttpDownloadUtility.downloadByteArray(imageUrl);

                byte[] decryptedBytes = imageFileBytes;
                // if the image is using the new AES encryption library
                if(managedMessage.getMessage().mayBeAesEncrypted()) {
                    AESCrypt aesCrypt = null;
                    if (managedMessage.isFromContact()) {
                         aesCrypt = new AESCrypt(managedMessage.getSenderUsername());
                    }
                    else {
                        aesCrypt = new AESCrypt(NetMeApp.getCurrentUser());
                    }
                    /*// decrypt the image bytes
                    AESCrypt aesCrypt = new AESCrypt(managedMessage.getSenderUsername());*/
                    decryptedBytes = aesCrypt.decryptBytes(imageFileBytes);

                    // decrypt and pull out the image file name
                    /*Map<String, String> fMap = MessageManager.parseEncryptedFileName(
                            new AESCrypt(NetMeApp.getCurrentUser()),
                            getString(R.string.encrypted_file_extension),
                            fileName
                    );*/
                    Map<String, String> fMap = MessageManager.parseEncryptedFileName(
                            aesCrypt,
                            getString(R.string.encrypted_file_extension),
                            fileName
                    );
                    imageName = fMap.get(MessageManager.FILE_NAME_KEY);
                }
                // if the image is using the legacy codetel encryption library
                else if(managedMessage.getMessage().mayBeCodetelEncrypted()) {
                    String senderUsername = managedMessage.getSenderUsername();
                    String salt = managedMessage.getSubject();

                    // if any of the data is bad, throw an exception
                    if(senderUsername == null || salt == null || decryptedBytes == null) {
                        throw new Exception();
                    }

                    // try to decrypt the data
                    CSMPDecryptResult cryptResult = CSMPCrypt.decrypt(
                            new CSMPIndexingKeyGenerator(senderUsername.getBytes(), salt.getBytes()),
                            imageFileBytes
                    );

                    // continue if the result is an image
                    decryptedBytes = cryptResult.getContents();
                    imageName = cryptResult.getMetaData().getFileName();
                }

                // make the bytes into a bitmap and return
                if (decryptedBytes != null) {
                    return BitmapFactory.decodeByteArray(decryptedBytes, 0, decryptedBytes.length, null);
                }
                else return null;
            }
            catch (CSMPCryptException e) {
                e.printStackTrace();
                Log.e(TAG, "Error decoding bitmap");
                return null;
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG,"Error downloading bitmap");
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            bitmap = result;

            if (isCancelled()) return;

            // Bitmap loaded successfully
            if (result != null) {
                TouchImageView imageView = new TouchImageView(context);
                imageView.setImageBitmap(result);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

                imageLayout.addView(imageView, params);
            }

            // Failed loading
            else Toast.makeText(context, R.string.error_load_image, Toast.LENGTH_SHORT).show();

            progressDialog.dismiss();
        }
    }
}
