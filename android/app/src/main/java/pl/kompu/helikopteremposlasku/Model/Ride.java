package pl.kompu.helikopteremposlasku.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Kompu on 2015-09-25.
 */
public class Ride implements Parcelable {
    public long id;
    public long driver;
    public Date date;
    public ArrayList<Passenger> passengers;

    public Ride(long driver, Date date) {
        this.id = -1;
        this.driver = driver;
        this.date = date;
        passengers = new ArrayList<>();
    }

    protected Ride(Parcel in) {
        id = in.readLong();
        driver = in.readLong();
        date = new Date(in.readLong());
        passengers = in.createTypedArrayList(Passenger.CREATOR);
    }

    public static final Creator<Ride> CREATOR = new Creator<Ride>() {
        @Override
        public Ride createFromParcel(Parcel in) {
            return new Ride(in);
        }

        @Override
        public Ride[] newArray(int size) {
            return new Ride[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(driver);
        parcel.writeLong(date.getTime());
        parcel.writeTypedList(passengers);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Ride))return false;
        Ride other = (Ride)o;

        Collections.sort(this.passengers, new PassengerComparator());
        Collections.sort(other.passengers, new PassengerComparator());

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.driver, other.driver);
        builder.append(this.date.getTime(), other.date.getTime());
        builder.append(this.passengers, other.passengers);
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(driver);
        builder.append(date.getTime());
        for (Passenger it : passengers) {
            builder.append(it.hashCode());
        }
        return builder.toHashCode();
    }

    public class PassengerComparator implements Comparator<Passenger> {

        @Override
        public int compare(Passenger first, Passenger second) {
            return Long.compare(first.person, second.person);
        }
    }
}
