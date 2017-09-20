package com.hsdi.MinitPay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hsdi.MinitPay.util.SharePrefManager;
import com.hsdi.NetMe.R;
import com.macate.minitpay.helpers.Constants;
import com.macate.minitpay.helpers.SessionManager;
import com.macate.minitpay.helpers.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MinitPayActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int LOGIN_REQUEST_CODE = 10;

    @Bind(R.id.btn_sign_in)
    Button btnSignIn;
    @Bind(R.id.btn_scan_code)
    Button btnScanCode;
    @Bind(R.id.btn_show_code)
    Button btnShowCode;
    @Bind(R.id.btn_balance)
    Button btnBalance;

    SessionManager session;
    private JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minit_pay);
        ButterKnife.bind(this);
        Log.e("yi", "MinitPayActivity:onCreate: ==========================" );
        session = new SessionManager(this);

        btnSignIn.setOnClickListener(this);
        btnScanCode.setOnClickListener(this);
        btnShowCode.setOnClickListener(this);
        btnBalance.setOnClickListener(this);

    }

    //判断是否登录
    private boolean isLogin() {
        SharedPreferences preferences = getSharedPreferences(SharePrefManager.NETME_MINITPAY_PREF_NAME, MODE_MULTI_PROCESS);
        boolean is_login = preferences.getBoolean("is_login", false);
        return is_login;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLogin()) {
            btnSignIn.setText("SIGN OUT");
            btnScanCode.setEnabled(true);
            btnShowCode.setEnabled(true);
            btnBalance.setEnabled(true);
        }else {
            btnSignIn.setText("SIGN In");
            btnScanCode.setEnabled(false);
            btnShowCode.setEnabled(false);
            btnBalance.setEnabled(false);
        }
        Log.e("yi", "MinitPayActivity:onResume: ");
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_sign_in:
                signInRequest();
                break;

            case R.id.btn_show_code:
                showCodeRequest();
                break;

            case R.id.btn_scan_code:
                scanCodeRequest();
                break;

            case R.id.btn_balance:
                showBalanceRequest();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }else {
                String barCodeId = result.getContents();
                String formatName = result.getFormatName();
                session.createBarCode(barCodeId);
                Log.e("yi", "barCodeId:========================= "+ barCodeId );
                Intent intent = new Intent(this, PayActivity.class);
                startActivity(intent);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
    //sign in
    private void signInRequest(){
        Log.e("yi", "signInRequest:isLogin() ==============  " + isLogin() );
        if (isLogin()){
            Log.e("yi", "signInRequest:isLogin() =======================================  " + isLogin() );
            Intent intent = new Intent(MinitPayActivity.this, LoginOutActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MinitPayActivity.this, LoginInMiNitActivity.class);
            startActivity(intent);
        }
    }

    //scan code
    private void scanCodeRequest(){
        new IntentIntegrator(this).initiateScan();
    }


    private void showBalanceRequest(){
        if (!Utils.isNetworkAvailable(this) || !Utils.isOnline()) {
            Utils.showInternetAlertDialog(this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
        }   else {
            String accessTokenUrl = Constants.BASE_URL + Constants.USER_DETAILS;
            // should be a singleton
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .header("Accept", Constants.ACCESS_TOKEN_ACCEPT)
                    .header("Authorization", session.getTokenType() + " " + session.getAccessToken())
                    .url(accessTokenUrl)
                    .build();
            Log.e("email","inside showbalance       =========================  ============     ");

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
                           runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String userId = json.getJSONObject("data").getString("id");
                                        String email = json.getJSONObject("data").getString("email");
                                        String balance;
                                        balance = json.getJSONObject("data").getString("minitbalance");
                                        session.createUserSession(email,userId);
                                        Log.e("email","email ================================= "+session.getKeyEmail() + "======userId" + session.getUserId());
                                        Intent intent = new Intent(MinitPayActivity.this, BalanceActivity.class);
                                        intent.putExtra("balance",balance);
                                        intent.putExtra("userId",userId);
                                        intent.putExtra("email",email);
                                        startActivity(intent);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        if (statusCode == 404) {
                            final String message = json.getString("message");
                            MinitPayActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.showAlertDialog(MinitPayActivity.this, Constants.ERROR, message, false);
                                }
                            });

                        } else if (statusCode == 401) {
                            MinitPayActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!Utils.isNetworkAvailable(MinitPayActivity.this) || !Utils.isOnline()) {
                                        Utils.showInternetAlertDialog(MinitPayActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                                    } else {
                                        Utils.generateAccessTokenBasedOnRefreshToken(MinitPayActivity.this);
                                    }
                                }
                            });

                        } else {
                           final String message = json.getString("message");
                            MinitPayActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   Utils.showAlertDialog(MinitPayActivity.this, Constants.ERROR, message, true);
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

    private void showCodeRequest(){
        if (!Utils.isNetworkAvailable(this) || !Utils.isOnline()) {
            Utils.showInternetAlertDialog(this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
        }   else {
            String accessTokenUrl = Constants.BASE_URL + Constants.USER_DETAILS;
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
                        Log.e("yi","email       =========================  ============ "+statusCode);
                        String responseData = response.body().string();
                        if (statusCode == 200) {
                            json = new JSONObject(responseData);
                            Log.e("json","json       ========================= ============     "+json);
                            // Run view-related code back on the main thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String userId = json.getJSONObject("data").getString("id");
                                        String email = json.getJSONObject("data").getString("email");
                                        session.createUserSession(email,userId);
                                        Log.e("yi","email   =========================  ============     "+session.getKeyEmail());
                                        Intent intent = new Intent(MinitPayActivity.this, ShowCodeActivity.class);
                                        intent.putExtra("userId",userId);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        if (statusCode == 404) {
                            final String message = json.getString("message");
                            MinitPayActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.showAlertDialog(MinitPayActivity.this, Constants.ERROR, message, false);
                                }
                            });

                        } else if (statusCode == 401) {
                            MinitPayActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!Utils.isNetworkAvailable(MinitPayActivity.this) || !Utils.isOnline()) {
                                        Utils.showInternetAlertDialog(MinitPayActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                                    } else {
                                        Utils.generateAccessTokenBasedOnRefreshToken(MinitPayActivity.this);
                                    }
                                }
                            });

                        } else {
                            final String message = json.getString("message");
                            MinitPayActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.showAlertDialog(MinitPayActivity.this, Constants.ERROR, message, true);
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
