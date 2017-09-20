// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.chat.text.helper_activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SoundRecorderActivity$$ViewBinder<T extends com.hsdi.NetMe.ui.chat.text.helper_activities.SoundRecorderActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689798, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131689798, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131689799, "field 'cancelBtn'");
    target.cancelBtn = view;
    view = finder.findRequiredView(source, 2131689801, "field 'acceptBtn'");
    target.acceptBtn = view;
    view = finder.findRequiredView(source, 2131689800, "field 'stateText'");
    target.stateText = finder.castView(view, 2131689800, "field 'stateText'");
    view = finder.findRequiredView(source, 2131689802, "field 'time'");
    target.time = finder.castView(view, 2131689802, "field 'time'");
    view = finder.findRequiredView(source, 2131689803, "field 'actionButton'");
    target.actionButton = finder.castView(view, 2131689803, "field 'actionButton'");
    view = finder.findRequiredView(source, 2131689804, "field 'seekBar'");
    target.seekBar = finder.castView(view, 2131689804, "field 'seekBar'");
  }

  @Override public void unbind(T target) {
    target.toolbar = null;
    target.cancelBtn = null;
    target.acceptBtn = null;
    target.stateText = null;
    target.time = null;
    target.actionButton = null;
    target.seekBar = null;
  }
}
