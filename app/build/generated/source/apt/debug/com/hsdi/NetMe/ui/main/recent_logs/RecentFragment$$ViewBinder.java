// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.main.recent_logs;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RecentFragment$$ViewBinder<T extends com.hsdi.NetMe.ui.main.recent_logs.RecentFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689866, "field 'swipeRefreshLayout'");
    target.swipeRefreshLayout = finder.castView(view, 2131689866, "field 'swipeRefreshLayout'");
    view = finder.findRequiredView(source, 2131689867, "field 'recyclerList'");
    target.recyclerList = finder.castView(view, 2131689867, "field 'recyclerList'");
    view = finder.findRequiredView(source, 2131689868, "field 'emptyMsg'");
    target.emptyMsg = finder.castView(view, 2131689868, "field 'emptyMsg'");
  }

  @Override public void unbind(T target) {
    target.swipeRefreshLayout = null;
    target.recyclerList = null;
    target.emptyMsg = null;
  }
}
