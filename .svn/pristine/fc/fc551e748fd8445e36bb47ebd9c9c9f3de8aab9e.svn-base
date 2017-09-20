package com.hsdi.NetMe.network;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A utility that downloads a file from a URL.
 * @author www.codejava.net
 *
 */
public class HttpDownloadUtility {
    private static final String TAG = "httpDownloadUtil";

    private static final int BUFFER_SIZE = 4096;

    /**
     * Downloads a file from a URL
     * @param fileURL HTTP URL of the file to be downloaded
     * @param saveDir path of the directory to save the file
     * @throws IOException
     */
    public static void downloadFile(String fileURL, String saveDir)
            throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            }
            else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("File downloaded");
        }
        else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }

    /**
     * Downloads a file from a URL
     * @param fileURL HTTP URL of the file to be downloaded
     * @param saveDir path of the directory to save the file
     * @param fileName the name to give the file
     * @throws IOException
     */
    public static void downloadFile(String fileURL, String saveDir, String fileName) throws IOException {
        HttpURLConnection connection;
        boolean redirect;

        Log.d("yuyong_image", String.format("downloading file: fileURL = %s ,saveDir = %s , fileName = %s ",fileURL,saveDir,fileName));

        do{
            URL url = new URL(fileURL);

            connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);  //you still need to handle redirect manually.
            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM){
                // get redirect url from "location" header field
                fileURL = connection.getHeaderField("Location");
                redirect = true;
            }
            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            else if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "bad download response = " + connection.getResponseCode());
                return;
            }
            else {
                redirect = false;
                String disposition = connection.getHeaderField("Content-Disposition");
                String contentType = connection.getContentType();
                int contentLength = connection.getContentLength();

                System.out.println("Content-Type = " + contentType);
                System.out.println("Content-Disposition = " + disposition);
                System.out.println("Content-Length = " + contentLength);
            }
        } while(redirect);

        // opens input stream from the HTTP connection
        InputStream inputStream = connection.getInputStream();
        String saveFilePath = saveDir + File.separator + fileName;

        // opens an output stream to save into file
        FileOutputStream outputStream = new FileOutputStream(saveFilePath);

        int bytesRead = -1;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();
        connection.disconnect();
    }

    /**
     * Downloads a file from a URL
     * @param fileURL HTTP URL of the file to be downloaded
     * @return a byte array containing the contents of the url file, if there was an
     *      error it will return null
     * @throws IOException
     */
    public static byte[] downloadByteArray(String fileURL)
            throws IOException {
        HttpURLConnection connection;
        boolean redirect = true;

        Log.d(TAG, "downloading file");

        do{
            URL url = new URL(fileURL);

            connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);  //you still need to handle redirect manually.
            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM){
                // get redirect url from "location" header field
                fileURL = connection.getHeaderField("Location");
            }
            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            else if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "bad download response = " + connection.getResponseCode());
                return null;
            }
            else redirect = false;
        } while(redirect);

        // open input stream for HTTP connection and output to store bytes
        InputStream inputStream = connection.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // read bytes
        int bytesRead;
        byte data[] = new byte[BUFFER_SIZE];
        while((bytesRead = inputStream.read(data)) != -1) {
            outputStream.write(data, 0, bytesRead);
        }

        // close everything when done
        outputStream.close();
        inputStream.close();
        connection.disconnect();

        Log.d(TAG, "download completed");

        return outputStream.toByteArray();
    }
}
