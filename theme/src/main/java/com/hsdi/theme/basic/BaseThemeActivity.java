package com.hsdi.theme.basic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hsdi.theme.handler.StatusBarThemeHandler;
import com.hsdi.theme.handler.ThemeHandler;

import java.util.HashMap;

public class BaseThemeActivity extends AppCompatActivity {

    private ThemeLayoutInflaterFactory mThemeFactory;

    public void applyTheme(View v, int id) {
        applyTheme(v, id, null);
    }

    public void applyTheme(Object v, int id, ThemeLayoutInflaterFactory.ThemeTypeValue type) {
        ThemeHandler handler = ThemeHandlerFactory.getHandler(v, type, null);
        if (handler != null)
            handler.init(this);
        if (handler == null)
            return;
        handler.doHandler(id);
    }

    public void applyTheme(View v, int id, ThemeLayoutInflaterFactory.ThemeTypeValue type) {
        ThemeHandler handler = null;
        HashMap map = mThemeFactory.getHandlers();
        if (map != null)
            handler = (ThemeHandler) map.get(v);
        if (handler == null) {
            handler = ThemeHandlerFactory.getHandler(v, type, null);
            if (handler != null)
                handler.init(this);
        }
        if (handler == null)
            return;
        handler.doHandler(id);
    }

    public int applyThemeStatusBar(int id) {
        StatusBarThemeHandler handler = new StatusBarThemeHandler();
        try {
            handler.init(this);
            return handler.getColor(id);
        } catch (Exception e) {
            return getResources().getColor(id);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mThemeFactory = new ThemeLayoutInflaterFactory();
        getLayoutInflater().setFactory(mThemeFactory);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        mThemeFactory.onDestroy();
        super.onDestroy();
    }
}
