// Generated code from Butter Knife. Do not modify!
package com.hsdi.MinitPay;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BalanceActivity$$ViewBinder<T extends com.hsdi.MinitPay.BalanceActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689663, "field 'tvAccount'");
    target.tvAccount = finder.castView(view, 2131689663, "field 'tvAccount'");
    view = finder.findRequiredView(source, 2131689664, "field 'tvBalance'");
    target.tvBalance = finder.castView(view, 2131689664, "field 'tvBalance'");
    view = finder.findRequiredView(source, 2131689665, "field 'btnSignIn'");
    target.btnSignIn = finder.castView(view, 2131689665, "field 'btnSignIn'");
  }

  @Override public void unbind(T target) {
    target.tvAccount = null;
    target.tvBalance = null;
    target.btnSignIn = null;
  }
}
