package com.hsdi.MinitPay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hsdi.NetMe.R;
import com.macate.minitpay.helpers.Constants;
import com.macate.minitpay.helpers.SessionManager;
import com.macate.minitpay.helpers.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.macate.minitpay.helpers.Utils.session;

public class PayActivity extends AppCompatActivity {


    @Bind(R.id.tv_invoice)
    TextView tvInvoice;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.btn_pay)
    Button btnPay;

    JSONObject json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("yi", "PayActivity:onClick:===============================pay ");
                pay();
            }
        });
        session = new SessionManager(this);

        String barCode = session.getKeyBarCode();
        fetchInvoiceDetails(barCode);
        tvInvoice.setText(barCode);

    }

    public void fetchInvoiceDetails(String barCodeId) {
        if (!Utils.isNetworkAvailable(this) || !Utils.isOnline()) {
            Utils.showInternetAlertDialog(this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
        } else {
            String accessTokenUrl = Constants.BASE_URL + Constants.GET_CLEARED_INOVICES_HISTORY+"/"+barCodeId;

            Log.e("accessTokenUrl", "ShoppingCartActivity:accessTokenUrl======================================  " + accessTokenUrl);
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
                        Log.e("responseData", "ShoppingCartActivity:responseData======================================  " + responseData);
                        // Run view-related code back on the main thread
                        if (statusCode == 200) {
                            PayActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String price = json.getJSONObject("data").getString("total");
                                        Log.e("yi", "price=========================== "+ price );
                                        tvPrice.setText(price);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        } else if (statusCode == 401) {
                            PayActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!Utils.isNetworkAvailable(PayActivity.this) || !Utils.isOnline()) {
                                        Utils.showInternetAlertDialog(PayActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                                    } else {
                                        Utils.generateAccessTokenBasedOnRefreshToken(PayActivity.this);
                                    }
                                }
                            });

                        } else {

                            PayActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String errorString = json.getString("message");
                                        Utils.showAlertDialog(PayActivity.this, Constants.ERROR, errorString, false);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
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


    String errorMessage;
    String[] arrayErrors;
    public void pay(){
        Log.e("","on click of btn pay");
        String registerUrl = Constants.BASE_URL + Constants.GET_CLEARED_INOVICES_HISTORY + "/" + session.getKeyBarCode();
        // should be a singleton
        OkHttpClient register = new OkHttpClient();

        RequestBody registerBody = new FormBody.Builder()
                .add("payAmount", tvPrice.getText().toString())
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
                    Log.e("yi", "onResponse: ============================="+ responseData );
                    JSONObject json = new JSONObject(responseData);
                    if (statusCode == 200) {
                        PayActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(PayActivity.this, MinitPayActivity.class);
                                startActivity(intent);
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

                        PayActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                arrayErrors = errorMessage.split(",");
                                Log.e("arrayErrors", "arrayErrors ================================= " + arrayErrors[0]);
                                Utils.showAlertDialog(PayActivity.this, Constants.ERROR, arrayErrors[0].substring(2, arrayErrors[0].length() - 2), false);
                            }
                        });
                    } else if(statusCode == 401) {
                        PayActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!Utils.isNetworkAvailable(PayActivity.this) || !Utils.isOnline()) {
                                    Utils.showInternetAlertDialog(PayActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                                } else {
                                    Utils.generateAccessTokenBasedOnRefreshToken(PayActivity.this);
                                }
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
    }
}
