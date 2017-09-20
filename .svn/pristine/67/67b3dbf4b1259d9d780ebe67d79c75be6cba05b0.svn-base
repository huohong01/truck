    package com.hsdi.NetMe.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hsdi.NetMe.ui.main.contacts.ContactFragment;
import com.hsdi.NetMe.ui.main.favorites.FavoritesFragment;
import com.hsdi.NetMe.ui.main.recent_logs.RecentFragment;
import com.hsdi.NetMe.ui.main.message_logs.MessageLogFragment;

class MainAdapter extends FragmentStatePagerAdapter {

    MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MessageLogFragment();
            case 1:
                return new RecentFragment();
            case 2:
                return new ContactFragment();
            case 3:
                return new FavoritesFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
