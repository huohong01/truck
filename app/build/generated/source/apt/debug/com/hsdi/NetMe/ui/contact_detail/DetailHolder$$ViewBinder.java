// Generated code from Butter Knife. Do not modify!
package com.hsdi.NetMe.ui.contact_detail;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class DetailHolder$$ViewBinder<T extends com.hsdi.NetMe.ui.contact_detail.DetailHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689966, "field 'typeIcon'");
    target.typeIcon = finder.castView(view, 2131689966, "field 'typeIcon'");
    view = finder.findRequiredView(source, 2131689967, "field 'title'");
    target.title = finder.castView(view, 2131689967, "field 'title'");
    view = finder.findRequiredView(source, 2131689968, "field 'label'");
    target.label = finder.castView(view, 2131689968, "field 'label'");
    view = finder.findRequiredView(source, 2131689969, "field 'messageBtn'");
    target.messageBtn = view;
    view = finder.findRequiredView(source, 2131689970, "field 'videoBtn'");
    target.videoBtn = view;
  }

  @Override public void unbind(T target) {
    target.typeIcon = null;
    target.title = null;
    target.label = null;
    target.messageBtn = null;
    target.videoBtn = null;
  }
}
