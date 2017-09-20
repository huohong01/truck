package com.hsdi.NetMe.models;

import com.hsdi.theme.basic.ThemeManager;

/**
 * Created by huohong.yi on 2017/8/18.
 */

public class Theme {

    private ThemeManager.ThemeName themeName;
    private boolean state;
    private int icon;

    public Theme(ThemeManager.ThemeName themeName, boolean state) {
        this.themeName = themeName;
        this.state = state;
    }

    public Theme(ThemeManager.ThemeName themeName, boolean state, int icon) {
        this.themeName = themeName;
        this.state = state;
        this.icon = icon;
    }

    public ThemeManager.ThemeName getThemeName() {
        return themeName;
    }

    public void setThemeName(ThemeManager.ThemeName themeName) {
        this.themeName = themeName;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "themeName=" + themeName +
                ", state=" + state +
                ", icon=" + icon +
                '}';
    }
}
