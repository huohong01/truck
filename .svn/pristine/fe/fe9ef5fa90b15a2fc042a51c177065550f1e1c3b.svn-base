package com.hsdi.NetMe.util;

/**
 * anthor : YIDS
 * date :  2017/8/31
 */

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hsdi.NetMe.R;

import java.util.ArrayList;

/**
 * 自定义弹出菜单
 * @author guopeng
 * @created 2015年11月27日
 */
public class PopMenu {

    private Context context;

    private ArrayList<String> itemList;
   // private ArrayList<String> popMenuOption;
    private PopupWindow popupWindow;

    private ListView listView;


    @SuppressWarnings("deprecation")
    public PopMenu(Context context) {

        this.context = context;
        itemList = new ArrayList<String>();
        View view = LayoutInflater.from(context).inflate(R.layout.popmenu, null);

        //设置 listview
        listView = (ListView)view.findViewById(R.id.popup_view_listView);
        listView.setAdapter(new PopAdapter(itemList));
        listView.setFocusableInTouchMode(true);
        listView.setFocusable(true);

        popupWindow = new PopupWindow(view, 100, LayoutParams.WRAP_CONTENT);
        popupWindow = new PopupWindow(view,
                context.getResources().getDimensionPixelSize(R.dimen.popmenu_width),
                LayoutParams.WRAP_CONTENT);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    //设置菜单项点击监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        listView.setOnItemClickListener(listener);
    }

    //单个添加菜单项
    public void addItem(String item) {
        itemList.add(item);
    }


    //下拉式 弹出 pop菜单 parent 右下角
    public void showAsDropDown(View parent) {
       // popupWindow.showAsDropDown(parent, 10,
                //保证尺寸是根据屏幕像素密度来的
        //        context.getResources().getDimensionPixelSize(R.dimen.popmenu_yoff));
        //popupWindow.showAsDropDown(parent);
       popupWindow.showAtLocation(parent, Gravity.BOTTOM|Gravity.LEFT,0,context.getResources().getDimensionPixelSize(R.dimen.popmenu_width));
        ViewGroup popupView = (ViewGroup) popupWindow.getContentView().getParent();
        if (popupView != null) {
            popupView.setPadding(0, 0, 0, 0);
        }

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        //刷新状态
        popupWindow.update();
    }

    //隐藏菜单
    public void dismiss() {
        popupWindow.dismiss();
    }

    // 适配器
    private final class PopAdapter extends BaseAdapter {
        private ArrayList<String> menuList;

        public PopAdapter(ArrayList<String> menuList) {
            this.menuList = menuList;
        }

        @Override
        public int getCount() {
            return menuList.size();
        }

        @Override
        public Object getItem(int position) {
            return menuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.popmenu_item, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.groupItem = (TextView) convertView.findViewById(R.id.pop_item_header);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.groupItem.setText(menuList.get(position));
            return convertView;
        }

        private final class ViewHolder {
            TextView groupItem;
        }
    }
}
