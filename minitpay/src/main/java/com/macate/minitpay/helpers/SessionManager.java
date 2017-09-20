package com.macate.minitpay.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.macate.minitpay.activities.LoginActivity;

/**
 * Created by Neerajakshi.Daggubat on 1/17/2017.
 */
public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "BeyondWirelessPref";
    // All Shared Preferences Keys

    private static final String KEY_REFRESH_TOKEN = "refreshToken";
    private static final String KEY_TOKEN_TYPE = "tokenType";
    private static final String KEY_ACCESS_TOKEN = "accessToken";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_BAR_CODE = "barCodeId";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createTokenSession(String accessToken, String refreshToken, String tokenType){
        // Storing phone # in pref
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_TOKEN_TYPE, tokenType);
        // commit changes
        editor.commit();
    }

    public void createBarCode(String barCodeId){
        // Storing phone # in pref
        editor.putString(KEY_BAR_CODE, barCodeId);
        // commit changes
        editor.commit();
    }

    public void createUserSession(String email, String userId){
        // Storing phone # in pref
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USER_ID, userId);
        // commit changes
        editor.commit();
    }

       /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        String email = getKeyEmail();
        editor.clear();
        editor.putString(KEY_EMAIL, email);
        // commit changes
        editor.commit();
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);
    }

    public void setRefreshToken (String token){
        editor.putString(KEY_REFRESH_TOKEN, token);
        editor.commit();
    }

    public void setAccessToken (String token){
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.commit();
    }

    public String getRefreshToken (){
        return pref.getString(KEY_REFRESH_TOKEN, "");
    }


       public String getAccessToken (){
        return pref.getString(KEY_ACCESS_TOKEN, "");
    }

    public String getTokenType (){
        return pref.getString(KEY_TOKEN_TYPE, "");
    }
    public String getKeyBarCode (){
        return pref.getString(KEY_BAR_CODE, "");
    }

    public String getKeyEmail (){
        return pref.getString(KEY_EMAIL, "");
    }

    public String getUserId (){
        return pref.getString(KEY_USER_ID, "");
    }

}
