package com.macate.minitpay.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.macate.minitpay.R;
import com.macate.minitpay.adapters.PaymentSourceAdapter;
import com.macate.minitpay.dialogs.ProgressDialogFragment;
import com.macate.minitpay.helpers.Constants;
import com.macate.minitpay.helpers.SessionManager;
import com.macate.minitpay.helpers.Utils;
import com.macate.minitpay.models.PaymentSources;

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

public class TopupActivity extends AppCompatActivity {
    ListView lvSelectedPaymentCard;
    JSONObject json;
    SessionManager session;
    private PaymentSources paymentSource;
    Button btnSubmit;
    boolean isAddNewPaymentPressed = false;
    EditText etAddBalanceAmount;
    TextView tvCardNo, tvAddNewPaymentMethod;
    public static ProgressDialogFragment progressFragment;
    String errorMessage;
    String sim1, deviceId, phoneNo,  simWithoutLastDigit ;
    Toolbar mToolbar;
    Spinner spPaymentSources;
    ImageButton ibLogout;
    private ArrayList<PaymentSources> alPayment;
    private PaymentSourceAdapter adPayment;
    String[] arrayErrors;
    String parentActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_data);

        session = new SessionManager(TopupActivity.this);

        parentActivity = getIntent().getStringExtra("parentActivity");
        // setup Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TopupActivity.this.setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        progressFragment = ProgressDialogFragment.newInstance(R.string.progressMessage);

        paymentSource = getIntent().getParcelableExtra("item");
        etAddBalanceAmount = (EditText) findViewById(R.id.etAddBalanceAmount);
        spPaymentSources = (Spinner) findViewById(R.id.spPaymentSources);


        alPayment = new ArrayList<PaymentSources>();
        adPayment = new PaymentSourceAdapter(TopupActivity.this, alPayment);
        addPaymentSources();

        tvAddNewPaymentMethod = (TextView) findViewById(R.id.tvAddNewPaymentMethod);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        btnSubmit.setTypeface(typeFace);
        etAddBalanceAmount.setTypeface(typeFace);
        tvAddNewPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TopupActivity.this, AddCardActivity.class);
                i.putExtra("parent","TopupActivity");
                i.putExtra("superParent", parentActivity);
                startActivity(i);
            }
        });


        spPaymentSources.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentSource = alPayment.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Utils.showAlertDialog(TopupActivity.this, Constants.payment_source_error, Constants.payment_source_error_message, false);
                return;
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();
                paymentSource = (PaymentSources) spPaymentSources.getSelectedItem();
                if (etAddBalanceAmount.getText().toString().equalsIgnoreCase("")) {
                    Utils.showAlertDialog(TopupActivity.this, Constants.INPUT_ERROR, Constants.BUY_DATA_ENTER_AMOUNT_ERROR, false);
                    finishProgress();
                }else if(Float.parseFloat(etAddBalanceAmount.getText().toString())< 0.5) {
                    Utils.showAlertDialog(TopupActivity.this, Constants.INPUT_ERROR, Constants.BUY_DATA_MIN_AMOUNT_ERROR, false);
                    finishProgress();
                }else
                if (spPaymentSources.getSelectedItemPosition()<0) {
                    Utils.showAlertDialog(TopupActivity.this, Constants.INPUT_ERROR, Constants.SELECT_PAYMENT_METHOD_ERROR, false);
                    finishProgress();
                }else if (!Utils.isNetworkAvailable(TopupActivity.this) || !Utils.isOnline()) {
                    Utils.showInternetAlertDialog(TopupActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                    finishProgress();
                } else {
                    String registerUrl = Constants.BASE_URL + Constants.CHARGES_URL;

                    Log.e ("registerUrl","registerUrl============================================= "+registerUrl);
                    // should be a singleton
                    OkHttpClient register = new OkHttpClient();

                        RequestBody registerBody = new FormBody.Builder()
                                .add("amount", etAddBalanceAmount.getText().toString())
                                .add("source_id", paymentSource.getPaymentSourceId())
                            .build();

                    Log.e ("registerBody","registerBody============================================= "+registerBody.toString());
                    Request registerToken = new Request.Builder()
                            .header("Accept", Constants.ACCESS_TOKEN_ACCEPT)
                            .header("Authorization", session.getTokenType() + " " + session.getAccessToken())
                            .url(registerUrl)
                            .post(registerBody)
                            .build();

                    register.newCall(registerToken).enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            try {
                                int statusCode = response.code();

                                String responseData = response.body().string();
                                JSONObject json = new JSONObject(responseData);
                                if (statusCode == 200) {
                                    if(parentActivity.equalsIgnoreCase("HomeActivity")) {
                                        Intent i = new Intent(TopupActivity.this, HomeActivity.class);
                                        i.putExtra("flag","no");
                                        startActivity(i);
                                        finishProgress();
                                    } /*else  if(parentActivity.equalsIgnoreCase("DeviceDetailsActivity")) {
                                        Intent i = new Intent(TopupActivity.this, DeviceDetailsActivity.class);
                                        startActivity(i);
                                        finishProgress();
                                    }*/
                                } else if (statusCode == 422) {
                                    JSONObject errors = json.getJSONObject("errors");

                                    Iterator keys = errors.keys();

                                    // loop to get the dynamic key
                                    String currentDynamicKey = (String) keys.next();
                                    // get the value of the dynamic key
                                    String currentDynamicValue = errors.getString(currentDynamicKey);
                                    errorMessage =  currentDynamicValue;

                                    TopupActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Toast.makeText(LoginActivity.this, stautsMessage, Toast.LENGTH_SHORT);
                                            finishProgress();
                                            arrayErrors = errorMessage.split(",");
                                            Log.e("arrayErrors", "arrayErrors ================================= " + arrayErrors[0]);
                                            Utils.showAlertDialog(TopupActivity.this, Constants.ERROR, arrayErrors[0].substring(2, arrayErrors[0].length() - 2), false);
                                        }
                                    });

                                }else if(statusCode == 401) {
                                    if (!Utils.isNetworkAvailable(TopupActivity.this) || !Utils.isOnline()) {
                                        Utils.showInternetAlertDialog(TopupActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                                    } else {
                                        Utils.generateAccessTokenBasedOnRefreshToken(TopupActivity.this);
                                    }
                                }  else {
                                    final String message = json.getString("message");
                                    TopupActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Utils.showAlertDialog(TopupActivity.this, Constants.ERROR, message, false);
                                            finishProgress();
                                        }
                                    });

                                }


                            } catch (JSONException e) {
                                    finishProgress();

                                    //json.getJSONObject("errors").getJSONArray("email").get(0).toString();
                            }
                            finishProgress();
                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

            }
        });
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
        if (!Utils.isNetworkAvailable(TopupActivity.this) || !Utils.isOnline()) {
            Utils.showInternetAlertDialog(TopupActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
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
                            TopupActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        alPayment.clear();
                                        alPayment = PaymentSources.parseJSON(json.getJSONArray("data"));
                                        if(alPayment.size()>0) {
                                            adPayment.clear();
                                            adPayment.addAll(alPayment);

                                        } else {
                                            spPaymentSources.setVisibility(View.GONE);
                                        }
                                        adPayment.setDropDownViewResource(R.layout.list_payment_source);
                                        spPaymentSources.setAdapter(adPayment);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if(statusCode == 401) {
                            if (!Utils.isNetworkAvailable(TopupActivity.this) || !Utils.isOnline()) {
                                Utils.showInternetAlertDialog(TopupActivity.this, Constants.NETWORK_CONNECTION_ERROR, Constants.CHECK_NETWORK_CONNECTIONS, false);
                            } else {
                                Utils.generateAccessTokenBasedOnRefreshToken(TopupActivity.this);
                            }
                        } else {
                            final String errorString = json.getString("message");
                            TopupActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.showAlertDialog(TopupActivity.this, Constants.ERROR, errorString, false);
                                    finishProgress();
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        if(parentActivity.equalsIgnoreCase("HomeActivity")) {
            Intent intent = new Intent(TopupActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("flag","no");
            startActivity(intent);
            finish(); // call this to finish the current activity
        } /*else  if(parentActivity.equalsIgnoreCase("DeviceDetailsActivity")) {
            Intent intent = new Intent(TopupActivity.this, DeviceDetailsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // call this to finish the current activity
        }*/
    }

    public void startProgress() {
        progressFragment.show(getSupportFragmentManager(), "progress");
    }

    public void finishProgress() {
        progressFragment.dismiss();
    }
}
