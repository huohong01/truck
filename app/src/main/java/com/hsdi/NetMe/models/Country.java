package com.hsdi.NetMe.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Country implements Comparable<Country>{
    @SerializedName("name")
    private String countryName;
    @SerializedName("dial_code")
    private String callingCode;
    @SerializedName("code")
    private String countryCode;

    public Country(String countryName, String callingCode, String countryCode) {
        this.countryName = countryName;
        this.callingCode = callingCode;
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCallingCode() {
        return callingCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;
        String name = country.getCountryName();
        String callCode = country.getCallingCode();
        String code = country.getCountryCode();

        return (countryName != null && name != null && countryName.equals(name))
                || (callingCode != null && callCode != null && callingCode.equals(callCode))
                        || (countryCode != null && code != null && countryCode.equals(code));

    }

    @Override
    public int compareTo(@NonNull Country another) {
        return countryName.compareTo(another.getCountryName());
    }

    @Override
    public String toString() {
        return "Country{" +
                "countryName='" + countryName + '\'' +
                ", callingCode='" + callingCode + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
