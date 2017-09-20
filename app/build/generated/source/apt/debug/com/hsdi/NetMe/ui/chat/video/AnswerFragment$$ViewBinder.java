// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.chat.video;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AnswerFragment$$ViewBinder<T extends com.hsdi.NetMe.ui.chat.video.AnswerFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689839, "field 'imageView'");
    target.imageView = finder.castView(view, 2131689839, "field 'imageView'");
    view = finder.findRequiredView(source, 2131689841, "field 'nameView'");
    target.nameView = finder.castView(view, 2131689841, "field 'nameView'");
    view = finder.findRequiredView(source, 2131689842, "field 'numberView'");
    target.numberView = finder.castView(view, 2131689842, "field 'numberView'");
    view = finder.findRequiredView(source, 2131689845, "method 'onAnswerClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onAnswerClicked(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689844, "method 'onDenyClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onDenyClicked(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.imageView = null;
    target.nameView = null;
    target.numberView = null;
  }
}
