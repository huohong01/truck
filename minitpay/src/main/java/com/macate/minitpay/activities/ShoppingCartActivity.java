package com.macate.minitpay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.macate.minitpay.R;
import com.macate.minitpay.adapters.ItemsAdapter;
import com.macate.minitpay.helpers.Constants;
import com.macate.minitpay.helpers.SessionManager;
import com.macate.minitpay.helpers.Utils;
import com.macate.minitpay.models.Items;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShoppingCartActivity extends AppCompatActivity {

    ImageView ivMerchantLogo;
    TextView tvMerchantName,tvSubTotal, tvTax,tvTip, tvTotal;
    Button btnPay, btnCancel;
    ListView lvItems;
    String barCodeId;
    JSONObject json;
    ArrayList<Items> alInvoiceItems;
    ItemsAdapter adInvoiceItems;
    SessionManager session;
    String errorMessage;
    String[] arrayErrors;
    Toolbar mToolbar;
    String strType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        session = new SessionManager(ShoppingCartActivity.this);

        strType = getIntent().getStringExtra("type");

        // setup Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        ShoppingCartActivity.this.setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        barCodeId= session.getKeyBarCode();

        Log.e("barCodeId","barCodeId========================== "+barCodeId);

        ivMerchantLogo = (ImageView) findViewById(R.id.ivMerchantLogo);
        tvMerchantName = (TextView) findViewById(R.id.tvMerchantName);
        tvSubTotal = (TextView) findViewById(R.id.tvSubTotal);
        tvTax = (TextView) findViewById(R.id.tvTax);
        tvTip = (TextView) findViewById(R.id.tvTip);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        btnPay = (Button) findViewById(R.id.btnPay);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        if(strType.equalsIgnoreCase("receipt")){
            ShoppingCartActivity.this.setTitle("Receipt");
            btnPay.setVisibility(View.GONE);

        } else {
            ShoppingCartActivity.this.setTitle("Shopping Cart");
            btnPay.setVisibility(View.VISIBLE);
        }

        lvItems = (ListView) findViewById(R.id.lvItems);
        alInvoiceItems = new ArrayList<Items>();
        adInvoiceItems = new ItemsAdapter(ShoppingCartActivity.this, alInvoiceItems);
        fetchInvoiceDetails(barCodeId);
        lvItems.setAdapter(adInvoiceItems);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShoppingCartActivity.this, HomeActivity.class);
                i.putExtra("flag","no");
                startActivity(i);
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("","on click of btn pay");
                pay();
            }
        });
    }

    public void onBackPressed(){
        Intent i = new Intent(ShoppingCartActivity.this, HomeActivity.class);
        i.putExtra("flag","no");
        startActivity(i);
    }

    public void pay(){
        Log.e("","on click of btn pay");
        String registerUrl = Constants.BASE_URL + Constants.GET_CLEARED_INOVICES_HISTORY + "/" + session.getKeyBarCode();
        // should be a singleton
        OkHttpClient register = new OkHttpClient();

        RequestBody registerBody = new FormBody.Builder()
                .add("payAmount", tvTotal.getText().toString())
                .build();
        Request registerToken = new Request.Builder()
                .header("Accept", Constants.ACCESS_TOKEN_ACCEPT)
                .header("Authorization", session.getTokenType() + " " + session.getAccessToken())
                .url(registerUrl)
                .put(registerBody)
                .build();

        register.newCall(registerToken).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    int statusCode = response.code();
                    errorMessage = "";
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    if (statusCode == 200) {
                        ShoppingCartActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(ShoppingCartActivity.this, ShoppingCartActivity.class);
                                i.putExtra("type","receipt");
                                startActivity(i);
                            }
                        });

                    } else if (statusCode == 422) {
                        JSONObject errors = json.getJSONObject("errors");
                        Iterator keys = errors.keys();

                        // loop to get the dynamic key
                        String currentDynamicKey = (String) keys.next();
                        // get the value of the dynamic key
                        String currentDynamicValue = errors.getString(currentDynamicKey);
                        // Toast.makeText(RegisterActivity.this, currentDynamicValue, Toast.LENGTH_LONG).show();
                        // do something here with the value...

                        errorMessage =  currentDynamicValue ;

                        ShoppingCartActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                arrayErrors = errorMessage.split(",");
                                Log.e("arrayErrors", "arrayErrors ================================= " + arrayErrors[0]);
                                Utils.showAlertDialog(ShoppingCartActivity.this, Constants.ERROR, arrayErrors[0].substring(2, arrayErrors[0].length() - 2), false);
                            }
                        });
                    } else if(statusCode == 401) {
                        if (!Utils.isNetworkAvailable(ShoppingCartActivity.this) || !Utils.isOnline()) {
                            Utils.showInternetAlertDialog(ShoppingCartActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                        } else {
                            Utils.generateAccessTokenBasedOnRefreshToken(ShoppingCartActivity.this);
                        }
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
    }
    public void fetchInvoiceDetails(String barCodeId) {
        if (!Utils.isNetworkAvailable(ShoppingCartActivity.this) || !Utils.isOnline()) {
            Utils.showInternetAlertDialog(ShoppingCartActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
        } else {
            String accessTokenUrl = Constants.BASE_URL + Constants.GET_CLEARED_INOVICES_HISTORY+"/"+barCodeId;

            Log.e("accessTokenUrl", "accessTokenUrl======================================  " + accessTokenUrl);
            // should be a singleton
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .header("Accept", "application/vnd.minit.v1+json")
                    .header("Authorization", session.getTokenType() + " " + session.getAccessToken())
                    .url(accessTokenUrl)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        int statusCode = response.code();
                        String responseData = response.body().string();
                        json = new JSONObject(responseData);
                        Log.e("responseData", "responseData======================================  " + responseData);
                        // Run view-related code back on the main thread
                        if (statusCode == 200) {
                            ShoppingCartActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        tvMerchantName.setText(json.getJSONObject("data").getString("merchant"));
                                        tvSubTotal.setText(json.getJSONObject("data").getString("subtotal"));
                                        tvTax.setText(json.getJSONObject("data").getString("tax"));
                                        tvTotal.setText(json.getJSONObject("data").getString("total"));

                                        alInvoiceItems.clear();
                                        btnPay.setText("Pay " + json.getJSONObject("data").getString("total"));

                                        alInvoiceItems = Items.parseJSON(json.getJSONObject("data").getJSONObject("items").getJSONArray("items"));

                                        Log.e("","alInvoiceItems ================ "+alInvoiceItems.size());
                                        if (alInvoiceItems.size() > 0) {
                                            lvItems.setVisibility(View.VISIBLE);
                                            adInvoiceItems.clear();
                                            adInvoiceItems.addAll(alInvoiceItems);
                                            adInvoiceItems.notifyDataSetChanged();
                                            lvItems.setAdapter(adInvoiceItems);

                                        } else {
                                            lvItems.setVisibility(View.GONE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        } else if (statusCode == 401) {
                            if (!Utils.isNetworkAvailable(ShoppingCartActivity.this) || !Utils.isOnline()) {
                                Utils.showInternetAlertDialog(ShoppingCartActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                            } else {
                                Utils.generateAccessTokenBasedOnRefreshToken(ShoppingCartActivity.this);
                            }
                        } else {
                            String errorString = json.getString("message");
                            Utils.showAlertDialog(ShoppingCartActivity.this, Constants.ERROR, errorString, false);
                        }
                    } catch (JSONException e) {
                        try {
                            String errorString = json.getString("message");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
