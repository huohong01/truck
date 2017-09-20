package com.hsdi.NetMe.ui.ContactListUtils;

import com.hsdi.NetMe.models.Contact;

public interface OnContactClickListener {
    /**
     * Called when a contact has been clicked
     * @param contact    the contact that was clicked
     * */
    void onClick(Contact contact);
}
