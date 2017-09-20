package com.hsdi.theme.handler;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

/**
 * Created by Yu Yong on 2017/8/8.
 */

public class BackgroundDrawableThremeHandler extends DrawableThemeHandler {
    public BackgroundDrawableThremeHandler(View view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        super(view, value, attas);
    }

    @TargetApi(16)
    @Override
    public void doHandler() {

        for (int i = 0; i < mAttas.get().getAttributeCount(); i++) {
            Log.i("yuyong_bgd", mAttas.get().getAttributeName(i) + "-->" + mAttas.get().getAttributeValue(i));
        }
        String bg_id = mAttas.get().getAttributeValue("http://schemas.android.com/apk/res/android", "background");
        bg_id = bg_id.split("@")[1];
        mView.get().setBackground(getDrawable(Integer.parseInt(bg_id)));
    }
}
