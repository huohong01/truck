// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.main.favorites;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FavoritesHolder$$ViewBinder<T extends com.hsdi.NetMe.ui.main.favorites.FavoritesHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689971, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131689971, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131689972, "field 'tvName'");
    target.tvName = finder.castView(view, 2131689972, "field 'tvName'");
  }

  @Override public void unbind(T target) {
    target.ivAvatar = null;
    target.tvName = null;
  }
}
