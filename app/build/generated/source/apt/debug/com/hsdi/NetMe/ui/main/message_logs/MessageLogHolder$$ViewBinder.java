// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.main.message_logs;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MessageLogHolder$$ViewBinder<T extends com.hsdi.NetMe.ui.main.message_logs.MessageLogHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689983, "field 'contentContainer'");
    target.contentContainer = view;
    view = finder.findRequiredView(source, 2131689984, "field 'avatar'");
    target.avatar = finder.castView(view, 2131689984, "field 'avatar'");
    view = finder.findRequiredView(source, 2131689985, "field 'avatarLetter'");
    target.avatarLetter = finder.castView(view, 2131689985, "field 'avatarLetter'");
    view = finder.findRequiredView(source, 2131689987, "field 'name'");
    target.name = finder.castView(view, 2131689987, "field 'name'");
    view = finder.findRequiredView(source, 2131689988, "field 'content'");
    target.content = finder.castView(view, 2131689988, "field 'content'");
    view = finder.findRequiredView(source, 2131689989, "field 'date'");
    target.date = finder.castView(view, 2131689989, "field 'date'");
    view = finder.findRequiredView(source, 2131689990, "field 'time'");
    target.time = finder.castView(view, 2131689990, "field 'time'");
    view = finder.findRequiredView(source, 2131689982, "field 'delete'");
    target.delete = view;
  }

  @Override public void unbind(T target) {
    target.contentContainer = null;
    target.avatar = null;
    target.avatarLetter = null;
    target.name = null;
    target.content = null;
    target.date = null;
    target.time = null;
    target.delete = null;
  }
}
