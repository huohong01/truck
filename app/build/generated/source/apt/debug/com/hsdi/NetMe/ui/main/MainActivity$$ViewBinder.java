// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.main;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.hsdi.NetMe.ui.main.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689729, "field 'viewPager'");
    target.viewPager = finder.castView(view, 2131689729, "field 'viewPager'");
    view = finder.findRequiredView(source, 2131689728, "field 'slidingTabLayout'");
    target.slidingTabLayout = finder.castView(view, 2131689728, "field 'slidingTabLayout'");
    view = finder.findRequiredView(source, 2131689726, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131689726, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131689727, "field 'spinner'");
    target.spinner = finder.castView(view, 2131689727, "field 'spinner'");
    view = finder.findRequiredView(source, 2131689725, "field 'drawer'");
    target.drawer = finder.castView(view, 2131689725, "field 'drawer'");
    view = finder.findRequiredView(source, 2131689730, "field 'avatar'");
    target.avatar = finder.castView(view, 2131689730, "field 'avatar'");
    view = finder.findRequiredView(source, 2131689731, "field 'userName'");
    target.userName = finder.castView(view, 2131689731, "field 'userName'");
    view = finder.findRequiredView(source, 2131689732, "field 'userNumber'");
    target.userNumber = finder.castView(view, 2131689732, "field 'userNumber'");
    view = finder.findRequiredView(source, 2131689733, "field 'doNotDisturb'");
    target.doNotDisturb = finder.castView(view, 2131689733, "field 'doNotDisturb'");
  }

  @Override public void unbind(T target) {
    target.viewPager = null;
    target.slidingTabLayout = null;
    target.toolbar = null;
    target.spinner = null;
    target.drawer = null;
    target.avatar = null;
    target.userName = null;
    target.userNumber = null;
    target.doNotDisturb = null;
  }
}
