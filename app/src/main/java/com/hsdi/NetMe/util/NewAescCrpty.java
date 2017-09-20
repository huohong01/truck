package com.hsdi.NetMe.util;

import java.io.UnsupportedEncodingException;

import javax.crypto.NoSuchPaddingException;

/**
 * Created by huohong.yi on 2017/4/20.
 */

public class NewAescCrpty extends AESCrypt {
    public NewAescCrpty(String password) throws Exception {
        super(password);
    }

    @Override
    public byte[] decryptBytes(byte[] encryptedBytes) throws UnsupportedEncodingException, NoSuchPaddingException{
        return null;
    }


}
