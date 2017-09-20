package com.hsdi.theme.handler;

import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

/**
 * Created by Yu Yong on 2017/8/8.
 */

public class TextColorHintThemeHandler extends ColorThemeHandler {
    public TextColorHintThemeHandler(View view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        super(view, value, attas);
    }

    @Override
    public void doHandler() {
        String txt_color_id = mAttas.get().getAttributeValue("http://schemas.android.com/apk/res/android", "textColorHint");
        txt_color_id = txt_color_id.split("@")[1];
        int color = getColor(Integer.parseInt(txt_color_id));
        if (mView.get() instanceof EditText) {
            ((EditText) mView.get()).setHintTextColor(color);
        } else {
            mView.get().setBackgroundColor(color);
        }
    }
}
