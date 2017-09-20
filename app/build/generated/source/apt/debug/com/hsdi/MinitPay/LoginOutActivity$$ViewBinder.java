// Generated code from Butter Knife. Do not modify!
package com.hsdi.MinitPay;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginOutActivity$$ViewBinder<T extends com.hsdi.MinitPay.LoginOutActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689724, "field 'btnYes'");
    target.btnYes = finder.castView(view, 2131689724, "field 'btnYes'");
  }

  @Override public void unbind(T target) {
    target.btnYes = null;
  }
}
