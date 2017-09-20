// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ProfileActivity$$ViewBinder<T extends com.hsdi.NetMe.ui.ProfileActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689753, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131689753, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131689748, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131689748, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131689750, "field 'etFirstName'");
    target.etFirstName = finder.castView(view, 2131689750, "field 'etFirstName'");
    view = finder.findRequiredView(source, 2131689752, "field 'etLastName'");
    target.etLastName = finder.castView(view, 2131689752, "field 'etLastName'");
  }

  @Override public void unbind(T target) {
    target.toolbar = null;
    target.ivAvatar = null;
    target.etFirstName = null;
    target.etLastName = null;
  }
}
