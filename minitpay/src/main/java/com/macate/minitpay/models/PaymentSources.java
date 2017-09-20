package com.macate.minitpay.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Neerajakshi.Daggubat on 8/31/2016.
 */
public class PaymentSources implements Parcelable {
    private String paymentSourceId;
    private String cardType;
    private String cardNo;

    public PaymentSources() {
        super();
    }

    public PaymentSources(JSONObject jsonObject) {
        try {
            this.paymentSourceId = jsonObject.getString("id");
            this.cardType = jsonObject.getString("card_type");
            this.cardNo = jsonObject.getString("masked_account");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<PaymentSources> parseJSON(JSONArray jsonArray) {
        ArrayList<PaymentSources> results = new ArrayList<PaymentSources>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                results.add(new PaymentSources(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getPaymentSourceId() {
        return paymentSourceId;
    }

    public void setPaymentSourceId(String paymentSourceId) {
        this.paymentSourceId = paymentSourceId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.paymentSourceId);
        dest.writeString(this.cardType);
        dest.writeString(this.cardNo);
    }

    protected PaymentSources(Parcel in) {
        this.paymentSourceId = in.readString();
        this.cardType = in.readString();
        this.cardNo = in.readString();
    }

    public static final Creator<PaymentSources> CREATOR = new Creator<PaymentSources>() {
        @Override
        public PaymentSources createFromParcel(Parcel source) {
            return new PaymentSources(source);
        }

        @Override
        public PaymentSources[] newArray(int size) {
            return new PaymentSources[size];
        }
    };
}
