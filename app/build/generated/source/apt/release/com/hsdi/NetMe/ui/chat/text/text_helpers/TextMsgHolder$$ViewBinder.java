// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.chat.text.text_helpers;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class TextMsgHolder$$ViewBinder<T extends com.hsdi.NetMe.ui.chat.text.text_helpers.TextMsgHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689948, "field 'avatar'");
    target.avatar = finder.castView(view, 2131689948, "field 'avatar'");
    view = finder.findRequiredView(source, 2131689949, "field 'avatarLetter'");
    target.avatarLetter = finder.castView(view, 2131689949, "field 'avatarLetter'");
    view = finder.findRequiredView(source, 2131689950, "field 'container'");
    target.container = finder.castView(view, 2131689950, "field 'container'");
    view = finder.findRequiredView(source, 2131689951, "field 'name'");
    target.name = finder.castView(view, 2131689951, "field 'name'");
    view = finder.findRequiredView(source, 2131689952, "field 'time'");
    target.time = finder.castView(view, 2131689952, "field 'time'");
  }

  @Override public void unbind(T target) {
    target.avatar = null;
    target.avatarLetter = null;
    target.container = null;
    target.name = null;
    target.time = null;
  }
}
