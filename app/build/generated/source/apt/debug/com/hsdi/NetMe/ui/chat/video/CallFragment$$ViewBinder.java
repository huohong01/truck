// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.chat.video;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CallFragment$$ViewBinder<T extends com.hsdi.NetMe.ui.chat.video.CallFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689846, "field 'imageView'");
    target.imageView = finder.castView(view, 2131689846, "field 'imageView'");
    view = finder.findRequiredView(source, 2131689848, "field 'nameView'");
    target.nameView = finder.castView(view, 2131689848, "field 'nameView'");
    view = finder.findRequiredView(source, 2131689849, "field 'numberView'");
    target.numberView = finder.castView(view, 2131689849, "field 'numberView'");
    view = finder.findRequiredView(source, 2131689850, "method 'onEndClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onEndClicked(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.imageView = null;
    target.nameView = null;
    target.numberView = null;
  }
}
