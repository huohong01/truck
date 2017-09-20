package com.macate.minitpay.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.macate.minitpay.R;
import com.macate.minitpay.activities.ShoppingCartActivity;
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
 * Created by neerajakshi.daggubat on 9/9/2015.
 */
public class LockScreenDialog extends DialogFragment {
    private View view;
    private TextView tvClose, tvOk;
    private SessionManager session;
    private EditText etLock;
    public LockScreenDialog() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
        session = new SessionManager(getActivity());
    }

    public static LockScreenDialog newInstance(String message) {
        LockScreenDialog newMessageDialog = new LockScreenDialog();
        Bundle args = new Bundle();
        args.putString("message", message);
        newMessageDialog.setArguments(args);
        return newMessageDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new SessionManager(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.show();
        // Set title divider color
        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        view = inflater.inflate(R.layout.lock_screen, container);
        etLock = (EditText) view.findViewById(R.id.etLock);
        tvClose = (TextView) view.findViewById(R.id.tvClose);
        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createToken();
            }
        });

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details, menu);
    }

    public void createToken(){
        if (!Utils.isNetworkAvailable(getActivity()) || !Utils.isOnline()) {
            Utils.showInternetAlertDialog(getActivity(), Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
        } else {
            String refreshTokenUrl = Constants.BASE_URL + Constants.ACCESS_TOKEN;

            Log.e("refreshTokenUrl", "refreshTokenUrl ==========================================   *********************      " + refreshTokenUrl);

            // should be a singleton
            OkHttpClient refreshTokenClient = new OkHttpClient();

            Log.e("","session.getKeyEmail()"+session.getKeyEmail());
            RequestBody refreshTokenBody = new FormBody.Builder()
                    .add("grant_type", Constants.APP_PASSWORD_GRANT_TPYE)
                    .add("client_id", Constants.APP_CLIENT_ID)
                    .add("client_secret", Constants.APP_CLIENT_SECRET)
                    .add("username", session.getKeyEmail())
                    .add("password", etLock.getText().toString())
                    .build();
            Request refreshTokenRequest = new Request.Builder()
                    .header("Accept", Constants.ACCESS_TOKEN_ACCEPT)
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

                            Intent i = new Intent(getActivity(), ShoppingCartActivity.class);
                            i.putExtra("type","shopping");
                            startActivity(i);
                            //finish();
                            //lookForDevice();
                        }  else {
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
