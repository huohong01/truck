package com.hsdi.theme.handler;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

/**
 * Created by Yu Yong on 2017/8/8.
 */

public class StyleBackgroundDrawableThemeHandler extends ThemeHandler {
    public StyleBackgroundDrawableThemeHandler(View view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        super(view, value, attas);
    }

    @TargetApi(16)
    @Override
    public void doHandler() {
        String drawable = mAttas.get().getAttributeValue("http://schemas.android.com/yuyong", "style_background_drawable");
        drawable = drawable.split("@")[1];
        String[] drawable_info = drawable.split("/");
        int new_drawable_id = -1;
        try {
            new_drawable_id = mRes.getIdentifier(drawable_info[1], drawable_info[0], mResPkName);
        } catch (Exception e) {
            new_drawable_id = -1;
        }
        Drawable new_drawable = null;
        try {
            new_drawable = mRes.getDrawable(new_drawable_id);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new_drawable = null;
        }
        if (new_drawable != null)
            mView.get().setBackground(new_drawable);
    }

}


