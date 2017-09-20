package com.hsdi.theme.handler;

import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

/**
 * Created by Yu Yong on 2017/8/8.
 */

public class StyleTextColorThemeHandler extends ThemeHandler {
    public StyleTextColorThemeHandler(View view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        super(view, value, attas);
    }

    @Override
    public void doHandler() {
        String color = mAttas.get().getAttributeValue("http://schemas.android.com/yuyong", "style_text_color");
        color = color.split("@")[1];
        String[] color_info = color.split("/");
        int new_color_id = mRes.getIdentifier(color_info[1], color_info[0], mResPkName);
        int new_color = -1;
        try {
            new_color = mRes.getColor(new_color_id);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new_color = -1;
        }
        if (new_color != -1) {
            if (mView.get() instanceof TextView) {
                ((TextView) mView.get()).setTextColor(new_color);
            }
        }

    }
}
