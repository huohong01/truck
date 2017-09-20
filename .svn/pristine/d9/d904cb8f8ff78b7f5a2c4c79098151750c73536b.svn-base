package com.hsdi.theme.handler;

import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

/**
 * Created by huohong.yi on 2017/8/29.
 */

public class ToolbarOverflowDrawableThemeHandler extends DrawableThemeHandler{

    public ToolbarOverflowDrawableThemeHandler(View view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        super(view, value, attas);
    }

    @Override
    public void doHandler() {

    }

    @Override
    public void doHandler(int id) {
        ((Toolbar) mView.get()).setOverflowIcon(getDrawable(id));
    }
}
