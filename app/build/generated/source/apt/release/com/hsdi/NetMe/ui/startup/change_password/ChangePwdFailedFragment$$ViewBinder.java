// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.startup.change_password;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChangePwdFailedFragment$$ViewBinder<T extends com.hsdi.NetMe.ui.startup.change_password.ChangePwdFailedFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689856, "field 'changeFailedRetry'");
    target.changeFailedRetry = finder.castView(view, 2131689856, "field 'changeFailedRetry'");
    view = finder.findRequiredView(source, 2131689857, "field 'changeFailedSignIn'");
    target.changeFailedSignIn = finder.castView(view, 2131689857, "field 'changeFailedSignIn'");
  }

  @Override public void unbind(T target) {
    target.changeFailedRetry = null;
    target.changeFailedSignIn = null;
  }
}
