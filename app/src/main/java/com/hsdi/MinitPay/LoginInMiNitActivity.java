package com.hsdi.MinitPay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginInMiNitActivity extends AppCompatActivity {


    @Bind(R.id.et_email)
    EditText etEmail;
    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.btn_signIn)
    Button btnSignIn;

    JSONObject json;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_minit_pay);
        ButterKnife.bind(this);
        session = new SessionManager(this);

        Log.e("yi", "LoginInMiNitActivity:onCreate: ===================================");

       btnSignIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Log.e("yi", "onClick: ====================================" );
               signInMinitPay();
           }
       });
    }
    //when login in successfully,set is_login = true;
    public void setInLogined(){
        SharedPreferences preferences = getSharedPreferences(SharePrefManager.NETME_MINITPAY_PREF_NAME, MODE_MULTI_PROCESS);
        boolean isLogined = preferences.getBoolean("is_login", false);
        if (!isLogined) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("is_login", true);
            edit.commit();
        }
    }

    public void signInMinitPay(){
        String email = etEmail.getText().toString();
        String pwd = etPwd.getText().toString();
        if (!Utils.isNetworkAvailable(this) || !Utils.isOnline()) {
            Utils.showInternetAlertDialog(this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
        } else {
            String refreshTokenUrl = Constants.BASE_URL + Constants.ACCESS_TOKEN;
            Log.e("refreshTokenUrl", "refreshTokenUrl ==========================================   *********************      " + refreshTokenUrl);
            // should be a singleton
            OkHttpClient refreshTokenClient = new OkHttpClient();

            RequestBody refreshTokenBody = new FormBody.Builder()
                    .add("grant_type", Constants.APP_PASSWORD_GRANT_TPYE)
                    .add("client_id", Constants.APP_CLIENT_ID)
                    .add("client_secret", Constants.APP_CLIENT_SECRET)
                    .add("username", email)
                    .add("password", pwd)
                    .build();
            Request refreshTokenRequest = new Request.Builder()
                    .header("Accept", "application/vnd.minit.v1+json")
                    .url(refreshTokenUrl)
                    .post(refreshTokenBody)
                    .build();

            refreshTokenClient.newCall(refreshTokenRequest).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        String responseData = response.body().string();
                        Log.e("reponse","responseData ==========================================   *********************      "+responseData);
                        JSONObject json = new JSONObject(responseData);
                        int statusCode = response.code();
                        Log.e("statusCode","statusCode ==========================================   *********************      "+statusCode);
                        if (statusCode == 200) {
                            String refreshToken = json.getString("refresh_token");
                            String accessToken = json.getString("access_token");
                            String tokenType = json.getString("token_type");
                            // store the refresh token in the session
                            session.createTokenSession(accessToken, refreshToken, tokenType);
                            //  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            //data/data/包名/shared_pref   MODE_MULTI_PROCESS表示多个进程可以访问
                            setInLogined();
                            Intent intent = new Intent(LoginInMiNitActivity.this, MinitPayActivity.class);
                            startActivity(intent);

                        }  else {
                            final String stautsMessage = json.getString("message");

                            LoginInMiNitActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(LoginActivity.this, stautsMessage, Toast.LENGTH_SHORT);
                                    Utils.showAlertDialog(LoginInMiNitActivity.this, Constants.ERROR, stautsMessage, false);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        String errorMessage = null;
                        try {
                            errorMessage = json.getString("message");
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
