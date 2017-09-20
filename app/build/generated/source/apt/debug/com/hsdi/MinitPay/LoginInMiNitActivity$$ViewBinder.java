// Generated code from Butter Knife. Do not modify!
package com.hsdi.MinitPay;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginInMiNitActivity$$ViewBinder<T extends com.hsdi.MinitPay.LoginInMiNitActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689720, "field 'etEmail'");
    target.etEmail = finder.castView(view, 2131689720, "field 'etEmail'");
    view = finder.findRequiredView(source, 2131689721, "field 'etPwd'");
    target.etPwd = finder.castView(view, 2131689721, "field 'etPwd'");
    view = finder.findRequiredView(source, 2131689722, "field 'btnSignIn'");
    target.btnSignIn = finder.castView(view, 2131689722, "field 'btnSignIn'");
  }

  @Override public void unbind(T target) {
    target.etEmail = null;
    target.etPwd = null;
    target.btnSignIn = null;
  }
}
