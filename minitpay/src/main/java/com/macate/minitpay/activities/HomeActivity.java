package com.macate.minitpay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.macate.minitpay.R;
import com.macate.minitpay.dialogs.LockScreenDialog;
import com.macate.minitpay.helpers.Constants;
import com.macate.minitpay.helpers.SessionManager;
import com.macate.minitpay.helpers.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {
    Button btnBuySomething, btnPurchaseHistory, btnManageCard, btnBuyData ;
    SessionManager session;
    TextView tvDisplayBalance;
    JSONObject json;
    String message, presentDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        session = new SessionManager(HomeActivity.this);

        presentDialog = getIntent().getStringExtra("flag");
        btnBuySomething = (Button) findViewById(R.id.btnBuySomething);
        tvDisplayBalance = (TextView) findViewById(R.id.tvDisplayBalance);
        btnPurchaseHistory = (Button) findViewById(R.id.btnPurchaseHistory);
        btnManageCard = (Button) findViewById(R.id.btnManageCard);
        //btnManageAccount = (Button) findViewById(R.id.btnManageAccount);
        btnBuyData = (Button) findViewById(R.id.btnBuyData);

        displayBalance();

        btnBuySomething.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(HomeActivity.this).initiateScan(); // `this` is the current Activity
            }
        });

        btnPurchaseHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (HomeActivity.this, PurchaseHistoryActivity.class);
                startActivity(i);
            }
        });

        btnManageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (HomeActivity.this, ManageCardActivity.class);
                startActivity(i);
            }
        });

       /* btnManageAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (HomeActivity.this, ManageAccountActivity.class);
                startActivity(i);
            }
        });*/

        btnBuyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, TopupActivity.class);
                i.putExtra("parentActivity", "HomeActivity");
                startActivity(i);
            }
        });

        if(presentDialog.equalsIgnoreCase("yes")){
            LockScreenDialog dialog = LockScreenDialog.newInstance("message");
            dialog.show(getSupportFragmentManager(), "compose_message");
        }

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
                session.createBarCode(barCodeId);

                Log.e("barCodeId", "barCodeId ============================================   " + barCodeId);

                Intent i = new Intent (HomeActivity.this, HomeActivity.class);
                i.putExtra("flag","yes");
                startActivity(i);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void displayBalance() {
        if (!Utils.isNetworkAvailable(HomeActivity.this) || !Utils.isOnline()) {
            HomeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.showInternetAlertDialog(HomeActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                }
            });
        }   else {
            String accessTokenUrl = Constants.BASE_URL + Constants.USER_DETAILS;
            // should be a singleton
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .header("Accept", Constants.ACCESS_TOKEN_ACCEPT)
                    .header("Authorization", session.getTokenType() + " " + session.getAccessToken())
                    .url(accessTokenUrl)
                    .build();
            Log.e("email","inside displaybalance       =========================  ============     ");

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        int statusCode = response.code();
                        Log.e("email","email       =========================  ============     "+statusCode);
                        String responseData = response.body().string();
                        if (statusCode == 200) {
                            json = new JSONObject(responseData);
                            Log.e("json","json       =========================  ============     "+json);
                            // Run view-related code back on the main thread
                            HomeActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String userId = json.getJSONObject("data").getString("id");
                                        String email = json.getJSONObject("data").getString("email");
                                        String balance="0";
                                        balance = json.getJSONObject("data").getString("minitbalance");
                                        session.createUserSession(email,userId);
                                        Log.e("email","email       =========================  ============     "+session.getKeyEmail());
                                        tvDisplayBalance.setText(balance);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        if (statusCode == 404) {
                            final String message = json.getString("message");
                            HomeActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.showAlertDialog(HomeActivity.this, Constants.ERROR, message, false);
                                }
                            });

                        } else if (statusCode == 401) {
                            if (!Utils.isNetworkAvailable(HomeActivity.this) || !Utils.isOnline()) {
                                Utils.showInternetAlertDialog(HomeActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                            } else {
                                Utils.generateAccessTokenBasedOnRefreshToken(HomeActivity.this);
                            }
                        } else {
                            message = json.getString("message");
                            HomeActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.showAlertDialog(HomeActivity.this, Constants.ERROR, message, true);
                                }
                            });
                        }
                    } catch (JSONException e) {
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
