package xyz.codewithcoffee.cyc_app;

import android.graphics.drawable.Drawable;

public class Appitem {
    public Drawable icon;
    public String name;
    public String packge_name;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackge_name() {
        return packge_name;
    }

    public void setPackge_name(String packge_name) {
        this.packge_name = packge_name;
    }

    public Appitem(Drawable icon, String name, String packge_name) {
        this.icon = icon;
        this.name = name;
        this.packge_name = packge_name;
    }

}
