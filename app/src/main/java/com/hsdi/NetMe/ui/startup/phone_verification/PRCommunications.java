package com.hsdi.NetMe.ui.startup.phone_verification;

import com.google.i18n.phonenumbers.Phonenumber;

public interface PRCommunications {
    void setEnteredPhone(String countryCode, Phonenumber.PhoneNumber phoneNumber);
    void setPin(String serverPin, String userPin);
}
