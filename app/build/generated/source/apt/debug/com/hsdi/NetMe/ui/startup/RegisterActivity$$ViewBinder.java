// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.startup;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RegisterActivity$$ViewBinder<T extends com.hsdi.NetMe.ui.startup.RegisterActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689759, "field 'usernameTV'");
    target.usernameTV = finder.castView(view, 2131689759, "field 'usernameTV'");
    view = finder.findRequiredView(source, 2131689771, "field 'passET'");
    target.passET = finder.castView(view, 2131689771, "field 'passET'");
    view = finder.findRequiredView(source, 2131689774, "field 'passConfET'");
    target.passConfET = finder.castView(view, 2131689774, "field 'passConfET'");
    view = finder.findRequiredView(source, 2131689768, "field 'emailET'");
    target.emailET = finder.castView(view, 2131689768, "field 'emailET'");
    view = finder.findRequiredView(source, 2131689762, "field 'fNameET'");
    target.fNameET = finder.castView(view, 2131689762, "field 'fNameET'");
    view = finder.findRequiredView(source, 2131689765, "field 'lNameET'");
    target.lNameET = finder.castView(view, 2131689765, "field 'lNameET'");
  }

  @Override public void unbind(T target) {
    target.usernameTV = null;
    target.passET = null;
    target.passConfET = null;
    target.emailET = null;
    target.fNameET = null;
    target.lNameET = null;
  }
}
