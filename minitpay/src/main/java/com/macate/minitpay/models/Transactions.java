package com.macate.minitpay.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Neerajakshi.Daggubat on 1/24/2017.
 */
public class Transactions implements Parcelable {
    private String createdDate;
    private String amount;
    private String barCodeId;
    private String qrcodeId;
    private String merchantName;
    private String subTotal;
    private String tax;
    private String total;
    private JSONArray items;
    private String status;

    public Transactions() {
        super();
    }

    public Transactions(JSONObject jsonObject) {
        try {
            this.barCodeId = jsonObject.getString("barcodeId");
            this.qrcodeId = jsonObject.getString("qrcodeId");
            this.createdDate = jsonObject.getJSONObject("updated_at").getString("date");
            this.merchantName = jsonObject.getString("merchant");
            this.amount = jsonObject.getString("total");
            this.status = jsonObject.getString("status");
            this.subTotal = jsonObject.getString("subtotal");
            this.tax = jsonObject.getString("tax");
            this.total = jsonObject.getString("total");
            this.items = jsonObject.getJSONObject("items").getJSONArray("items");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Transactions> parseJSON(JSONArray jsonArray) {
        ArrayList<Transactions> results = new ArrayList<Transactions>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Log.e("jsonArray", "jsonArray.length() " + jsonArray.length());
            try {
                results.add(new Transactions(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }


    public String getCreatedDate() {
        return createdDate;
    }

    public String getAmount() {
        return amount;
    }

    public String getBarCodeId() {
        return barCodeId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getStatus() {
        return status;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public String getTax() {
        return tax;
    }

    public String getTotal() {
        return total;
    }

    public JSONArray getItems() {
        return items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.createdDate);
        dest.writeString(this.amount);
        dest.writeString(this.barCodeId);
        dest.writeString(this.merchantName);
        dest.writeString(this.subTotal);
        dest.writeString(this.tax);
        dest.writeString(this.total);
        dest.writeString(this.status);
    }

    protected Transactions(Parcel in) {
        this.createdDate = in.readString();
        this.amount = in.readString();
        this.barCodeId = in.readString();
        this.merchantName = in.readString();
        this.subTotal = in.readString();
        this.tax = in.readString();
        this.total = in.readString();
        this.status = in.readString();
    }

    public static final Creator<Transactions> CREATOR = new Creator<Transactions>() {
        @Override
        public Transactions createFromParcel(Parcel source) {
            return new Transactions(source);
        }

        @Override
        public Transactions[] newArray(int size) {
            return new Transactions[size];
        }
    };
}


