package com.hsdi.NetMe.ui.startup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.hsdi.NetMe.BaseActivity;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.ui.main.MainActivity;
import com.hsdi.NetMe.ui.startup.change_password.ChangePwdFailedFragment;
import com.hsdi.NetMe.ui.startup.change_password.ChangePwdFragment;
import com.hsdi.NetMe.ui.startup.change_password.ChangePwdSuccessFragment;
import com.hsdi.NetMe.ui.startup.change_password.IChangePassword;

public class ChangePwdActivity extends BaseActivity implements IChangePassword{

    private FragmentManager fragmentManager;
    private int changeId = R.id.change_pwd_holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        fragmentManager = getSupportFragmentManager();
        onChangePwd();
    }

    @Override
    public void onChangeSuccess() {
        fragmentManager.beginTransaction()
                .replace(changeId,new ChangePwdSuccessFragment())
                .commit();

    }

    @Override
    public void onChangeFailed() {
        fragmentManager.beginTransaction()
                .replace(changeId,new ChangePwdFailedFragment())
                .commit();
    }

    @Override
    public void onChangePwd() {
        fragmentManager.beginTransaction()
                .replace(changeId,new ChangePwdFragment())
                .commit();
    }

    @Override
    public void onReturnToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
