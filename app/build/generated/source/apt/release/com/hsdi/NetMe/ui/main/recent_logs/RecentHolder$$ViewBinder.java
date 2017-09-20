// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.main.recent_logs;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RecentHolder$$ViewBinder<T extends com.hsdi.NetMe.ui.main.recent_logs.RecentHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689974, "field 'contentContainer'");
    target.contentContainer = view;
    view = finder.findRequiredView(source, 2131689975, "field 'avatar'");
    target.avatar = finder.castView(view, 2131689975, "field 'avatar'");
    view = finder.findRequiredView(source, 2131689976, "field 'avatarLetter'");
    target.avatarLetter = finder.castView(view, 2131689976, "field 'avatarLetter'");
    view = finder.findRequiredView(source, 2131689978, "field 'name'");
    target.name = finder.castView(view, 2131689978, "field 'name'");
    view = finder.findRequiredView(source, 2131689979, "field 'number'");
    target.number = finder.castView(view, 2131689979, "field 'number'");
    view = finder.findRequiredView(source, 2131689980, "field 'date'");
    target.date = finder.castView(view, 2131689980, "field 'date'");
    view = finder.findRequiredView(source, 2131689981, "field 'time'");
    target.time = finder.castView(view, 2131689981, "field 'time'");
    view = finder.findRequiredView(source, 2131689973, "field 'delete'");
    target.delete = view;
  }

  @Override public void unbind(T target) {
    target.contentContainer = null;
    target.avatar = null;
    target.avatarLetter = null;
    target.name = null;
    target.number = null;
    target.date = null;
    target.time = null;
    target.delete = null;
  }
}
