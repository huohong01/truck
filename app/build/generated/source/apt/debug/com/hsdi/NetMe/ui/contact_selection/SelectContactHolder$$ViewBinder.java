// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.contact_selection;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SelectContactHolder$$ViewBinder<T extends com.hsdi.NetMe.ui.contact_selection.SelectContactHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689996, "field 'avatar'");
    target.avatar = finder.castView(view, 2131689996, "field 'avatar'");
    view = finder.findRequiredView(source, 2131689997, "field 'avatarLetter'");
    target.avatarLetter = finder.castView(view, 2131689997, "field 'avatarLetter'");
    view = finder.findRequiredView(source, 2131689998, "field 'text'");
    target.text = finder.castView(view, 2131689998, "field 'text'");
    view = finder.findRequiredView(source, 2131689999, "field 'checkBox'");
    target.checkBox = finder.castView(view, 2131689999, "field 'checkBox'");
  }

  @Override public void unbind(T target) {
    target.avatar = null;
    target.avatarLetter = null;
    target.text = null;
    target.checkBox = null;
  }
}
