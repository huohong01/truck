// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.startup.recovery;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RecoveryFailedFragment$$ViewBinder<T extends com.hsdi.NetMe.ui.startup.recovery.RecoveryFailedFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689884, "field 'retryBtn'");
    target.retryBtn = finder.castView(view, 2131689884, "field 'retryBtn'");
    view = finder.findRequiredView(source, 2131689886, "field 'signUpBtn'");
    target.signUpBtn = finder.castView(view, 2131689886, "field 'signUpBtn'");
  }

  @Override public void unbind(T target) {
    target.retryBtn = null;
    target.signUpBtn = null;
  }
}
