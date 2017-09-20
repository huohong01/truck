package com.hsdi.NetMe.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;

import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.util.PreferenceManager;

/**
 * show the user a dialog when a contact in a video message is unknown. allows the user to add the
 * contact to the device's contacts
 * */
public class UnknownContactActivity extends Activity {

    public final static String EXTRA_CONTACT_OBJECT = "contact_object";
    private final static int REQUEST_NEW_CONTACT = 100;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unknown_contact);

        // getting the passed contact
        contact = getIntent().getParcelableExtra(EXTRA_CONTACT_OBJECT);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // asking the user if they wish to add this contact
        new AlertDialog.Builder(this)
                .setTitle(R.string.add_contact)
                .setMessage(R.string.add_selected_contact_msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // making the call to the this contact with the contact's set name and number
                        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, contact.getPhones().get(0));
                        intent.putExtra(ContactsContract.Intents.Insert.NAME, contact.getName());
                        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                        intent.putExtra("finishActivityOnSaveCompleted", true);
                        startActivityForResult(intent, REQUEST_NEW_CONTACT);

                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // not adding this contact, closing
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if contact added, perform check of all the contacts again
        if(resultCode == RESULT_OK){
            PreferenceManager.getInstance(this).resetTimeToCheckContactRegistrations();
            NetMeApp.loadContactList();
        }

        // whatever the result, end
        finish();
    }
}
