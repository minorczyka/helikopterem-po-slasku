package pl.kompu.helikopteremposlasku.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kompu on 2015-09-24.
 */
public class UserColor implements Parcelable {

    public String name;
    public String backgroundColor;
    public String textColor;

    protected UserColor(Parcel in) {
        name = in.readString();
        backgroundColor = in.readString();
        textColor = in.readString();
    }

    public static final Creator<UserColor> CREATOR = new Creator<UserColor>() {
        @Override
        public UserColor createFromParcel(Parcel in) {
            return new UserColor(in);
        }

        @Override
        public UserColor[] newArray(int size) {
            return new UserColor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(backgroundColor);
        parcel.writeString(textColor);
    }
}
