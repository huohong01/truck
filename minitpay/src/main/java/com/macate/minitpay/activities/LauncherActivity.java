package com.macate.minitpay.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.macate.minitpay.R;
import com.macate.minitpay.helpers.Constants;
import com.macate.minitpay.helpers.Utils;


public class LauncherActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        if (Utils.checkPermission(Manifest.permission.READ_PHONE_STATE, getApplicationContext(), LauncherActivity.this)) {
        }
        else
        {
            String message = "Phone State permission allows us to access phone details. Please allow the app to read phone details";
            Utils.requestPermission(Manifest.permission.READ_PHONE_STATE, PERMISSION_REQUEST_CODE_LOCATION, LauncherActivity.this, LauncherActivity.this, message);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    Intent i = new Intent(LauncherActivity.this, LoginActivity.class);
                    startActivity(i);
            }
        }, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("is_first", false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("is_first", Boolean.TRUE);
            edit.commit();
        }
    }
}
