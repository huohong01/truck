package com.macate.minitpay.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.util.Log;
import android.widget.Toast;

import com.macate.minitpay.fragments.LoginFragment;
import com.macate.minitpay.fragments.RegisterFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[];
    private int flag;

    // Adapter gets teh manager insert or remove fragment from activity
    public ViewPagerAdapter(FragmentManager fm, String tabTitles[], int flag) {
        super(fm);
        Log.e("", "inside pager adapter flag ---------------------- "+flag);
        this.tabTitles = tabTitles;
        this.flag = flag;


    }
    // The order and creation of fragments within the pager
    @Override
    public Fragment getItem(int position) {

        Log.e("ount","getItem  ================= "+position + "    " +flag);
        if (flag == 0) { // details activity
            if (position == 0) {
                return new LoginFragment();
            } else if (position == 1) {
                return new RegisterFragment();
            } else
                return null;
        } /*else if (flag == 1) { // sub-details activity
            if (position == 0) {
                return new SubDetailsDataFragment();
               *//* } else if (position == 1) {
                    return new SubDetailsMinutesFragment();*//*

            } else
                return null;

        } else if (flag == 2) { // sub-details activity
            if (position == 0) {
                return new OffersFragment();
            } else if (position == 1) {
                return new WishlistFragment();
            } *//*else if(position == 2) {
                    return new NegotiateFragment();
                }*//*
            else

                return null;

        }else if (flag == 3) { // data tranfer activity
            if (position == 0) {
                return new SendDataFragment();
            } else if (position == 1) {
                return new RequestDataFragment();
            } else if (position == 2) {
                return new DataTransferHistoryFragment();
            } else
                return null;

        }*/else {
            return null;
        }
    }


    // Return the no of fragments to swipe
    @Override
    public int getCount() {
        Log.e("ount","count ================= "+tabTitles.length);
        return tabTitles.length;
    }
    // Return the page title
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}

