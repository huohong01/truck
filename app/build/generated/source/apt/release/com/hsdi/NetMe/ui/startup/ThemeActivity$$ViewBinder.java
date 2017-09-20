// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.startup;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ThemeActivity$$ViewBinder<T extends com.hsdi.NetMe.ui.startup.ThemeActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689805, "field 'themeToolbar'");
    target.themeToolbar = finder.castView(view, 2131689805, "field 'themeToolbar'");
    view = finder.findRequiredView(source, 2131689806, "field 'themeGridview'");
    target.themeGridview = finder.castView(view, 2131689806, "field 'themeGridview'");
  }

  @Override public void unbind(T target) {
    target.themeToolbar = null;
    target.themeGridview = null;
  }
}
