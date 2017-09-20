// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.startup.change_password;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChangePwdSuccessFragment$$ViewBinder<T extends com.hsdi.NetMe.ui.startup.change_password.ChangePwdSuccessFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689858, "field 'changePwdSuccessCheck'");
    target.changePwdSuccessCheck = finder.castView(view, 2131689858, "field 'changePwdSuccessCheck'");
    view = finder.findRequiredView(source, 2131689859, "field 'changeSuccessMessage'");
    target.changeSuccessMessage = finder.castView(view, 2131689859, "field 'changeSuccessMessage'");
  }

  @Override public void unbind(T target) {
    target.changePwdSuccessCheck = null;
    target.changeSuccessMessage = null;
  }
}
