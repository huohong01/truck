package com.hsdi.NetMe.ui.chat.util;

import android.os.AsyncTask;
import android.util.Log;

import com.hsdi.NetMe.models.ManagedMessage;
import com.hsdi.NetMe.util.AESCrypt;
import com.macate.csmp.CSMPCryptStringUtil;
import com.macate.csmp.CSMPIndexingKeyGenerator;
import com.macate.csmp.CSMPKeyGenerator;

/**
 * Created by huohong.yi on 2017/3/29.
 */

public class DecryptMessageAsyntask extends AsyncTask<ManagedMessage,Void,String> {

    INotificationMessageListener iNotificationMessageListener;

    public DecryptMessageAsyntask(INotificationMessageListener iNotificationMessageListener) {
        this.iNotificationMessageListener = iNotificationMessageListener;
    }

    @Override
    protected String doInBackground(ManagedMessage... params) {
        ManagedMessage managedMessage = params[0];
        String decryptedMessage = null;
        try {
            // try to decrypt using AES
            if(managedMessage.getMessage().mayBeAesEncrypted()) {
                AESCrypt aesCrypt = new AESCrypt(managedMessage.getSenderUsername());
                // attempt to decrypt the message
                decryptedMessage = aesCrypt.decryptString(managedMessage.getOriginalMessage());
                Log.e("yi", "run: =============================decryptedMessage  = " + decryptedMessage );
                // if decryption worked, set the message text
                managedMessage.setDecryptedText(decryptedMessage)
                        .setTextStatus(ManagedMessage.STATUS_READY);
            }
            // try to decrypt using the Codetel lib
            else if(managedMessage.getMessage().mayBeCodetelEncrypted()) {
                // create the key gen for this message
                CSMPKeyGenerator keyGen = new CSMPIndexingKeyGenerator(
                        // since the managed message was created using a message, this will never be null
                        managedMessage.getSenderUsername().getBytes(),
                        managedMessage.getSubject().getBytes()
                );

                String encryptedMsg = managedMessage.getOriginalMessage();

                // attempt to decrypt the message
                decryptedMessage = CSMPCryptStringUtil.decrypt(keyGen, encryptedMsg);

                // if decryption worked, set the message text
                managedMessage.setDecryptedText(decryptedMessage)
                        .setTextStatus(ManagedMessage.STATUS_READY);

            }
            else throw new Exception("Couldn't determine the decryption method");
        }
        catch (Exception e) {
            Log.e("yi", "Failed to decrypt message with id " + managedMessage.getMessageId(), e);
            // in the case where decryption didn't work, just set the original message
            managedMessage.setDecryptedText(managedMessage.getOriginalMessage())
                    .setTextStatus(ManagedMessage.STATUS_READY);

        }
        return decryptedMessage;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result!=null){
            iNotificationMessageListener.onMessageSuccess(result);
        }
    }
}

