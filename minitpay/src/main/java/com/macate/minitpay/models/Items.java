package com.macate.minitpay.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Neerajakshi.Daggubat on 1/25/2017.
 */
public class Items implements Parcelable {
    private String itemName;
    private String itemQunatity;
    private String itemPrice;
    private String totalItemPrice;
    public Items() {
        super();
    }

    public Items(JSONObject jsonObject) {
        try {
            this.itemName = jsonObject.getString("t");
            this.itemQunatity = jsonObject.getString("q");
            this.itemPrice = jsonObject.getString("p");
            this.totalItemPrice = jsonObject.getString("it");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Items> parseJSON(JSONArray jsonArray) {
        ArrayList<Items> results = new ArrayList<Items>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Log.e("jsonArray", "jsonArray.length() " + jsonArray.length());
            try {
                results.add(new Items(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemQunatity() {
        return itemQunatity;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getTotalItemPrice() {
        return totalItemPrice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemName);
        dest.writeString(this.itemQunatity);
        dest.writeString(this.itemPrice);
        dest.writeString(this.totalItemPrice);
    }

    protected Items(Parcel in) {
        this.itemName = in.readString();
        this.itemQunatity = in.readString();
        this.itemPrice = in.readString();
        this.totalItemPrice = in.readString();
    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel source) {
            return new Items(source);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };
}



