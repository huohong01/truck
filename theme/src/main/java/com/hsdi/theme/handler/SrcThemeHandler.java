package com.hsdi.theme.handler;

import android.annotation.TargetApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

/**
 * Created by Yu Yong on 2017/8/8.
 */

public class SrcThemeHandler extends DrawableThemeHandler {
    public SrcThemeHandler(View view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        super(view, value, attas);
    }

    @Override
    @TargetApi(16)
    public void doHandler() {
        String bg_id = mAttas.get().getAttributeValue("http://schemas.android.com/apk/res/android", "src");
        if (bg_id == null){
            return;
        }
        bg_id = bg_id.split("@")[1];
        doHandler(Integer.parseInt(bg_id));
    }

    @TargetApi(16)
    @Override
    public void doHandler(int id) {
        if (mView.get() instanceof ImageView)
            ((ImageView) mView.get()).setImageDrawable(getDrawable(id));
        else
            mView.get().setBackground(getDrawable(id));
    }
}
