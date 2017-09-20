package com.macate.minitpay.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.macate.minitpay.R;
import com.macate.minitpay.adapters.CustomSpinnerAdapter;
import com.macate.minitpay.helpers.BluePayHelper;
import com.macate.minitpay.helpers.BluePayRequest;
import com.macate.minitpay.helpers.Constants;
import com.macate.minitpay.helpers.SessionManager;
import com.macate.minitpay.helpers.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class  AddCardActivity extends AppCompatActivity {
    EditText etName, etCardNumber,   etCVC, etAddress, etCity, etState,etZip;
    Button btnSave;
    Spinner spExpirationMonth, spExpirationYear, spCountry;
    SessionManager session;
    JSONObject json;
    String errorMessage;
    String[] arrayErrors;
    String year, month;
    Toolbar mToolbar;
    CheckBox chkMakePrimary;
    String country;
    ImageButton ibLogout;
    String parentActivity, superParent;
    int isChecked = 0;
    ArrayList<String> alYears, alMonths, alCountries,alCountriesSorted;
    CustomSpinnerAdapter adYears, adMonths, adCountries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        session = new SessionManager(AddCardActivity.this);
        parentActivity = getIntent().getStringExtra("parent");
        superParent = getIntent().getStringExtra("superParent");

        // setup Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        AddCardActivity.this.setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etName = (EditText) findViewById(R.id.etName);
        etCardNumber = (EditText) findViewById(R.id.etCardNumber);
        spExpirationMonth = (Spinner) findViewById(R.id.spExpirationMonth);
        spExpirationYear = (Spinner) findViewById(R.id.spExpirationYear);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etCVC = (EditText) findViewById(R.id.etCVC);
        etCity = (EditText) findViewById(R.id.etCity);
        etState = (EditText) findViewById(R.id.etState);
        etZip = (EditText) findViewById(R.id.etZip);
        spCountry = (Spinner) findViewById(R.id.spCountry);
        chkMakePrimary = (CheckBox)findViewById(R.id.chkMakePrimary);

      /*  if(parentActivity.equalsIgnoreCase("EditDeviceDetails")){
            selectedItem = getIntent().getParcelableExtra("selectedItem");
        }
*/
        btnSave = (Button) findViewById(R.id.btnSave);
        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        etName.setTypeface(typeFace);
        etCardNumber.setTypeface(typeFace);
        etAddress.setTypeface(typeFace);
        etCVC.setTypeface(typeFace);
        etCity.setTypeface(typeFace);
        etState.setTypeface(typeFace);
        etZip.setTypeface(typeFace);
        chkMakePrimary.setTypeface(typeFace);
        btnSave.setTypeface(typeFace);
        //spCardType = (Spinner) findViewById(R.id.spCardType);

        chkMakePrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkMakePrimary.isChecked()){
                    isChecked = 1;
                } else {
                    isChecked = 0;
                }

            }
        });
        alYears = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        alYears.add("Year");
        for (int i = thisYear; i <= thisYear+10; i++) {
            alYears.add(Integer.toString(i));
        }
        adYears = new CustomSpinnerAdapter(this, alYears);
        adYears.setDropDownViewResource(R.layout.list_payment_source);
        spExpirationYear.setAdapter(adYears);

        spExpirationYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String completeYear = alYears.get(position);
                    year = completeYear.substring(2);
                    Log.e("year","year ----- "+year);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Utils.showAlertDialog(AddCardActivity.this, Constants.ERROR, Constants.SELECT_YEAR, false);
                return;
            }
        });

        alMonths = new ArrayList<String>();
        alMonths.add("Month");
        for (int i = 1; i <= 12; i++) {
            alMonths.add(Integer.toString(i));
        }
        adMonths = new CustomSpinnerAdapter(this, alMonths);
        adMonths.setDropDownViewResource(R.layout.list_payment_source);
        spExpirationMonth.setAdapter(adMonths);

        spExpirationMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    month = alMonths.get(position);
                    Log.e("month","month -------------------------------- "+month);
                    if(Integer.parseInt(month) < 10){
                        month = "0"+month;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Utils.showAlertDialog(AddCardActivity.this, Constants.ERROR, Constants.SELECT_MONTH, false);
                return;
            }
        });

        Locale[] locale = Locale.getAvailableLocales();
        alCountries = new ArrayList<String>();
        alCountriesSorted = new ArrayList<String>();
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !alCountries.contains(country) ){
                alCountries.add( country );
            }
        }
        Collections.sort(alCountries, String.CASE_INSENSITIVE_ORDER);

        alCountriesSorted.add("Select Country");
        alCountriesSorted.addAll(alCountries);

        adCountries = new CustomSpinnerAdapter(this, alCountriesSorted);
        adCountries.setDropDownViewResource(R.layout.list_payment_source);
        spCountry.setAdapter(adCountries);


        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    country = alCountries.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Utils.showAlertDialog(AddCardActivity.this, Constants.ERROR, Constants.SELECT_COUNTRY, false);
                return;
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spExpirationMonth.getSelectedItemPosition() == 0 || spExpirationYear.getSelectedItemPosition() == 0 || etCardNumber.getText().toString().equals("") || etCVC.getText().toString().equals("")) {
                    Utils.showAlertDialog(AddCardActivity.this, Constants.INPUT_ERROR, "Enter all the fields", false);
                } else if (!Utils.isNetworkAvailable(AddCardActivity.this) || !Utils.isOnline()) {
                    Utils.showInternetAlertDialog(AddCardActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                } else {
                    // Set Customer Information
                    HashMap<String, String> customerParams = new HashMap<>();
                    customerParams.put("firstName", etName.getText().toString());
                    customerParams.put("address1", etAddress.getText().toString());
                    customerParams.put("city", etCity.getText().toString());
                    customerParams.put("state", etState.getText().toString());
                    customerParams.put("zip", etZip.getText().toString());
                    customerParams.put("country", country);
                    customerParams.put("cardNumber",  etCardNumber.getText().toString());
                    customerParams.put("expirationDate", month+year);
                    customerParams.put("cvv2", etCVC.getText().toString());
                    customerParams.put("amount", "0.00");

                    HashMap<String, String> payment = (HashMap<String, String>) BluePayHelper.doPost(customerParams);
                    Log.e("status","get Status ============ "+payment);
                    Log.e("status","get Status ============ "+payment.get("STATUS"));

                    // If transaction was successful reads the responses from BluePay
                    if (payment.get("STATUS").equalsIgnoreCase("1")) {
                      /*  Log.e("BluePay","Transaction Status: " + payment.getStatus());
                        Log.e("BluePay", "Transaction ID: " + payment.getTransID());
                        Log.e("BluePay", "Transaction Message: " + payment.getMessage());
                        Log.e("BluePay", "AVS Result: " + payment.getAVS());
                        Log.e("BluePay", "CVV2: " + payment.getCVV2());
                        Log.e("BluePay", "Masked Payment Account: " + payment.getMaskedPaymentAccount());
                        Log.e("BluePay","Card Type: " + payment.getCardType());
                        Log.e("BluePay", "Authorization Code: " + payment.getAuthCode());*/



                        String accessTokenUrl = Constants.BASE_URL + Constants.SOURCES_STRIPE;
                        // should be a singleton
                        OkHttpClient client = new OkHttpClient();
                        RequestBody formBody = new FormBody.Builder()
                                .add("transaction_status", payment.get("STATUS"))
                                .add("transaction_message", payment.get("MESSAGE"))
                                .add("transaction_id", payment.get("TRANS_ID"))
                                .add("avs_response", payment.get("AVS"))
                                .add("cvs_response", payment.get("CVV2"))
                                .add("masked_account", payment.get("PAYMENT_ACCOUNT_MASK"))
                                .add("card_type", payment.get("CARD_TYPE"))
                                .add("authorization_code", payment.get("AUTH_CODE"))
                                .build();
                        Request request = new Request.Builder()
                                .header("Accept", "application/vnd.minitpay.v1+json")
                                .header("Authorization", session.getTokenType() + " " + session.getAccessToken())
                                .url(accessTokenUrl)
                                .post(formBody)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                try {
                                    int statusCode = response.code();
                                    String responseData = response.body().string();
                                    json = new JSONObject(responseData);
                                    if (statusCode == 200) {

                                        if(parentActivity.equalsIgnoreCase("TopupActivity")) {
                                            Intent i = new Intent(AddCardActivity.this, TopupActivity.class);
                                            i.putExtra("parentActivity",superParent);
                                            startActivity(i);
                                        } /*else if (parentActivity.equalsIgnoreCase("EditDeviceDetails")){
                                        Intent i = new Intent(AddCardActivity.this, EditDeviceDetails.class);
                                        i.putExtra("selectedItem",selectedItem);
                                        startActivity(i);
                                    }*/else if (parentActivity.equalsIgnoreCase("Home")){
                                    Intent i = new Intent(AddCardActivity.this, HomeActivity.class);
                                    startActivity(i);
                                }
                            }  else if (statusCode == 422) {
                                JSONObject errors = json.getJSONObject("errors");

                                Iterator keys = errors.keys();
                                // loop to get the dynamic key
                                String currentDynamicKey = (String) keys.next();
                                // get the value of the dynamic key
                                String currentDynamicValue = errors.getString(currentDynamicKey);
                                errorMessage = currentDynamicValue;

                                AddCardActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        arrayErrors = errorMessage.split(",");
                                        Log.e("arrayErrors", "arrayErrors ================================= " + arrayErrors[0]);
                                        Utils.showAlertDialog(AddCardActivity.this, Constants.ERROR, arrayErrors[0].substring(2, arrayErrors[0].length() - 2), false);
                                    }
                                });

                            } else if(statusCode == 401) {
                                if (!Utils.isNetworkAvailable(AddCardActivity.this) || !Utils.isOnline()) {
                                    Utils.showInternetAlertDialog(AddCardActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                                } else {
                                    Utils.generateAccessTokenBasedOnRefreshToken(AddCardActivity.this);
                                }
                            }  else {
                                final String message = json.getString("message");
                                AddCardActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.showAlertDialog(AddCardActivity.this, Constants.CARD_ERROR, message, false);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                });

            } else {
                        Utils.showAlertDialog(AddCardActivity.this, Constants.CARD_ERROR, payment.get("MESSAGE"), false);
                    }
                }
            }
        });
        }
    @Override
    protected void onResume()
    {
        super.onResume();

    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if(parentActivity.equalsIgnoreCase("BuyDataActivity")) {
            startActivity(new Intent(AddCardActivity.this, HomeActivity.class));
            finish();
        } /*else if (parentActivity.equalsIgnoreCase("EditDeviceDetails")){
            Intent i = new Intent(AddCardActivity.this, EditDeviceDetails.class);
            i.putExtra("selectedItem",selectedItem);
            startActivity(i);
            finish();

        } */else if(parentActivity.equalsIgnoreCase("Home")) {
            startActivity(new Intent(AddCardActivity.this, HomeActivity.class));
            finish();
        }

    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }


    }


