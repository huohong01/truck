// Generated code from Butter Knife. Do not modify!
package com.hsdi.MinitPay;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MinitPayActivity$$ViewBinder<T extends com.hsdi.MinitPay.MinitPayActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689665, "field 'btnSignIn'");
    target.btnSignIn = finder.castView(view, 2131689665, "field 'btnSignIn'");
    view = finder.findRequiredView(source, 2131689740, "field 'btnScanCode'");
    target.btnScanCode = finder.castView(view, 2131689740, "field 'btnScanCode'");
    view = finder.findRequiredView(source, 2131689741, "field 'btnShowCode'");
    target.btnShowCode = finder.castView(view, 2131689741, "field 'btnShowCode'");
    view = finder.findRequiredView(source, 2131689742, "field 'btnBalance'");
    target.btnBalance = finder.castView(view, 2131689742, "field 'btnBalance'");
  }

  @Override public void unbind(T target) {
    target.btnSignIn = null;
    target.btnScanCode = null;
    target.btnShowCode = null;
    target.btnBalance = null;
  }
}
