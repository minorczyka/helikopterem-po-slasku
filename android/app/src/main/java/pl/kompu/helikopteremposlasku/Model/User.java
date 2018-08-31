package pl.kompu.helikopteremposlasku.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kompu on 2015-09-22.
 */
public class User implements Parcelable {

    public long id;
    public String name;
    public String username;
    public UserColor color;

    protected User(Parcel in) {
        id = in.readLong();
        name = in.readString();
        username = in.readString();
        color = in.readParcelable(UserColor.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(username);
        parcel.writeParcelable(color, i);
    }
}
