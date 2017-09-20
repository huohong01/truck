package com.hsdi.theme.handler;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

/**
 * Created by Yu Yong on 2017/8/18.
 */

public class TxtColorStateListThemeHandler extends ThemeHandler {
    public TxtColorStateListThemeHandler(View view, ThemeLayoutInflaterFactory.ThemeTypeValue value, AttributeSet attas) {
        super(view, value, attas);
    }

    @TargetApi(23)
    @Override
    public void doHandler() {

        for (int i = 0; i < mAttas.get().getAttributeCount(); i++) {
            Log.i("yuyong_bgd_tc", mAttas.get().getAttributeName(i) + "-->" + mAttas.get().getAttributeValue(i));
        }
        String txt_color_id = mAttas.get().getAttributeValue("http://schemas.android.com/apk/res/android", "textColor");
        txt_color_id = txt_color_id.split("@")[1];
        if (mView.get() instanceof TextView) {
            ColorStateList list = getColorStateList(Integer.parseInt(txt_color_id));
            ((TextView) mView.get()).setTextColor(list);
        }
    }

    private ColorStateList getColorStateList(int id) {
        ColorStateList originColorStateList = mContext.get().getResources().getColorStateList(id);
        if (mRes == null) {
            return originColorStateList;
        }
        String resName = mContext.get().getResources().getResourceEntryName(id);
        int newColorStateListId = -1;
        if (newColorStateListId <= 0)
            newColorStateListId = mRes.getIdentifier(resName, "drawable", mResPkName);
        ColorStateList newColorStateList = null;
        try {
            newColorStateList = mRes.getColorStateList(newColorStateListId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            newColorStateList = originColorStateList;
        }
        return newColorStateList;
    }
}
