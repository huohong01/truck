// Generated code from Butter Knife. Do not modify!
package com.hsdi.MinitPay;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PayActivity$$ViewBinder<T extends com.hsdi.MinitPay.PayActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689744, "field 'tvInvoice'");
    target.tvInvoice = finder.castView(view, 2131689744, "field 'tvInvoice'");
    view = finder.findRequiredView(source, 2131689745, "field 'tvPrice'");
    target.tvPrice = finder.castView(view, 2131689745, "field 'tvPrice'");
    view = finder.findRequiredView(source, 2131689746, "field 'btnPay'");
    target.btnPay = finder.castView(view, 2131689746, "field 'btnPay'");
  }

  @Override public void unbind(T target) {
    target.tvInvoice = null;
    target.tvPrice = null;
    target.btnPay = null;
  }
}
