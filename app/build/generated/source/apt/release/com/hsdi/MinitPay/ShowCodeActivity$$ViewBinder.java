// Generated code from Butter Knife. Do not modify!
package com.hsdi.MinitPay;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ShowCodeActivity$$ViewBinder<T extends com.hsdi.MinitPay.ShowCodeActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689797, "field 'ivBarCode'");
    target.ivBarCode = finder.castView(view, 2131689797, "field 'ivBarCode'");
  }

  @Override public void unbind(T target) {
    target.ivBarCode = null;
  }
}
