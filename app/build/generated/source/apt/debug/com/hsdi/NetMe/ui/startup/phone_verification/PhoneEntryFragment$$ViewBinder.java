// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.startup.phone_verification;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PhoneEntryFragment$$ViewBinder<T extends com.hsdi.NetMe.ui.startup.phone_verification.PhoneEntryFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689873, "field 'phoneEntryView'");
    target.phoneEntryView = view;
    view = finder.findRequiredView(source, 2131689875, "field 'verifyBtn' and method 'onClick'");
    target.verifyBtn = finder.castView(view, 2131689875, "field 'verifyBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689870, "field 'countrySpinner'");
    target.countrySpinner = finder.castView(view, 2131689870, "field 'countrySpinner'");
    view = finder.findRequiredView(source, 2131689872, "field 'phoneNumberET'");
    target.phoneNumberET = finder.castView(view, 2131689872, "field 'phoneNumberET'");
  }

  @Override public void unbind(T target) {
    target.phoneEntryView = null;
    target.verifyBtn = null;
    target.countrySpinner = null;
    target.phoneNumberET = null;
  }
}
