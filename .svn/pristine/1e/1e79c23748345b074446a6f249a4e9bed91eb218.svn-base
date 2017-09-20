package com.hsdi.MinitPay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hsdi.NetMe.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BalanceActivity extends AppCompatActivity {

    @Bind(R.id.tv_account)
    TextView tvAccount;
    @Bind(R.id.tv_balance)
    TextView tvBalance;
    @Bind(R.id.btn_sign_in)
    Button btnSignIn;

    private String userId;
    private String email;
    private String balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        ButterKnife.bind(this);

        tvAccount = (TextView) findViewById(R.id.tv_account);
        tvBalance = (TextView) findViewById(R.id.tv_balance);

        Intent intent = getIntent();
        balance = intent.getStringExtra("balance");
        userId = intent.getStringExtra("userId");
        email = intent.getStringExtra("email");
        Log.e("yi", "onCreate:=================================== userId = " + userId + ",email = " + email + ",balance = " + balance);
        tvAccount.setText("account: " + userId);
        tvBalance.setText("balance: " + balance);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BalanceActivity.this, MinitPayActivity.class);
                startActivity(intent);
            }
        });


    }


}
