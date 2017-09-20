package com.hsdi.NetMe.ui.startup.phone_verification;

import android.support.v4.app.Fragment;

/**
 * Created by huohong.yi on 2017/6/7.
 */

public abstract class BackHandledFragment extends Fragment implements FragmentBackHandler{

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }
}
