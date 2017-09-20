// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.chat.text.helper_activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ImageViewActivity$$ViewBinder<T extends com.hsdi.NetMe.ui.chat.text.helper_activities.ImageViewActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689704, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131689704, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131689705, "field 'imageLayout'");
    target.imageLayout = finder.castView(view, 2131689705, "field 'imageLayout'");
  }

  @Override public void unbind(T target) {
    target.toolbar = null;
    target.imageLayout = null;
  }
}
