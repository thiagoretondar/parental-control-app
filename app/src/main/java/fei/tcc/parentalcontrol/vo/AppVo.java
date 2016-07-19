package fei.tcc.parentalcontrol.vo;

import android.graphics.drawable.Drawable;

/**
 * Created by thiagoretondar on 7/18/16.
 */
public class AppVo {

    private String name;
    private Drawable icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return name;
    }
}
