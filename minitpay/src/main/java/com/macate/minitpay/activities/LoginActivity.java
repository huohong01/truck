package com.macate.minitpay.activities;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.macate.minitpay.R;
import com.macate.minitpay.adapters.ViewPagerAdapter;
import com.macate.minitpay.helpers.Constants;
import com.macate.minitpay.helpers.SessionManager;
import org.json.JSONObject;
public class LoginActivity extends AppCompatActivity {
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.e("LoginActivity", "inside on create of LoginActivity");
        // get the viewpager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Log.e("LoginActivity", "inside on create of LoginActivity 22222222222222 ");
        String[] tabTitles = {Constants.SIGNIN_LABEL, Constants.SIGNUP_LABEL};
        Log.e("LoginActivity", "on create of loginActivity   " + tabTitles[0]);
        //set the viewpager adapter for the pager
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), tabTitles, 0));

        Log.e("LoginActivity", "on create of loginActivity");
        // find the string tabstrip
        Log.e("LoginActivity", "on create of loginActivity");
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the tabstrip to the view pager
        Log.e("LoginActivity", "on create of loginActivity");
        tabStrip.setViewPager(viewPager);
    }
}
