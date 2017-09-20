// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.chat.audio;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VoiceFragment$$ViewBinder<T extends com.hsdi.NetMe.ui.chat.audio.VoiceFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689938, "field 'voiceMute' and method 'toggleMicrophone'");
    target.voiceMute = finder.castView(view, 2131689938, "field 'voiceMute'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toggleMicrophone(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689939, "field 'voiceEnd' and method 'endVoiceChat'");
    target.voiceEnd = finder.castView(view, 2131689939, "field 'voiceEnd'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.endVoiceChat(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689940, "field 'voiceShowChat' and method 'showTextChat'");
    target.voiceShowChat = finder.castView(view, 2131689940, "field 'voiceShowChat'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showTextChat(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689937, "field 'voiceControlsWrapper'");
    target.voiceControlsWrapper = finder.castView(view, 2131689937, "field 'voiceControlsWrapper'");
    view = finder.findRequiredView(source, 2131689942, "field 'voiceName'");
    target.voiceName = finder.castView(view, 2131689942, "field 'voiceName'");
    view = finder.findRequiredView(source, 2131689944, "field 'voiceTime'");
    target.voiceTime = finder.castView(view, 2131689944, "field 'voiceTime'");
    view = finder.findRequiredView(source, 2131689943, "field 'voiceAvatar'");
    target.voiceAvatar = finder.castView(view, 2131689943, "field 'voiceAvatar'");
    view = finder.findRequiredView(source, 2131689946, "field 'voiceProgressbar'");
    target.voiceProgressbar = finder.castView(view, 2131689946, "field 'voiceProgressbar'");
    view = finder.findRequiredView(source, 2131689947, "field 'voiceFragmentHolder'");
    target.voiceFragmentHolder = finder.castView(view, 2131689947, "field 'voiceFragmentHolder'");
    view = finder.findRequiredView(source, 2131689941, "method 'setSpeakerphoneOn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setSpeakerphoneOn(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.voiceMute = null;
    target.voiceEnd = null;
    target.voiceShowChat = null;
    target.voiceControlsWrapper = null;
    target.voiceName = null;
    target.voiceTime = null;
    target.voiceAvatar = null;
    target.voiceProgressbar = null;
    target.voiceFragmentHolder = null;
  }
}
