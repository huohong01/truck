// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.ContactListUtils;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ContactHolder$$ViewBinder<T extends com.hsdi.NetMe.ui.ContactListUtils.ContactHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689965, "field 'favIcon'");
    target.favIcon = finder.castView(view, 2131689965, "field 'favIcon'");
    view = finder.findRequiredView(source, 2131689963, "field 'avatar'");
    target.avatar = finder.castView(view, 2131689963, "field 'avatar'");
    view = finder.findRequiredView(source, 2131689964, "field 'avatarLetter'");
    target.avatarLetter = finder.castView(view, 2131689964, "field 'avatarLetter'");
    view = finder.findRequiredView(source, 2131689959, "field 'alphabetIndicator'");
    target.alphabetIndicator = finder.castView(view, 2131689959, "field 'alphabetIndicator'");
    view = finder.findRequiredView(source, 2131689960, "field 'name'");
    target.name = finder.castView(view, 2131689960, "field 'name'");
    view = finder.findRequiredView(source, 2131689962, "field 'divider'");
    target.divider = view;
    view = finder.findRequiredView(source, 2131689961, "field 'registeredMarker'");
    target.registeredMarker = view;
  }

  @Override public void unbind(T target) {
    target.favIcon = null;
    target.avatar = null;
    target.avatarLetter = null;
    target.alphabetIndicator = null;
    target.name = null;
    target.divider = null;
    target.registeredMarker = null;
  }
}
