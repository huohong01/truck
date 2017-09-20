package com.hsdi.NetMe.ui.startup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.hsdi.NetMe.BaseActivity;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.theme.basic.ThemeManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.hsdi.theme.basic.ThemeLayoutInflaterFactory.ThemeTypeValue.toolbar_nav_icon;

public class ThemeActivity extends BaseActivity {

    @Bind(R.id.theme_toolbar)
    Toolbar themeToolbar;
    @Bind(R.id.theme_gridview)
    GridView themeGridview;

    private SimpleAdapter themeAdapter;
    private ThemeManager themeManager;
    private List<Map<String, Object>> themeList = new ArrayList<>();
    private int[] icon = {R.drawable.theme_orange, R.drawable.theme_green, R.drawable.theme_blue, R.drawable.theme_red};
    private Map<String, Object> currentTheme;
    private String[] from = {"image", "status", "type"};
    private int[] to = {R.id.theme_picture, R.id.theme_status};


    private ThemeManager.OnApplyListener onApplyListener = new ThemeManager.OnApplyListener() {
        @Override
        public void onApply(boolean isSuccess) {
            for (Map<String, Object> themeDataItem : themeList) {
                themeDataItem.put("status", R.drawable.icon_uncheck);
            }
            currentTheme.put("status", R.drawable.icon_check);
            themeAdapter.notifyDataSetChanged();
            NetMeApp.getInstance().Restart();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        themeToolbar.setTitle("Theme");
        themeToolbar.setNavigationIcon(R.drawable.back);
        applyTheme(themeToolbar, R.drawable.back, toolbar_nav_icon);
        themeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        themeManager = new ThemeManager(this);
        //填充数据源
        Map<String, Object> themeOragne = new HashMap<>();
        themeOragne.put("type", ThemeManager.ThemeName.ORANGE_DEFAULT);
        themeOragne.put("image", icon[0]);
        themeOragne.put("status", R.drawable.icon_uncheck);
        themeList.add(themeOragne);
        Map<String, Object> themeGreen = new HashMap<>();
        themeGreen.put("type", ThemeManager.ThemeName.GREEN);
        themeGreen.put("image", icon[1]);
        themeGreen.put("status", R.drawable.icon_uncheck);
        themeList.add(themeGreen);

     /*   Map<String, Object> themeBlue = new HashMap<>();
        themeBlue.put("type", ThemeManager.ThemeName.BLUE);
        themeBlue.put("image", icon[2]);
        themeBlue.put("status", R.drawable.icon_uncheck);
        themeList.add(themeBlue);
        Map<String, Object> themeRed = new HashMap<>();
        themeRed.put("type", ThemeManager.ThemeName.RED);
        themeRed.put("image", icon[3]);
        themeRed.put("status", R.drawable.icon_uncheck);
        themeList.add(themeRed);
*/
        for (Map<String, Object> themeDataItem : themeList) {
            if (themeDataItem.get("type") == themeManager.getCurrentTheme()) {
                themeDataItem.put("status", R.drawable.icon_check);
            }
        }

        //初始化适配器
        themeAdapter = new SimpleAdapter(this, themeList, R.layout.item_theme, from, to);
        themeGridview.setAdapter(themeAdapter);
        themeGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentTheme = themeList.get(position);
                showDialog();
            }
        });
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.change_skin))
                .setMessage(getString(R.string.change_skin_dialog_message))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                themeManager.applyTheme((ThemeManager.ThemeName) currentTheme.get("type"), onApplyListener);
                            }
                        }
                )
                .show();
    }

}
