package com.hsdi.theme.handler;

import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

/**
 * anthor : YIDS
 * date :  2017/8/29
 */

public class ToolbarBackDrawableThemeHandler extends DrawableThemeHandler {
    public ToolbarBackDrawableThemeHandler(View view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        super(view, value, attas);
    }

    @Override
    public void doHandler() {

    }

    @Override
    public void doHandler(int id) {
        ((Toolbar) mView.get()).setNavigationIcon(getDrawable(id));
    }
}
