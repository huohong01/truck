// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.chat.text.helper_activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ViewLocationActivity$$ViewBinder<T extends com.hsdi.NetMe.ui.chat.text.helper_activities.ViewLocationActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689811, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131689811, "field 'toolbar'");
  }

  @Override public void unbind(T target) {
    target.toolbar = null;
  }
}
