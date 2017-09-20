package com.macate.minitpay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.macate.minitpay.R;
import com.macate.minitpay.adapters.PaymentSourcesListAdapter;
import com.macate.minitpay.helpers.Constants;
import com.macate.minitpay.helpers.SessionManager;
import com.macate.minitpay.helpers.Utils;
import com.macate.minitpay.models.PaymentSources;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ManageCardActivity extends AppCompatActivity {
    TextView btnAddCreditCard;
    ListView lvPaymentSource;
    JSONObject json;
    Toolbar mToolbar;
    SessionManager session;
    ImageButton ibLogout;

    private ArrayList<PaymentSources> alPayment;
    private PaymentSourcesListAdapter adPayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_card);

        session = new SessionManager(ManageCardActivity.this);
        // setup Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        ManageCardActivity.this.setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnAddCreditCard = (TextView) findViewById(R.id.btnAddCreditCard);


        lvPaymentSource = (ListView) findViewById(R.id.lvPaymentSource);
        alPayment = new ArrayList<PaymentSources>();
        adPayment = new PaymentSourcesListAdapter(ManageCardActivity.this, alPayment);
        addPaymentSources();

        btnAddCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageCardActivity.this, AddCardActivity.class);
                i.putExtra("parent","Home");
                startActivity(i);
            }
        });

        /*lvPaymentSource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ManageCardActivity.this, BuyDataActivity.class);
                PaymentSources ps = alPayment.get(position);
                i.putExtra("item", ps);
                startActivity(i);
            }
        });*/
    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    private void addPaymentSources() {
        if (!Utils.isNetworkAvailable(ManageCardActivity.this) || !Utils.isOnline()) {
            Utils.showInternetAlertDialog(ManageCardActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
        } else {
            String accessTokenUrl = Constants.BASE_URL + Constants.SOURCES_STRIPE;
            // should be a singleton
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .header("Accept", Constants.ACCESS_TOKEN_ACCEPT)
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
                        // Run view-related code back on the main thread
                        if (statusCode == 200) {
                            ManageCardActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        alPayment.clear();
                                        alPayment = PaymentSources.parseJSON(json.getJSONArray("data"));
                                        adPayment.clear();
                                        adPayment.addAll(alPayment);
                                        lvPaymentSource.setAdapter(adPayment);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if(statusCode == 401) {
                            if (!Utils.isNetworkAvailable(ManageCardActivity.this) || !Utils.isOnline()) {
                                Utils.showInternetAlertDialog(ManageCardActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                            } else {
                                Utils.generateAccessTokenBasedOnRefreshToken(ManageCardActivity.this);
                            }
                        } else {
                            String errorString = json.getString("message");
                            Utils.showAlertDialog(ManageCardActivity.this, Constants.ERROR, errorString, false);
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

    /*@Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(ManageCardActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // call this to finish the current activity
    }*/
}
