// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.chat.text;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class TextFragment$$ViewBinder<T extends com.hsdi.NetMe.ui.chat.text.TextFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689916, "field 'chatTyping'");
    target.chatTyping = finder.castView(view, 2131689916, "field 'chatTyping'");
    view = finder.findRequiredView(source, 2131689915, "field 'chatEntryFields'");
    target.chatEntryFields = finder.castView(view, 2131689915, "field 'chatEntryFields'");
    view = finder.findRequiredView(source, 2131689908, "field 'chatRlContainer'");
    target.chatRlContainer = finder.castView(view, 2131689908, "field 'chatRlContainer'");
    view = finder.findRequiredView(source, 2131689920, "field 'chatCloseDrawer' and method 'closeDrawer'");
    target.chatCloseDrawer = finder.castView(view, 2131689920, "field 'chatCloseDrawer'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.closeDrawer(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689911, "field 'chatToolbarTyping'");
    target.chatToolbarTyping = finder.castView(view, 2131689911, "field 'chatToolbarTyping'");
    view = finder.findRequiredView(source, 2131689919, "field 'chatSendBtn' and method 'onSendClicked'");
    target.chatSendBtn = finder.castView(view, 2131689919, "field 'chatSendBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onSendClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689917, "field 'llChatSend'");
    target.llChatSend = finder.castView(view, 2131689917, "field 'llChatSend'");
    view = finder.findRequiredView(source, 2131689909, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131689909, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131689913, "field 'msgList'");
    target.msgList = finder.castView(view, 2131689913, "field 'msgList'");
    view = finder.findRequiredView(source, 2131689914, "field 'pendingMediaList'");
    target.pendingMediaList = finder.castView(view, 2131689914, "field 'pendingMediaList'");
    view = finder.findRequiredView(source, 2131689683, "field 'msgEntryField'");
    target.msgEntryField = finder.castView(view, 2131689683, "field 'msgEntryField'");
    view = finder.findRequiredView(source, 2131689910, "field 'toolbarTitle'");
    target.toolbarTitle = finder.castView(view, 2131689910, "field 'toolbarTitle'");
    view = finder.findRequiredView(source, 2131689918, "field 'attachButton' and method 'onAttachClicked'");
    target.attachButton = view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onAttachClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689912, "field 'progressBar'");
    target.progressBar = view;
    view = finder.findRequiredView(source, 2131689907, "field 'drawer'");
    target.drawer = finder.castView(view, 2131689907, "field 'drawer'");
    view = finder.findRequiredView(source, 2131689921, "field 'participantList'");
    target.participantList = finder.castView(view, 2131689921, "field 'participantList'");
  }

  @Override public void unbind(T target) {
    target.chatTyping = null;
    target.chatEntryFields = null;
    target.chatRlContainer = null;
    target.chatCloseDrawer = null;
    target.chatToolbarTyping = null;
    target.chatSendBtn = null;
    target.llChatSend = null;
    target.toolbar = null;
    target.msgList = null;
    target.pendingMediaList = null;
    target.msgEntryField = null;
    target.toolbarTitle = null;
    target.attachButton = null;
    target.progressBar = null;
    target.drawer = null;
    target.participantList = null;
  }
}
