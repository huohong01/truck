package com.hsdi.NetMe.util;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by preetha on 3/21/2016.
 */
public class RootUtils {
    public static boolean isDeviceRooted(Context context) {
        boolean r1 = checkRootMethod1();
        boolean r2 = checkRootMethod2();
        boolean r3 = checkRootMethod3();
        boolean r4 = checkRootMethod4(context);
        System.err.println("r1 = " + r1 + " r2 = " + r2 + " r3 = " + r3 + " r4 = " + r4);
        return r1 || r2 || r3 || r4;
//        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3() || checkRootMethod4(context);
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su" };
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }
    public static boolean checkRootMethod4(Context context) {
        return isPackageInstalled("eu.chainfire.supersu", context);
    }

    private static boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
