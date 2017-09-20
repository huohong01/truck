package com.hsdi.NetMe.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Email implements Parcelable {
    private String address;
    private int type;
    private String typeLabel;

    public Email(String address, int type, String typeLabel) {
        this.address = address;
        this.type = type;
        this.typeLabel = typeLabel;
    }

    public String getAddress() {
        return address;
    }

    public int getType() {
        return type;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    @Override
    public String toString() {
        return "Email{" + "address='" + address + '\'' +
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
        bundle.putString("address", address);
        bundle.putInt("type", type);
        bundle.putString("typeLabel", typeLabel);

        dest.writeBundle(bundle);
    }

    public static final Creator<Email> CREATOR = new Creator<Email>() {
        @Override
        public Email createFromParcel(Parcel source) {
            Bundle bundle = source.readBundle(getClass().getClassLoader());

            return new Email(
                    bundle.getString("address"),
                    bundle.getInt("type"),
                    bundle.getString("typeLabel")
            );
        }

        @Override
        public Email[] newArray(int size) {
            return new Email[size];
        }
    };
}
