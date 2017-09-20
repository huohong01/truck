package com.hsdi.theme.basic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hsdi.theme.handler.ThemeHandler;

import java.util.HashMap;

/**
 * Created by huohong.yi on 2017/8/31.
 */

public class BaseThemeFragment extends Fragment {

    private ThemeLayoutInflaterFactory mThemeFactory;

    public void applyTheme(View v, int id) {
        applyTheme(v, id, null);
    }

    public void applyTheme(View v, int id, ThemeLayoutInflaterFactory.ThemeTypeValue type) {
        ThemeHandler handler = null;
        HashMap map = mThemeFactory.getHandlers();
        if (map != null)
            handler = (ThemeHandler) map.get(v);
        if (handler == null) {
            handler = ThemeHandlerFactory.getHandler(v, type, null);
            if (handler != null)
                handler.init(getActivity());
        }
        if (handler == null)
            return;
        handler.doHandler(id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mThemeFactory = new ThemeLayoutInflaterFactory();
        inflater.setFactory(mThemeFactory);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        mThemeFactory.onDestroy();
        super.onDestroy();
    }

}
