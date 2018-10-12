package kz.onko_zko.hospital.hamburger;

import android.os.Parcel;
import android.os.Parcelable;

public class SubMenuItem implements Parcelable {
    private String name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubMenuItem(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    protected SubMenuItem(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<SubMenuItem> CREATOR = new Parcelable.Creator<SubMenuItem>() {
        @Override
        public SubMenuItem createFromParcel(Parcel source) {
            return new SubMenuItem(source);
        }

        @Override
        public SubMenuItem[] newArray(int size) {
            return new SubMenuItem[size];
        }
    };
}
