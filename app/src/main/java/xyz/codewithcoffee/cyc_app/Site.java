package xyz.codewithcoffee.cyc_app;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;


public class Site implements Parcelable {

    private String code = null;
    private String name = null;
    private boolean selected = false;

    public Site(String code, String name, boolean selected) {
        super();
        this.code = code;
        this.name = name;
        this.selected = selected;
    }

    protected Site(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);
        this.code = data[0];
        this.name = data[1];
        this.selected = Boolean.valueOf(data[2]);
    }

    public static final Creator<Site> CREATOR = new Creator<Site>() {
        @Override
        public Site createFromParcel(Parcel in) {
            return new Site(in);
        }

        @Override
        public Site[] newArray(int size) {
            return new Site[size];
        }
    };

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{code,name,Boolean.toString(selected)});
    }
}
