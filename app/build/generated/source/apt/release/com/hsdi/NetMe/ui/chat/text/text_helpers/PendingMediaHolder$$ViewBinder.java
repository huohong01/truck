// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.chat.text.text_helpers;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PendingMediaHolder$$ViewBinder<T extends com.hsdi.NetMe.ui.chat.text.text_helpers.PendingMediaHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689995, "field 'close'");
    target.close = view;
    view = finder.findRequiredView(source, 2131689992, "field 'imageView'");
    target.imageView = finder.castView(view, 2131689992, "field 'imageView'");
    view = finder.findRequiredView(source, 2131689993, "field 'mapView'");
    target.mapView = finder.castView(view, 2131689993, "field 'mapView'");
    view = finder.findRequiredView(source, 2131689994, "field 'textView'");
    target.textView = finder.castView(view, 2131689994, "field 'textView'");
  }

  @Override public void unbind(T target) {
    target.close = null;
    target.imageView = null;
    target.mapView = null;
    target.textView = null;
  }
}
