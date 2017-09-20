package com.hsdi.NetMe.util;

import com.hsdi.NetMe.models.Contact;

public interface SelectPhoneCallback {
    void selectedNumberIs(Contact contact, String phoneNumber);
}
