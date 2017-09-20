package com.hsdi.theme.handler;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

/**
 * Created by Yu Yong on 2017/8/8.
 */

public class DrawableLeftThemeHandler extends DrawableThemeHandler {

    public DrawableLeftThemeHandler(View view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        super(view, value, attas);
    }

    @Override
    @TargetApi(16)
    public void doHandler() {
        String bg_id = mAttas.get().getAttributeValue("http://schemas.android.com/apk/res/android", "drawableLeft");
        bg_id = bg_id.split("@")[1];
        if (mView.get() instanceof EditText) {
            Drawable tmp = getDrawable(Integer.parseInt(bg_id));
            tmp.setBounds(0, 0, tmp.getIntrinsicWidth(), tmp.getMinimumHeight());
            ((EditText) mView.get()).setCompoundDrawables(tmp, null, null, null);
        } else if (mView.get() instanceof TextView) {
            Drawable tmp = getDrawable(Integer.parseInt(bg_id));
            tmp.setBounds(0, 0, tmp.getIntrinsicWidth(), tmp.getMinimumHeight());
            ((TextView) mView.get()).setCompoundDrawablesWithIntrinsicBounds(tmp, null, null, null);
        }
    }
}
