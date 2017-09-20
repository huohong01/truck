// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.startup.recovery;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RecoveryMethodFragment$$ViewBinder<T extends com.hsdi.NetMe.ui.startup.recovery.RecoveryMethodFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689889, "field 'emailET'");
    target.emailET = finder.castView(view, 2131689889, "field 'emailET'");
    view = finder.findRequiredView(source, 2131689890, "field 'submitBtn'");
    target.submitBtn = finder.castView(view, 2131689890, "field 'submitBtn'");
  }

  @Override public void unbind(T target) {
    target.emailET = null;
    target.submitBtn = null;
  }
}
