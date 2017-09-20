package com.hsdi.NetMe.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.database.RegisteredContactManager;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.models.Phone;

import java.util.ArrayList;
import java.util.List;

public class PhoneUtils {

    /**
     * Sends a text message invite to a phone number with a link to the app on the app store
     * @param activity       the activity sending the invite
     * @param phoneNumber    the phone number to send the invite to
     * */
    public static void sendInvite(Activity activity, String phoneNumber) {
        Intent sendSms = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber, null));

        sendSms.putExtra("sms_body", activity.getString(R.string.invite_msg));
        sendSms.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.invite_msg));

        activity.startActivity(sendSms);
    }

    /**
     * Sends a text message invite to a phone number with a link to the app on the app store
     * @param fragment       the fragment sending the invite
     * @param phoneNumber    the phone number to send the invite to
     * */
    public static void sendInvite(Fragment fragment, String phoneNumber) {
        Intent sendSms = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber, null));

        sendSms.putExtra("sms_body", fragment.getString(R.string.invite_msg));
        sendSms.putExtra(Intent.EXTRA_TEXT, fragment.getString(R.string.invite_msg));

        fragment.startActivity(sendSms);
    }

    /**
     * Returns the user's phone number formatted in RFC3966 format.
     * @param context        context
     * @param phoneNumber    the phone number to format
     * @return The formatted number if formatting was possible, else null.
     * */
    public static String formatPhoneNumber(Context context, String phoneNumber) {
        String countryCode = PreferenceManager.getInstance(context).getCountryCode();

        // if this is a newer OS, use the new libs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                return PhoneNumberUtils.formatNumberToRFC3966(phoneNumber, countryCode).replace("tel:", "");
            }
            catch (Exception ignore) {}
        }
        // if older OS, try using the included lib
        try {
            Phonenumber.PhoneNumber number = PhoneNumberUtil.getInstance().parse(phoneNumber, countryCode);
            String numberString = PhoneNumberUtil.getInstance().format(number, PhoneNumberUtil.PhoneNumberFormat.RFC3966);
            return numberString.replace("tel:","");
        }
        catch (Exception ignore) { }

        // if there was an error with the new libs, return null
        return null;
    }

    /**
     * Shows the user a dialog with a list of phone numbers which are cross referenced with the
     * registered user database to only show registered numbers, the user then chooses a number.
     * If there is only one registered number, no dialog is shown and that number is returned in
     * the callback.
     * @param context     the context
     * @param contact     the contact to choose phone numbers from
     * @param callback    a callback used to update the calling activity of the user's selection
     * */
    public static void getRegisteredNumber(Context context, @NonNull final Contact contact, @NonNull final SelectPhoneCallback callback) {
        final List<String> regNumbers = RegisteredContactManager.getRegisteredNumbersForContact(context, contact.getId());
        performSelectionNumber(context, contact, regNumbers, callback);
    }

    /**
     * Shows the user a dialog with a list of phone numbers asking them to choose one from the list
     * @param context     the context
     * @param contact     the contact to choose phone numbers from
     * @param callback    a callback used to update the calling activity of the user's selection
     * */
    public static void selectPhoneNumber(Context context, @NonNull final Contact contact, @NonNull final SelectPhoneCallback callback) {
        final List<String> phoneNumber = new ArrayList<>();

        for (Phone phone : contact.getPhones()) {
            phoneNumber.add(phone.getPlainNumber());
        }

        performSelectionNumber(context, contact, phoneNumber, callback);
    }

    /**
     * Shows the user a dialog with a list of phone numbers asking them to choose one from the list
     * @param context     the context
     * @param contact     the contact to choose phone numbers from
     * @param numbers     a list of phone numbers in {@link String} format to ask the user to choose from
     * @param callback    a callback used to update the calling activity of the user's selection
     * */
    private static void performSelectionNumber(@NonNull Context context, @NonNull final Contact contact, final List<String> numbers, @NonNull final SelectPhoneCallback callback) {
        if(numbers.size() == 0) callback.selectedNumberIs(contact, null);
        else if (numbers.size() == 1) {
            callback.selectedNumberIs(contact, numbers.get(0));
        }
        else {
            final int choicePositionTracker[] = {0};

            new AlertDialog.Builder(context)
                    .setTitle(R.string.choose_number)
                    .setSingleChoiceItems(
                            numbers.toArray(new String[numbers.size()]),
                            choicePositionTracker[0],
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    choicePositionTracker[0] = which;
                                }
                            }
                    )
                    .setPositiveButton(
                            R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    callback.selectedNumberIs(
                                            contact,
                                            numbers.get(choicePositionTracker[0])
                                    );
                                }
                            }
                    )
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
    }
}
