// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.chat;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChatActivity$$ViewBinder<T extends com.hsdi.NetMe.ui.chat.ChatActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689672, "field 'videoFragContainer'");
    target.videoFragContainer = finder.castView(view, 2131689672, "field 'videoFragContainer'");
    view = finder.findRequiredView(source, 2131689673, "field 'textFragContainer'");
    target.textFragContainer = finder.castView(view, 2131689673, "field 'textFragContainer'");
    view = finder.findRequiredView(source, 2131689674, "field 'hideTextBtn'");
    target.hideTextBtn = view;
    view = finder.findRequiredView(source, 2131689671, "field 'invisibleET'");
    target.invisibleET = finder.castView(view, 2131689671, "field 'invisibleET'");
  }

  @Override public void unbind(T target) {
    target.videoFragContainer = null;
    target.textFragContainer = null;
    target.hideTextBtn = null;
    target.invisibleET = null;
  }
}
