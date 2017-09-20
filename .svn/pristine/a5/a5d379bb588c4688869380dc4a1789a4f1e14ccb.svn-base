package com.hsdi.NetMe.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.hsdi.NetMe.ui.startup.PhoneRetrievalActivity;
import com.hsdi.NetMe.util.PreferenceManager;

public class SmsPinListener extends BroadcastReceiver {
    private static final String TAG = "SmsPinListener";

    private static final String SERVER_NUMBER_1 = "9544177607";
    private static final String SERVER_NUMBER_2 = "7862071431";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();

            // the payload can not be empty
            if (bundle != null){
                // trying to retrieve the SMS message received
                try{
                    String format = bundle.getString("format");
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    SmsMessage[] msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){

                        // version m will do something different
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                        }
                        else msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);


                        String msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();

                        //if the message if from the server and the username/phone number hasn't
                        //  been set yet, send it to the phone retrieval activity
                        if(isValidSender(msg_from)) {
                            String pin = PhoneNumberUtil.normalizeDigitsOnly(msgBody);

                            Intent startIntent = new Intent(context, PhoneRetrievalActivity.class);
                            startIntent.putExtra(PhoneRetrievalActivity.UserPinKey, pin);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(startIntent);
                        }

                        Log.d(TAG, "Sms from " + msg_from + ", content: " + msgBody + ", have username: " + PreferenceManager.getInstance(context).haveUsername());
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    FlurryAgent.onError("smsPinListener", "Failed while reading pin", e);
                }
            }
        }
    }

    private boolean isValidSender(String senderNumber) {
        return senderNumber != null &&
                !senderNumber.isEmpty() &&
                (
                        PhoneNumberUtil.normalizeDigitsOnly(senderNumber).contains(SERVER_NUMBER_1) ||
                        PhoneNumberUtil.normalizeDigitsOnly(senderNumber).contains(SERVER_NUMBER_2)
                );
    }
}
