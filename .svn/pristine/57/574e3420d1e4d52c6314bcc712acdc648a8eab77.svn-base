package com.hsdi.theme.handler;

import android.util.AttributeSet;
import android.view.View;

import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

/**
 * Created by Yu Yong on 2017/8/8.
 */

public class BackgroundColorThemeHandler extends ColorThemeHandler {
    public BackgroundColorThemeHandler(View view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        super(view, value, attas);
    }

    @Override
    public void doHandler() {
        String bg_id = mAttas.get().getAttributeValue("http://schemas.android.com/apk/res/android", "background");
        bg_id = bg_id.split("@")[1];
        int color = getColor(Integer.parseInt(bg_id));
        mView.get().setBackgroundColor(color);
    }

    @Override
    public void doHandler(int id) {
        mView.get().setBackgroundColor(getColor(id));
    }
}
