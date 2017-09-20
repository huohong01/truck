package com.hsdi.theme.basic;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.hsdi.theme.file.FileOperater;

import java.io.File;
import java.io.FileFilter;
import java.lang.ref.WeakReference;

/**
 * Created by Yu Yong on 2017/8/9.
 */

public class ThemeManager {

    private final static String _res_file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/NetMe/Theme/res.";

    //adb push F:\work\dailyWork\20170817\NetMe\app_theme_green\build\outputs\apk\app_theme_green-debug.apk /sdcard/NetMe/Theme/res.GREEN

    public final static String getResFile(ThemeName name) {
        return _res_file + name.name();
    }

    public enum ThemeName {
        ORANGE_DEFAULT,
        BLUE,
        RED,
        GREEN;


        public static String getFileName(ThemeName name) {
            return "THEME/" + name.name();
        }
    }

    public interface OnApplyListener {
        void onApply(boolean isSuccess);
    }

    private WeakReference<Context> mContext;

    public ThemeManager(Context context) {
        mContext = new WeakReference<Context>(context);
    }

    public static ThemeName getCurrentTheme() {
        File[] res_files = new File(_res_file).getParentFile().listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().startsWith("res.");
            }
        });
        if (res_files == null || res_files.length < 1)
            return ThemeName.ORANGE_DEFAULT;
        String res_name = res_files[0].getName().replace(".", "##");
        return ThemeName.valueOf(res_name.split("##")[1]);
    }

    public void applyTheme(final ThemeName theme, final OnApplyListener listener) {
        new AsyncTask<ThemeName, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(final ThemeName... params) {
                //移除文件
                File[] files = new File(_res_file).getParentFile().listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().startsWith("res.");
                    }
                });
                boolean del_file = files == null;
                if (!del_file) {
                    del_file = true;
                    for (File file : files) {
                        del_file &= FileOperater.deleteFile(file);
                    }
                }
                if (params[0] == ThemeName.ORANGE_DEFAULT || !del_file) {
                    return del_file;
                }
                //拷贝文件
                return FileOperater.writeAssetsToSd(ThemeName.getFileName(params[0]), mContext.get(), getResFile(theme));
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                listener.onApply(aBoolean);
            }
        }.execute(theme);
    }
}
