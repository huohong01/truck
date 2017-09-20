// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.contact_detail;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ContactDetailActivity$$ViewBinder<T extends com.hsdi.NetMe.ui.contact_detail.ContactDetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689681, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131689681, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131689676, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131689676, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131689678, "field 'tvName'");
    target.tvName = finder.castView(view, 2131689678, "field 'tvName'");
    view = finder.findRequiredView(source, 2131689679, "field 'recyclerView'");
    target.recyclerView = finder.castView(view, 2131689679, "field 'recyclerView'");
    view = finder.findRequiredView(source, 2131689680, "field 'favoriteBtn'");
    target.favoriteBtn = finder.castView(view, 2131689680, "field 'favoriteBtn'");
  }

  @Override public void unbind(T target) {
    target.toolbar = null;
    target.ivAvatar = null;
    target.tvName = null;
    target.recyclerView = null;
    target.favoriteBtn = null;
  }
}
