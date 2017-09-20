// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.startup;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginActivity$$ViewBinder<T extends com.hsdi.NetMe.ui.startup.LoginActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689709, "field 'usernameTV'");
    target.usernameTV = finder.castView(view, 2131689709, "field 'usernameTV'");
    view = finder.findRequiredView(source, 2131689713, "field 'passwordET'");
    target.passwordET = finder.castView(view, 2131689713, "field 'passwordET'");
    view = finder.findRequiredView(source, 2131689715, "field 'rememberMe'");
    target.rememberMe = finder.castView(view, 2131689715, "field 'rememberMe'");
    view = finder.findRequiredView(source, 2131689710, "field 'loginClear'");
    target.loginClear = finder.castView(view, 2131689710, "field 'loginClear'");
  }

  @Override public void unbind(T target) {
    target.usernameTV = null;
    target.passwordET = null;
    target.rememberMe = null;
    target.loginClear = null;
  }
}
