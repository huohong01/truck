// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.chat.video;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VideoFragment$$ViewBinder<T extends com.hsdi.NetMe.ui.chat.video.VideoFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689936, "field 'fragmentHolder'");
    target.fragmentHolder = finder.castView(view, 2131689936, "field 'fragmentHolder'");
    view = finder.findRequiredView(source, 2131689924, "field 'publisherView'");
    target.publisherView = finder.castView(view, 2131689924, "field 'publisherView'");
    view = finder.findRequiredView(source, 2131689922, "field 'subscriberView' and method 'toggleScreenControls'");
    target.subscriberView = finder.castView(view, 2131689922, "field 'subscriberView'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toggleScreenControls(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689923, "field 'mainOffIcon'");
    target.mainOffIcon = view;
    view = finder.findRequiredView(source, 2131689925, "field 'userOffIcon'");
    target.userOffIcon = view;
    view = finder.findRequiredView(source, 2131689934, "field 'progressBar'");
    target.progressBar = view;
    view = finder.findRequiredView(source, 2131689926, "field 'controlViews'");
    target.controlViews = view;
    view = finder.findRequiredView(source, 2131689929, "field 'cameraBtn' and method 'toggleCamera'");
    target.cameraBtn = finder.castView(view, 2131689929, "field 'cameraBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toggleCamera(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689927, "field 'muteBtn' and method 'toggleMicrophone'");
    target.muteBtn = finder.castView(view, 2131689927, "field 'muteBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toggleMicrophone(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689933, "field 'timeView'");
    target.timeView = finder.castView(view, 2131689933, "field 'timeView'");
    view = finder.findRequiredView(source, 2131689928, "method 'showTextChat'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showTextChat(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689930, "method 'endVideoChat'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.endVideoChat(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689932, "method 'flipCamera'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.flipCamera(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.fragmentHolder = null;
    target.publisherView = null;
    target.subscriberView = null;
    target.mainOffIcon = null;
    target.userOffIcon = null;
    target.progressBar = null;
    target.controlViews = null;
    target.cameraBtn = null;
    target.muteBtn = null;
    target.timeView = null;
  }
}
