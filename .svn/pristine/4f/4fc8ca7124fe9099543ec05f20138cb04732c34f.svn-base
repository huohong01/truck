package com.hsdi.NetMe.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;


/**
 * Created by huohong.yi on 2017/8/9.
 */

public class VersionUtils {
    public static final String TAG = "VersionUtils";
    public static String getApkVersion(Context context){
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(),0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "getApkVersion: package manager name is not found ");
            e.printStackTrace();
        }
        return null;
    }
}
