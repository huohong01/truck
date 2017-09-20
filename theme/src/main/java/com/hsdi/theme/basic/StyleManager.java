package com.hsdi.theme.basic;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by huohong.yi on 2017/8/30.
 */

public class StyleManager {
    public static Resources.Theme getTheme(Context ctx,Resources resources, int style_id) {
        Resources.Theme theme = resources.newTheme();
        //theme.obtainStyledAttributes()
        Resources.Theme _theme = ctx.getTheme();
        theme.setTo(_theme);
        theme.applyStyle(style_id, true);
        return theme;
    }

}
