package com.macate.minitpay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.macate.minitpay.R;
import com.macate.minitpay.adapters.TransactionsAdapter;
import com.macate.minitpay.helpers.Constants;
import com.macate.minitpay.helpers.SessionManager;
import com.macate.minitpay.helpers.Utils;
import com.macate.minitpay.models.Transactions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Neerajakshi.Daggubat on 1/30/2017.
 */
public class PurchaseHistoryActivity extends AppCompatActivity {

    ListView lvTransaction;
    ArrayList<Transactions> alTransaction;
    TransactionsAdapter adTransactions;
    SessionManager session;
    JSONObject json;
    TextView tvNoRecordsFound;
    ImageButton ibBack;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        // setup Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        PurchaseHistoryActivity.this.setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvNoRecordsFound = (TextView) findViewById(R.id.tvNoRecordsFound);
        session = new SessionManager(PurchaseHistoryActivity.this);

        lvTransaction = (ListView) findViewById(R.id.lvTransactions);
        ibBack = (ImageButton) findViewById(R.id.ibBackArrow);
        alTransaction = new ArrayList<Transactions>();

        adTransactions = new TransactionsAdapter(PurchaseHistoryActivity.this, alTransaction);
        displayTransactions();
        lvTransaction.setAdapter(adTransactions);
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String barCodeId =    result.getContents();
                String scanFormat = result.getFormatName();

                Log.e("barCodeId", "barCodeId ============================================   " + barCodeId);
                session.createBarCode(barCodeId);
                Intent i = new Intent (PurchaseHistoryActivity.this, ShoppingCartActivity.class);
                i.putExtra("type","receipt");
                startActivity(i);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void displayTransactions() {
        if (!Utils.isNetworkAvailable(PurchaseHistoryActivity.this) || !Utils.isOnline()) {
            Utils.showInternetAlertDialog(PurchaseHistoryActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
        } else {
            String accessTokenUrl = Constants.BASE_URL + Constants.GET_CLEARED_INOVICES_HISTORY;

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
                            PurchaseHistoryActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        alTransaction.clear();
                                        alTransaction = Transactions.parseJSON(json.getJSONArray("data"));
                                        if (alTransaction.size() != 0) {
                                            adTransactions.clear();
                                            adTransactions.addAll(alTransaction);
                                            adTransactions.notifyDataSetChanged();
                                            lvTransaction.setAdapter(adTransactions);
                                            tvNoRecordsFound.setVisibility(View.GONE);
                                            lvTransaction.setVisibility(View.VISIBLE);
                                        } else {
                                            lvTransaction.setVisibility(View.GONE);
                                            tvNoRecordsFound.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (statusCode == 401) {
                            if (!Utils.isNetworkAvailable(PurchaseHistoryActivity.this) || !Utils.isOnline()) {
                                Utils.showInternetAlertDialog(PurchaseHistoryActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                            } else {
                                Utils.generateAccessTokenBasedOnRefreshToken(PurchaseHistoryActivity.this);
                            }
                        } else {
                            String errorString = json.getString("message");
                            Utils.showAlertDialog(PurchaseHistoryActivity.this, Constants.ERROR, errorString, false);
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

