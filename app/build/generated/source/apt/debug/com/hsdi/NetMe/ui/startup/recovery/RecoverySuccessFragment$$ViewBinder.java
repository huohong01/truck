// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.startup.recovery;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RecoverySuccessFragment$$ViewBinder<T extends com.hsdi.NetMe.ui.startup.recovery.RecoverySuccessFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689893, "field 'checkView'");
    target.checkView = view;
    view = finder.findRequiredView(source, 2131689894, "field 'bottomMessageView'");
    target.bottomMessageView = view;
  }

  @Override public void unbind(T target) {
    target.checkView = null;
    target.bottomMessageView = null;
  }
}
