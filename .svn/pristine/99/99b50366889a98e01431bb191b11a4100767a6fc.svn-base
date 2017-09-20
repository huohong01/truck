package com.hsdi.NetMe.pwd;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.hsdi.NetMe.util.MyLog;
import com.hsdi.NetMe.util.Utils;

/**
 * Created by huohong.yi on 2017/8/16.
 */

public class PwdManager {
    private Context mContext;
    private static PwdManager thiz;
    private static final String file_name = "HSDI_AES_KEY_PWD";
    public final static String k_pwd = "k_pwd";
    private SharedPreferences mShare;

    private PwdManager(Application application) throws Exception {
        mContext = application.getApplicationContext();
        mShare = mContext.getSharedPreferences(file_name, Context.MODE_PRIVATE);
        if (!mShare.contains(k_pwd)) {
            if (!mShare.edit().putString(k_pwd, Utils.random()).commit())
                throw new Exception("Write PWD failed");
            else
                MyLog.writeLog("PwdManager", "write success-->" + getPwd("PwdManager"));
        }
    }



    public synchronized static PwdManager getThiz(Application application) throws Exception {
        if (thiz == null) {
            thiz = new PwdManager(application);
        }
        return thiz;
    }

    public String getPwd(String req_str) {
        String pwd = mShare.getString(k_pwd, "");
        MyLog.writeLog("PwdManager", "read-->" + req_str + "-->" + pwd);
        return pwd;
    }

}
