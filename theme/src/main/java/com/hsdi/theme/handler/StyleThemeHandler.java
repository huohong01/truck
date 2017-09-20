package com.hsdi.theme.handler;

import android.content.res.Resources;
import android.util.AttributeSet;

import com.hsdi.theme.basic.StyleManager;
import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

/**
 * Created by huohong.yi on 2017/8/30.
 */

public class StyleThemeHandler extends ThemeHandler {
    public StyleThemeHandler(Object view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        super(view, value, attas);
    }

    @Override
    public void doHandler() {

    }
    @Override
    public void doHandler(int id) {
        String resName = mContext.get().getResources().getResourceEntryName(id);
        int new_drawable_id = -1;
        try {
            new_drawable_id = mRes.getIdentifier(resName, "style", mResPkName);
        } catch (Exception e) {
            new_drawable_id = -1;
        }
        ((Resources.Theme[])oView.get())[0]=StyleManager.getTheme(mContext.get(),mRes,new_drawable_id);
    }
}
