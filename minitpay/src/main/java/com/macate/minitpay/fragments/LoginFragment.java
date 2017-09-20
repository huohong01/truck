package com.macate.minitpay.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.macate.minitpay.R;
import com.macate.minitpay.activities.HomeActivity;
import com.macate.minitpay.helpers.Constants;
import com.macate.minitpay.helpers.SessionManager;
import com.macate.minitpay.helpers.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Neerajakshi.Daggubat on 1/17/2017.
 */
public class LoginFragment extends Fragment {
    View view;
    EditText etAccount, etPassword;
    TextView tvForgotPassword;
    Button btnSignIn;
    JSONObject json;
    SessionManager session;
    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getActivity());

        Log.e("","inside login fragment on create");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signin, container, false);


        Log.e("", "inside login fragment on createview");

        etAccount = (EditText) view.findViewById(R.id.etAccount);
        etPassword = (EditText) view.findViewById(R.id.etPassword);

      //  tvForgotPassword = (TextView) view.findViewById(R.id.tvForgotPassword);
        btnSignIn = (Button) view.findViewById(R.id.btnSignIn);
        Typeface typeFace= Typeface.createFromAsset(getResources().getAssets(), "fonts/Roboto-Regular.ttf");
        etAccount.setTypeface(typeFace);
        etPassword.setTypeface(typeFace);
        btnSignIn.setTypeface(typeFace);

/*
        chkRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    session.createEmailSession(etAccount.getText().toString());
                } else {
                    session.createEmailSession("");
                }
            }
        });

        if(chkRememberMe.isChecked()){
            session.createEmailSession(etAccount.getText().toString());
        } else {
            session.createEmailSession("");
        }
*/


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createToken();
            }
        });



      /*  tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    *//*Intent i = new Intent(getActivity(), ResetPasswordSendEmail.class);
                    startActivity(i);*//*
            }
        });*/
        return view;
    }

    public void createToken(){
        if (!Utils.isNetworkAvailable(getActivity()) || !Utils.isOnline()) {
            Utils.showInternetAlertDialog(getActivity(), Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
        } else {
            String refreshTokenUrl = Constants.BASE_URL + Constants.ACCESS_TOKEN;

            Log.e("refreshTokenUrl", "refreshTokenUrl ==========================================   *********************      " + refreshTokenUrl);

            // should be a singleton
            OkHttpClient refreshTokenClient = new OkHttpClient();

            RequestBody refreshTokenBody = new FormBody.Builder()
                    .add("grant_type", Constants.APP_PASSWORD_GRANT_TPYE)
                    .add("client_id", Constants.APP_CLIENT_ID)
                    .add("client_secret", Constants.APP_CLIENT_SECRET)
                    .add("username", etAccount.getText().toString())
                    .add("password", etPassword.getText().toString())
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

                           /* Intent i = new Intent(getActivity(), HomeActivity.class);
                            i.putExtra("flag","no");
                            startActivity(i);*/
                            //finish();
                            //lookForDevice();
                        }  else {
                            final String stautsMessage = json.getString("message");
                            Intent i = new Intent(getActivity(), HomeActivity.class);
                            i.putExtra("flag","no");
                            startActivity(i);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(LoginActivity.this, stautsMessage, Toast.LENGTH_SHORT);
                                    Utils.showAlertDialog(getActivity(), Constants.ERROR, stautsMessage, false);
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