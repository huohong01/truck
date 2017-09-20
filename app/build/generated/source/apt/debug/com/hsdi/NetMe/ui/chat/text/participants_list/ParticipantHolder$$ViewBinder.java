// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.chat.text.participants_list;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ParticipantHolder$$ViewBinder<T extends com.hsdi.NetMe.ui.chat.text.participants_list.ParticipantHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689953, "field 'avatar'");
    target.avatar = finder.castView(view, 2131689953, "field 'avatar'");
    view = finder.findRequiredView(source, 2131689954, "field 'initials'");
    target.initials = finder.castView(view, 2131689954, "field 'initials'");
    view = finder.findRequiredView(source, 2131689955, "field 'name'");
    target.name = finder.castView(view, 2131689955, "field 'name'");
    view = finder.findRequiredView(source, 2131689956, "field 'number'");
    target.number = finder.castView(view, 2131689956, "field 'number'");
    view = finder.findRequiredView(source, 2131689957, "field 'textChatBtn'");
    target.textChatBtn = view;
    view = finder.findRequiredView(source, 2131689958, "field 'videoChatBtn'");
    target.videoChatBtn = view;
  }

  @Override public void unbind(T target) {
    target.avatar = null;
    target.initials = null;
    target.name = null;
    target.number = null;
    target.textChatBtn = null;
    target.videoChatBtn = null;
  }
}
