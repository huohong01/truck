package com.hsdi.NetMe.util;

import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypt {
    private static final String TAG = "AESCrypt";

    private final Cipher cipher;
    private final SecretKeySpec key;
    private AlgorithmParameterSpec spec;


    public AESCrypt(String password) throws Exception {
        // hash password with SHA-256 and crop the output to 128-bit for key
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

//        byte[] tmp = password.getBytes("UTF-8");
//        byte[] key_tmp = new byte[tmp.length];
//        System.arraycopy(tmp, 0, key_tmp, 0, tmp.length);
//        Log.i("yuyong", String.format("tmp--->%s----key_tmp--->%s", Arrays.toString(tmp), Arrays.toString(key_tmp)));

      /*  digest.update(password.getBytes("UTF-8"));
        byte[] bytes_digest = digest.digest();
        byte[] keyBytes = new byte[bytes_digest.length];
        System.arraycopy(bytes_digest, 0, keyBytes, 0, bytes_digest.length > 32 ? 32 : bytes_digest.length);
        Log.i("yuyong", String.format("digest.digest()--->%s----keyBytes--->%s", Arrays.toString(bytes_digest), Arrays.toString(keyBytes)));*/

        /* byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] keyBytes = digest.digest();*/

        digest.update(password.getBytes("UTF-8"));
        byte[] keyBytes = new byte[32];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        //cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        key = new SecretKeySpec(keyBytes, "AES");
        spec = getIV();
    }

    private static AlgorithmParameterSpec getIV() {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivParameterSpec;
        ivParameterSpec = new IvParameterSpec(iv);
        return ivParameterSpec;
    }

    public String encryptString(String plainText) throws Exception {
        byte[] encrypted = encryptBytes(plainText.getBytes("UTF-8"));
        String encrypt = new String(Base64.encode(encrypted, Base64.DEFAULT), "UTF-8");
        Log.i("yuyong", String.format("encryptString:--->encrypt:%s",encrypt));
        return encrypt;
    }

    public String decryptString(String encryptedText) throws Exception {
        byte[] bytes = Base64.decode(encryptedText, Base64.DEFAULT);
        byte[] decrypted = decryptBytes(bytes);
        String decrypt = new String(decrypted, "UTF-8");
        Log.i("yuyong", String.format("encryptString:--->decrypt:%s",decrypt));
        return decrypt;
    }

    public String encryptUrlSafeString(String plainText) throws Exception {
        byte[] encryptedBytes = encryptBytes(plainText.getBytes("UTF-8"));
      //  Log.i("yuyong", String.format("encryptUrlSafeString:--->plainText:%s--->encryptedBytes:%s",plainText, Arrays.toString(encryptedBytes)));
        byte[] bytes = Base64.encode(encryptedBytes, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
      //  Log.i("yuyong", String.format("encryptUrlSafeString:--->plainText:%s--->bytes:%s",plainText, new String(bytes,"UTF-8")));
        return new String(bytes, "UTF-8");
    }

    public String decryptUrlSafeString(String encryptedText) throws Exception {
        Log.i("yuyong", String.format("decryptUrlSafeString:--->encryptedText:%s",encryptedText));
        byte[] bytes = Base64.decode(encryptedText, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
      //  Log.i("yuyong", String.format("decryptUrlSafeString:--->encryptedText:%s--->bytes:%s",encryptedText, Arrays.toString(bytes)));
        byte[] decrypted = decryptBytes(bytes);
     //   Log.i("yuyong", String.format("decryptUrlSafeString:--->decrypted:%s",new String(decrypted, "UTF-8")));
        return new String(decrypted, "UTF-8");
    }

    public byte[] encryptLocation(LatLng location) throws Exception {
        String locationString = location.latitude + "," + location.longitude;
        return encryptBytes(locationString.getBytes("UTF-8"));
    }

    public LatLng decryptLocation(byte[] encryptedBytes) throws Exception {
        byte[] decryptedBytes = decryptBytes(encryptedBytes);
        String locationString = new String(decryptedBytes, "UTF-8");
        double lat = Double.parseDouble(
                locationString.substring(
                        0,
                        locationString.indexOf(",")
                )
        );
        double lng = Double.parseDouble(
                locationString.substring(
                        locationString.indexOf(",") + 1,
                        locationString.length()
                )
        );
        return new LatLng(lat, lng);
    }

    public byte[] encryptBytes(byte[] plainBytes) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        return cipher.doFinal(plainBytes);
    }

    public byte[] decryptBytes(byte[] encryptedBytes) throws UnsupportedEncodingException, NoSuchPaddingException {

        if (1 == 0) {
            try {
                MessageDigest tmp_digest = MessageDigest.getInstance("SHA-256");
                tmp_digest.update(("aaaaaaaaaa").getBytes("UTF-8"));
                byte[] keyBytes = new byte[32];
                System.arraycopy(tmp_digest.digest(), 0, keyBytes, 0, keyBytes.length);

                SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
                Cipher tmp_cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                tmp_cipher.init(Cipher.DECRYPT_MODE, key, getIV());
                byte[] cipherText = tmp_cipher.doFinal(encryptedBytes);
            } catch (Exception e) {
                Log.i("yuyong_chiher_1", e.getMessage());
            }
        }
        byte[] cipherText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
         //   Log.i("yuyong", String.format("decryptBytes:--->encryptedBytes:%s",Arrays.toString(encryptedBytes)));
            cipherText = cipher.doFinal(encryptedBytes);
         //   Log.i("yuyong", String.format("decryptBytes:--->cipherText:%s",Arrays.toString(cipherText)));
        } catch (Exception e) {
            Log.i("yuyong_chiher_2", e.getMessage());
        }
        return cipherText;

    }
}
