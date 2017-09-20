// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.contact_selection;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SelectContactsActivity$$ViewBinder<T extends com.hsdi.NetMe.ui.contact_selection.SelectContactsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689778, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131689778, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131689779, "field 'listView'");
    target.listView = finder.castView(view, 2131689779, "field 'listView'");
    view = finder.findRequiredView(source, 2131689780, "field 'progressbar'");
    target.progressbar = view;
  }

  @Override public void unbind(T target) {
    target.toolbar = null;
    target.listView = null;
    target.progressbar = null;
  }
}
