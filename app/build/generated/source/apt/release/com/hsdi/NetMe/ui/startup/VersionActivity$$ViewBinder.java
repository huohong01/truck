// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.startup;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VersionActivity$$ViewBinder<T extends com.hsdi.NetMe.ui.startup.VersionActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689808, "field 'versionToolbar'");
    target.versionToolbar = finder.castView(view, 2131689808, "field 'versionToolbar'");
    view = finder.findRequiredView(source, 2131689810, "field 'versionCode'");
    target.versionCode = finder.castView(view, 2131689810, "field 'versionCode'");
  }

  @Override public void unbind(T target) {
    target.versionToolbar = null;
    target.versionCode = null;
  }
}
