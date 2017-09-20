package com.hsdi.theme.handler;

import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;

import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

/**
 * Created by Yu Yong on 2017/8/8.
 */

public abstract class ColorThemeHandler extends ThemeHandler {
    public ColorThemeHandler(View view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        super(view, value, attas);
    }

    public ColorThemeHandler() {
        super();
    }

    protected int getColor(int color_id) {
        int originColor = mContext.get().getResources().getColor(color_id);
        if (mRes == null) {
            return originColor;
        }
        String resName = mContext.get().getResources().getResourceEntryName(color_id);
        int new_color_id = mRes.getIdentifier(resName, "color", mResPkName);
        int new_color = -1;
        try {
            new_color = mRes.getColor(new_color_id);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new_color = originColor;
        }
        return new_color;
    }
}
