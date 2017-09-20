package com.macate.minitpay.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;


import com.macate.minitpay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Neerajakshi.Daggubat on 9/2/2016.
 */
public class Utils {
    public static SessionManager session ;

    public static void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setInverseBackgroundForced(false)
                .setCancelable(false)
                .setIcon((status) ? R.mipmap.success_icon : R.mipmap.warning_ico)
                .setNegativeButton(context.getString(R.string.bCloseError), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.black));
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.black));
    }


    public static void showInternetAlertDialog(final Context context, String title, String message, Boolean status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setInverseBackgroundForced(false)
                .setCancelable(false)
                .setIcon((status) ? R.mipmap.no_wifi_ico : R.mipmap.no_wifi_ico)
                .setPositiveButton(context.getString(R.string.bOk), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton(context.getString(R.string.bSettings), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
        });
        AlertDialog alert = builder.create();
        alert.show();

        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.black));
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.black));
    }

    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager  = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }


    public static void requestPermission(String strPermission,int perCode,final Context context,final  Activity _a, String message){

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a, strPermission)){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Permission Requested!")
                    .setMessage(message)
                    .setInverseBackgroundForced(false)
                    .setCancelable(false)
                    .setPositiveButton(context.getString(R.string.bSettings), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", _a.getPackageName(), null);
                            intent.setData(uri);
                            context.startActivity(intent);
                        }
                    })
                    .setNegativeButton(context.getString(R.string.bClose), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
        }
    }

    public static boolean checkPermission(String strPermission,Context _c,Activity _a){
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }


    public static void generateAccessTokenBasedOnRefreshToken(Context context){
        session = new SessionManager(context);
            String registerUrl = Constants.BASE_URL + Constants.ACCESS_TOKEN;
            // should be a singleton
            OkHttpClient register = new OkHttpClient();

            RequestBody registerBody = new FormBody.Builder()
                    .add("grant_type", Constants.APP_REFRESH_TOKEN_GRANT_TPYE)
                    .add("client_id", Constants.APP_CLIENT_ID)
                    .add("client_secret", Constants.APP_CLIENT_SECRET)
                    .add("refresh_token", session.getRefreshToken())
                    .build();
            Request registerToken = new Request.Builder()
                    .header("Accept", "application/vnd.minit.v1+json")
                    .url(registerUrl)
                    .post(registerBody)
                    .build();

            register.newCall(registerToken).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        int statusCode = response.code();
                        Log.e("onResponse", "inside onresponse");
                        String responseData = response.body().string();
                        Log.e("onResponse", "inside onresponse" + responseData);
                        JSONObject json = new JSONObject(responseData);
                        Log.e("statusCode", "statusCode ================== " + statusCode);
                        if (statusCode == 200) {
                            String refreshToken = json.getString("refresh_token");
                            String accessToken = json.getString("access_token");
                            String tokenType = json.getString("token_type");
                            // store the refresh token in the session
                            session.createTokenSession(accessToken, refreshToken, tokenType);
                        } else if(statusCode == 400){
                            session.logoutUser();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
    }
}
