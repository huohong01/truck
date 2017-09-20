package com.hsdi.theme.basic;

import android.util.AttributeSet;
import android.view.View;

import com.hsdi.theme.handler.BackgroundColorThemeHandler;
import com.hsdi.theme.handler.BackgroundDrawableThremeHandler;
import com.hsdi.theme.handler.BtnColorStateListThemeHandler;
import com.hsdi.theme.handler.DrawableLeftThemeHandler;
import com.hsdi.theme.handler.MenuItemIconThemeHandler;
import com.hsdi.theme.handler.SrcThemeHandler;
import com.hsdi.theme.handler.StyleBackgroundDrawableThemeHandler;
import com.hsdi.theme.handler.StyleTextColorThemeHandler;
import com.hsdi.theme.handler.StyleThemeHandler;
import com.hsdi.theme.handler.TextColorHintThemeHandler;
import com.hsdi.theme.handler.TextColorThemeHandler;
import com.hsdi.theme.handler.ThemeHandler;
import com.hsdi.theme.handler.ToolbarBackDrawableThemeHandler;
import com.hsdi.theme.handler.ToolbarOverflowDrawableThemeHandler;
import com.hsdi.theme.handler.TxtColorStateListThemeHandler;


/**
 * Created by Yu Yong on 2017/8/8.
 */

public class ThemeHandlerFactory {

    public static final ThemeHandler getHandler(Object view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        if (value == ThemeLayoutInflaterFactory.ThemeTypeValue.menuitem_icon) {
            return new MenuItemIconThemeHandler(view, value, attas);
        }
        if (value == ThemeLayoutInflaterFactory.ThemeTypeValue.style) {
            return new StyleThemeHandler(view, value, attas);
        }
        return null;
    }

    public static final ThemeHandler getHandler(View view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        if (value == ThemeLayoutInflaterFactory.ThemeTypeValue.background_color) {
            return new BackgroundColorThemeHandler(view, value, attas);
        }
        if (value == ThemeLayoutInflaterFactory.ThemeTypeValue.background_drawable) {
            return new BackgroundDrawableThremeHandler(view, value, attas);
        }
        if (value == ThemeLayoutInflaterFactory.ThemeTypeValue.src) {
            return new SrcThemeHandler(view, value, attas);
        }
        if (value == ThemeLayoutInflaterFactory.ThemeTypeValue.drawableLeft) {
            return new DrawableLeftThemeHandler(view, value, attas);
        }
        if (value == ThemeLayoutInflaterFactory.ThemeTypeValue.textColorHint) {
            return new TextColorHintThemeHandler(view, value, attas);
        }
        if (value == ThemeLayoutInflaterFactory.ThemeTypeValue.textColor) {
            return new TextColorThemeHandler(view, value, attas);
        }
        if (value == ThemeLayoutInflaterFactory.ThemeTypeValue.style_background_drawable) {
            return new StyleBackgroundDrawableThemeHandler(view, value, attas);
        }
        if (value == ThemeLayoutInflaterFactory.ThemeTypeValue.style_text_color) {
            return new StyleTextColorThemeHandler(view, value, attas);
        }
        if (value == ThemeLayoutInflaterFactory.ThemeTypeValue.txt_color_state_list) {
            return new TxtColorStateListThemeHandler(view, value, attas);
        }
        if (value == ThemeLayoutInflaterFactory.ThemeTypeValue.btn_color_state_list) {
            return new BtnColorStateListThemeHandler(view, value, attas);
        }
        if (value == ThemeLayoutInflaterFactory.ThemeTypeValue.toolbar_nav_icon) {
            return new ToolbarBackDrawableThemeHandler(view, value, attas);
        }
        if (value == ThemeLayoutInflaterFactory.ThemeTypeValue.toolbar_overflow_icon) {
            return new ToolbarOverflowDrawableThemeHandler(view, value, attas);
        }
        return null;
    }
}
