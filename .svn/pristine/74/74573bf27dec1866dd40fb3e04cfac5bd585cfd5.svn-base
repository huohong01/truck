package com.hsdi.theme.handler;

import android.annotation.TargetApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;

import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

/**
 * Created by Yu Yong on 2017/8/18.
 */

public class BtnColorStateListThemeHandler extends DrawableThemeHandler {
    public BtnColorStateListThemeHandler(View view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        super(view, value, attas);
    }

    @TargetApi(23)
    @Override
    public void doHandler() {
        String btn_color_id = mAttas.get().getAttributeValue("http://schemas.android.com/apk/res/android", "button");
        btn_color_id = btn_color_id.split("@")[1];
        if (mView.get() instanceof CheckBox) {
            ((CheckBox) mView.get()).setButtonDrawable(getDrawable(Integer.parseInt(btn_color_id)));
        }
    }


}
