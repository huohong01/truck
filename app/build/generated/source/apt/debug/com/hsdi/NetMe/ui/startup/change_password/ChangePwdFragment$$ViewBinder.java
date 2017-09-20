// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.startup.change_password;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChangePwdFragment$$ViewBinder<T extends com.hsdi.NetMe.ui.startup.change_password.ChangePwdFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689852, "field 'etOldPassword'");
    target.etOldPassword = finder.castView(view, 2131689852, "field 'etOldPassword'");
    view = finder.findRequiredView(source, 2131689853, "field 'etNewPassword'");
    target.etNewPassword = finder.castView(view, 2131689853, "field 'etNewPassword'");
    view = finder.findRequiredView(source, 2131689854, "field 'etConfirmPassword'");
    target.etConfirmPassword = finder.castView(view, 2131689854, "field 'etConfirmPassword'");
    view = finder.findRequiredView(source, 2131689855, "field 'btnChangePwdSubmit'");
    target.btnChangePwdSubmit = finder.castView(view, 2131689855, "field 'btnChangePwdSubmit'");
  }

  @Override public void unbind(T target) {
    target.etOldPassword = null;
    target.etNewPassword = null;
    target.etConfirmPassword = null;
    target.btnChangePwdSubmit = null;
  }
}
