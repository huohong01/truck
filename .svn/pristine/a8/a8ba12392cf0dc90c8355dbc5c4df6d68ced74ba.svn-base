package com.hsdi.MinitPay.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_MULTI_PROCESS;

/**
 * Created by huohong.yi on 2017/2/24.
 */

public class SharePrefManager {

    public static final String NETME_MINITPAY_PREF_NAME = "minitpay preferences";

    private Context mContext;
    private SharedPreferences preferences;
    private static SharePrefManager instance;

    public SharePrefManager(Context context){
        mContext = context;
        preferences = mContext.getSharedPreferences(NETME_MINITPAY_PREF_NAME, MODE_MULTI_PROCESS);
    }

    public static SharePrefManager getInstance(Context context){
        if (instance == null){
            instance = new SharePrefManager(context);
        }
        return instance;
    }

    public void cleanSharePrefManager(){
        preferences.edit().clear().commit();
    }

}
