package pl.kompu.helikopteremposlasku.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by Kompu on 2015-09-25.
 */
public class Passenger implements Parcelable {
    public long person;
    public boolean twice;

    public Passenger(long person, boolean twice) {
        this.person = person;
        this.twice = twice;
    }

    protected Passenger(Parcel in) {
        person = in.readLong();
        twice = (in.readInt() == 1);
    }

    public static final Creator<Passenger> CREATOR = new Creator<Passenger>() {
        @Override
        public Passenger createFromParcel(Parcel in) {
            return new Passenger(in);
        }

        @Override
        public Passenger[] newArray(int size) {
            return new Passenger[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(person);
        parcel.writeInt(twice ? 1 : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Passenger))return false;
        Passenger other = (Passenger)o;

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.person, other.person);
        builder.append(this.twice, other.twice);
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(person);
        builder.append(twice);
        return builder.toHashCode();
    }
}
