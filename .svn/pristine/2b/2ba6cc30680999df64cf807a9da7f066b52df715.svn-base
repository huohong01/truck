package com.hsdi.NetMe.models;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.hsdi.NetMe.util.PhoneUtils;

public class Phone implements Parcelable {
    private String number;
    private int type;
    private String typeLabel;

    public Phone(String number, int type, String typeLabel) {
        this.number = number;
        this.type = type;
        this.typeLabel = typeLabel;
    }

    public String getNumber() {
        return number;
    }

    public String getFormattedNumber(Context context) {
        String formattedNumber = PhoneUtils.formatPhoneNumber(context, number);

        if(formattedNumber != null) return formattedNumber;
        else return number;
    }

    public String getPlainNumber() {
        return PhoneNumberUtil.normalizeDigitsOnly(number);
    }

    public int getType() {
        return type;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    @Override
    public String toString() {
        return "Phone{" + "number='" + number + '\'' +
                ", type=" + type +
                ", typeLabel='" + typeLabel + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString("number", number);
        bundle.putInt("type", type);
        bundle.putString("typeLabel", typeLabel);

        dest.writeBundle(bundle);
    }

    public static final Creator<Phone> CREATOR = new Creator<Phone>() {
        @Override
        public Phone createFromParcel(Parcel source) {
            Bundle bundle = source.readBundle(getClass().getClassLoader());

            return new Phone(
                    bundle.getString("number"),
                    bundle.getInt("type"),
                    bundle.getString("typeLabel")
            );
        }

        @Override
        public Phone[] newArray(int size) {
            return new Phone[size];
        }
    };
}
