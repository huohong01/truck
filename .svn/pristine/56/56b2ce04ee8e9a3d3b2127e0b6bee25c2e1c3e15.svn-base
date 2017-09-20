package com.hsdi.NetMe.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class Utils {
    private static final int BUFFER_SIZE = 1024 * 10;
    private static final int MAX_LENGTH = 8;

    /**
     * Get bitmap from a Uri object, which is retrieved from a file chooser. This also
     * ensures that the bitmap is not too large to get MemOverflow exception
     * @param context
     * @param uri
     * @return
     * @throws java.io.FileNotFoundException
     */
    public static Bitmap getBitmapFromUriChooser(Context context, Uri uri) throws FileNotFoundException {
        AssetFileDescriptor fileDescriptor = context.getContentResolver().openAssetFileDescriptor(uri, "r");

        BitmapFactory.Options o1 = new BitmapFactory.Options();
        o1.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, o1);

        return BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, null);
    }

    /**
     * Returns the bytes from the file described in the FileDescriptor
     * @param fileDescriptor    the file
     * @return a byte array of the data
     * */
    public static byte[] getBytesFromFileDescriptor(FileDescriptor fileDescriptor) throws IOException {
        // create the input and output streams
        FileInputStream fileDescStream = new FileInputStream(fileDescriptor);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        // read from the input to the output stream
        int byteReads;
        while ((byteReads = fileDescStream.read()) != -1) {
            byteStream.write(byteReads);
        }

        // write out data and close the streams
        byte[] data = byteStream.toByteArray();
        byteStream.close();
        fileDescStream.close();

        return data;
    }

    /**
     * Reshape the bitmap into a rounded shape
     * @param bitmap orignal bitmap
     * @param radius circle radius in px
     * @return reshaped bitmap, return null if fail
     */
    public static Bitmap getCircleShape(Bitmap bitmap, int radius) {
        if (bitmap == null) return null;

        try {
            Bitmap sbmp;

            if (bitmap.getWidth() != radius || bitmap.getHeight() != radius) {
                float smallest = Math.min(bitmap.getWidth(), bitmap.getHeight());
                float factor = smallest / radius;
                sbmp = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / factor),
                        (int) (bitmap.getHeight() / factor), false);
            }
            else sbmp = bitmap;

            Bitmap output = Bitmap.createBitmap(radius, radius,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, radius, radius);

            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.parseColor("#BAB399"));
            canvas.drawCircle(radius / 2 + 0.7f,
                    radius / 2 + 0.7f, radius / 2 + 0.1f, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(sbmp, rect, rect, paint);

            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

   /* *//**
     * Read bytes from a file
     * @param file the file
     * @return null if fails
     *//*
    public static byte[] getBytesFromFile(File file) {
        if (file.length() <=0) return null;
        InputStream is = null;
        try {
            int size = (int) file.length();
            byte[] data = new byte[size];
            is = new BufferedInputStream(new FileInputStream(file));
            is.read(data);

            return data;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable ignore) {}
            }
        }
        return null;
       *//* int size = (int) file.length();
        byte[] data = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int length = -1;
            while ((length = fis.read(b))!=-1){
                baos.write(b,0,length);
                baos.flush();
            }
            fis.close();
            baos.close();
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;*//*
    }*/

    /**
     * 文件转化为字节数组
     *
     * @param file
     * @return
     */
    public static byte[] getBytesFromFile(File file) {
        byte[] data = null;
        try {
            if (file == null) {
                // log.error("helper:the file is null!");
                return null;
            }
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            data = out.toByteArray();
        } catch (IOException e) {
            // log.error("helper:get bytes from file process error!");
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Read bytes from a file path
     * @param path    path to the file
     * @return null if fails
     */
    public static byte[] getBytesFromPath(String path) {
        if (path == null || path.isEmpty()) return null;

        File file = new File(path);
        return getBytesFromFile(file);
    }

    /**
     * Save a file from Uri via ContentResolver.
     * @param uri          path to database, which can be used to create an InputStream object
     * @param fullDestPath full destination path (includes file name and extension)
     * @return A real file stored in the destination directory
     * @throws IOException
     */
    public static File saveFileFromUri(Context context, Uri uri, String fullDestPath) throws IOException {
        ContentResolver contentResolver = context.getContentResolver();
        InputStream is = contentResolver.openInputStream(uri);

        return saveFileFromStream(is, fullDestPath);
    }

    /**
     * Retrieve data from a InputStream object and save the data into a file. Existing file will be deleted.
     * @param is           InputStream object
     * @param fullDestPath full destination path (includes file name and extension)
     * @return new File object contains the stream's data
     * @throws IOException
     */
    public static File saveFileFromStream(InputStream is, String fullDestPath) throws IOException {
        File file = new File(fullDestPath);

        if (file.exists()) file.delete();

        file = new File(fullDestPath);
        OutputStream os = null;

        try {
            os = new FileOutputStream(file);
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;

            while ((read = is.read(buffer)) != -1)
                os.write(buffer, 0, read);

            os.flush();
        } finally {
            if (os != null) os.close();
            if (is != null) is.close();
        }

        return file;
    }

    /**
     * Save bytes to file in the private storage
     * @param data        the data in bytes
     * @param fileName    the name to save the file number
     * @return false if fail
     */
    public static boolean savePrivateFileFromBytes(Context context, byte[] data, String fileName) {
        OutputStream os = null;

        try {
            os = new BufferedOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            os.write(data);
            os.flush();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Throwable ignore) {}
            }
        }

        return false;
    }

    /** Generates a random String of length {@value MAX_LENGTH} */
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
