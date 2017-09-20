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
import com.macate.minitpay.activities.LoginActivity;
import com.macate.minitpay.helpers.Constants;
import com.macate.minitpay.helpers.SessionManager;
import com.macate.minitpay.helpers.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

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
public class RegisterFragment extends Fragment {
    View view;
    String errorMessage, accessToken, tokenType;
    EditText etEmail, etPassword, etFirstName, etLastName;
    TextView tvForgotPassword;
    Button btnSignup;
    JSONObject json;
    String responseData ;
    SessionManager session;
    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);

        session = new SessionManager(getActivity());
        etEmail = (EditText) view.findViewById(R.id.etEmail);

        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etLastName = (EditText) view.findViewById(R.id.etLastName);
        btnSignup = (Button) view.findViewById(R.id.btnSignUp);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTokenNetworkCall();
            }
        });
        Typeface typeFace= Typeface.createFromAsset(getResources().getAssets(), "fonts/Roboto-Regular.ttf");
        etEmail.setTypeface(typeFace);
        etPassword.setTypeface(typeFace);
        etFirstName.setTypeface(typeFace);
        etLastName.setTypeface(typeFace);
        btnSignup.setTypeface(typeFace);

        return view;
    }


    private void createNewTokenNetworkCall() {
        if (!Utils.isNetworkAvailable(getActivity()) || !Utils.isOnline()) {
            Utils.showInternetAlertDialog(getActivity(), Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
        } else {
            String accessTokenUrl = Constants.BASE_URL + Constants.ACCESS_TOKEN;

            Log.e("accessTokenUrl","accessTokenUrl =================================================  "+accessTokenUrl);
            // should be a singleton
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("grant_type", Constants.APP_CLIENT_CREDENTIALS_GRANT_TPYE)
                    .add("client_id", Constants.APP_CLIENT_ID)
                    .add("client_secret", Constants.APP_CLIENT_SECRET)
                    .build();
            Request request = new Request.Builder()
                    .header("Accept", Constants.ACCESS_TOKEN_ACCEPT)
                    .url(accessTokenUrl)
                    .post(formBody)
                    .build();

            Log.e("request","formBody =================================================  "+formBody.toString());

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        responseData = response.body().string();
                        Log.e("reponse","responseData ==========================================   *********************      "+responseData);
                        json = new JSONObject(responseData);
                        int statusCode = response.code();
                        Log.e("reponse","responseData ==========================================   *********************      "+responseData);
                        if (statusCode == 200) {
                            accessToken = json.getString("access_token");
                            tokenType = json.getString("token_type");

                            Log.e("accessToken", "accessToken =================================================  " + accessToken);

                            callRegisterNetworkCall();
                        } else {
                            final String stautsMessage = json.getString("message");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(LoginActivity.this, stautsMessage, Toast.LENGTH_SHORT);
                                    Utils.showAlertDialog(getActivity(), Constants.ERROR, stautsMessage, false);
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


    private void callRegisterNetworkCall(){
        if (!Utils.isNetworkAvailable(getActivity()) || !Utils.isOnline()) {
            Utils.showInternetAlertDialog(getActivity(), Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
        } else {
            String registerUrl = Constants.BASE_URL + Constants.REGISTER_URL;
            // should be a singleton
            OkHttpClient register = new OkHttpClient();
            Log.e("registerUrl", "registerUrl =================================================  " + registerUrl);
            RequestBody registerBody = new FormBody.Builder()
                    .add("firstname", etFirstName.getText().toString())
                    .add("lastname", etLastName.getText().toString())
                    .add("email", etEmail.getText().toString())
                    .add("password", etPassword.getText().toString())
                    .add("password_confirmation", etPassword.getText().toString())
                    .build();
            Request registerToken = new Request.Builder()
                    .header("Accept", Constants.ACCESS_TOKEN_ACCEPT)
                    .header("Authorization", tokenType + " " + accessToken)
                    .url(registerUrl)
                    .post(registerBody)
                    .build();

            register.newCall(registerToken).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        int statusCode = response.code();
                        errorMessage = "";
                        String responseData = response.body().string();
                        Log.e("responseData","responseData ================================= "+responseData);
                        JSONObject json = new JSONObject(responseData);
                        if (statusCode == 200) {
                            // createRefreshToken(etEmail.getText().toString(), etPassword.getText().toString());

                            Intent i = new Intent (getActivity(), LoginActivity.class);
                            startActivity(i);
                        } else if (statusCode == 422) {
                            JSONObject errors = json.getJSONObject("errors");
                            Iterator keys = errors.keys();

                            // while (keys.hasNext()) {
                            // loop to get the dynamic key
                            String currentDynamicKey = (String) keys.next();
                            // get the value of the dynamic key
                            String currentDynamicValue = errors.getString(currentDynamicKey);
                            // Toast.makeText(getActivity(), currentDynamicValue, Toast.LENGTH_LONG).show();
                            // do something here with the value...

                            errorMessage = currentDynamicValue;
                            Log.e("errorMessage","errorMessage ================================= "+errorMessage);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(LoginActivity.this, stautsMessage, Toast.LENGTH_SHORT);
                                    String[] arrayErrors = errorMessage.split(",");
                                    Log.e("arrayErrors", "arrayErrors ================================= " + arrayErrors[0]);
                                    Utils.showAlertDialog(getActivity(), Constants.ERROR, arrayErrors[0].substring(2, arrayErrors[0].length() - 2), false);
                                }
                            });
                            // }
                        } else {
                            final String stautsMessage = json.getString("message");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(LoginActivity.this, stautsMessage, Toast.LENGTH_SHORT);
                                    Utils.showAlertDialog(getActivity(), Constants.ERROR, stautsMessage, false);
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



    public void createRefreshToken(String email, String password) {
        if (!Utils.isNetworkAvailable(getActivity()) || !Utils.isOnline()) {
            Utils.showInternetAlertDialog(getActivity(), Constants.NETWORK_CONNECTION_ERROR,Constants.CHECK_NETWORK_CONNECTIONS, false);
        } else {
            errorMessage = "";
            String refreshTokenUrl = Constants.BASE_URL + Constants.ACCESS_TOKEN;
            // should be a singleton
            OkHttpClient refreshTokenClient = new OkHttpClient();

            RequestBody refreshTokenBody = new FormBody.Builder()
                    .add("grant_type", Constants.APP_PASSWORD_GRANT_TPYE)
                    .add("client_id", Constants.APP_CLIENT_ID)
                    .add("client_secret", Constants.APP_CLIENT_SECRET)
                    .add("username", email)
                    .add("password", password)
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
                        int statusCode = response.code();
                        String responseData = response.body().string();
                        json = new JSONObject(responseData);

                        if (statusCode == 200) {
                            String refreshToken = json.getString("refresh_token");
                            String accessToken = json.getString("access_token");
                            String tokenType = json.getString("token_type");


                            // store the refresh token in the session
                            session.createTokenSession(accessToken, refreshToken, tokenType);
                            Intent i = new Intent(getActivity(), HomeActivity.class);
                            i.putExtra("flag","no");
                            startActivity(i);
                        }  else {
                            final String stautsMessage = json.getString("message");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(LoginActivity.this, stautsMessage, Toast.LENGTH_SHORT);
                                    Utils.showAlertDialog(getActivity(), Constants.ERROR, stautsMessage, false);
                                }

                                // callLoginNetworkCall();
                            });
                        }
                    } catch (JSONException e) {
                        String errorStatusCode = null;
                        try {
                            errorStatusCode = json.getJSONObject("errors").getJSONArray("email").get(0).toString();
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