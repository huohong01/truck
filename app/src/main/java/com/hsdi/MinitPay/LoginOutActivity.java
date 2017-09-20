package com.hsdi.MinitPay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.hsdi.MinitPay.util.SharePrefManager;
import com.hsdi.NetMe.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginOutActivity extends AppCompatActivity {

    @Bind(R.id.btn_yes)
    Button btnYes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_out);
        ButterKnife.bind(this);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新登录状态，并跳转到主页面
                setOutLogined();
                Intent intent = new Intent(LoginOutActivity.this, MinitPayActivity.class);
                startActivity(intent);
            }
        });
    }
    ////when login out successfully,set is_login = false;
    public void setOutLogined() {
        SharedPreferences preferences = getSharedPreferences(SharePrefManager.NETME_MINITPAY_PREF_NAME, MODE_MULTI_PROCESS);
        boolean isLogined = preferences.getBoolean("is_login", false);
        if (isLogined) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("is_login", false);
            edit.commit();
        }
    }

}
