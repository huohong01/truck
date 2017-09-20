package com.hsdi.theme.file;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Yu Yong on 2017/8/9.
 */

public class FileOperater {
    public static boolean writeAssetsToSd(String file_name, Context context, String file_abs_path) {
        File obj_file = new File(file_abs_path);
        if (obj_file.exists())
            return true;
        if (!obj_file.getParentFile().exists()) {
            obj_file.getParentFile().mkdirs();
        }
        try {
            obj_file.createNewFile();
            InputStream inputStream = context.getAssets().open(file_name);
            FileOutputStream fout = new FileOutputStream(obj_file);
            byte[] buffer = new byte[1024 * 10];
            while (true) {
                int len = inputStream.read(buffer);
                if (len == -1) {
                    break;
                }
                fout.write(buffer, 0, len);
            }
            inputStream.close();
            fout.close();
            return true;
        } catch (Exception e) {
            Log.i("yuyong", "copy file failed-->" + e.getMessage());
            return false;
        }
    }

    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return true;
        }
        File tmp_file = new File(file.getAbsolutePath() + ".tmp");
        file.renameTo(tmp_file);
        tmp_file.delete();
        if (!file.exists()) {
            return true;
        }
        return false;
    }
}
