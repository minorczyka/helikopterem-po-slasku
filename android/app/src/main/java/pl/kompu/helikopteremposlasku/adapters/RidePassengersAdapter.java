package pl.kompu.helikopteremposlasku.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import pl.kompu.helikopteremposlasku.model.Passenger;
import pl.kompu.helikopteremposlasku.model.User;
import pl.kompu.helikopteremposlasku.R;
import pl.kompu.helikopteremposlasku.view.PassengerCheckBox;
import pl.kompu.helikopteremposlasku.listeners.PassengerCheckBoxListener;

/**
 * Created by Kompu on 2015-09-27.
 */
public class RidePassengersAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<User> users;
    private ArrayList<Passenger> passengers;
    private PassengerCheckBoxListener checkBoxListener;
    private ArrayList<WeakReference<CheckBox>> onceCheckBoxes;
    private ArrayList<WeakReference<CheckBox>> twiceCheckBoxes;

    public RidePassengersAdapter(Context context, ArrayList<User> users, ArrayList<Passenger> passengers, PassengerCheckBoxListener listener) {
        this.context = context;
        this.users = users;
        this.passengers = passengers;
        this.checkBoxListener = listener;

        onceCheckBoxes = new ArrayList<>();
        twiceCheckBoxes = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        boolean viewNull = false;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_ride_passenger, viewGroup, false);
            viewNull = true;
        }

        User user = (User) getItem(i);
        TextView driverName = (TextView) view.findViewById(R.id.driver_name);
        CheckBox once = (CheckBox) view.findViewById(R.id.checkbox_once);
        CheckBox twice = (CheckBox) view.findViewById(R.id.checkbox_twice);

        driverName.setText(user.name);

        Passenger passenger = getPassenger(user);
        if (passenger != null) {
            once.setChecked(!passenger.twice);
            twice.setChecked(passenger.twice);
        }

        if (viewNull) {
            CompoundButton.OnCheckedChangeListener listener = new PassengerCheckBox(once, twice, checkBoxListener);
            once.setOnCheckedChangeListener(listener);
            twice.setOnCheckedChangeListener(listener);

            if (i < onceCheckBoxes.size()) {
                onceCheckBoxes.set(i, new WeakReference<>(once));
            } else {
                onceCheckBoxes.add(new WeakReference<>(once));
            }
            if (i < twiceCheckBoxes.size()) {
                twiceCheckBoxes.set(i, new WeakReference<>(twice));
            } else {
                twiceCheckBoxes.add(new WeakReference<>(twice));
            }
        }

        return view;
    }

    public void changeChecked(boolean once, boolean checked) {
        ArrayList<WeakReference<CheckBox>> list = once ? onceCheckBoxes : twiceCheckBoxes;
        for (WeakReference<CheckBox> weakReference : list) {
            CheckBox checkBox = weakReference.get();
            if (checkBox != null) {
                checkBox.setChecked(checked);
            }
        }
    }

    private Passenger getPassenger(User user) {
        for (Passenger passenger : passengers) {
            if (passenger.person == user.id) {
                return passenger;
            }
        }
        return null;
    }

    public ArrayList<Passenger> getPassengers() {
        ArrayList<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < onceCheckBoxes.size(); ++i) {
            CheckBox once = onceCheckBoxes.get(i).get();
            CheckBox twice = twiceCheckBoxes.get(i).get();
            User user = users.get(i);
            if (once != null && twice != null && user != null) {
                if (once.isChecked()) {
                    passengers.add(new Passenger(user.id, false));
                } else if (twice.isChecked()) {
                    passengers.add(new Passenger(user.id, true));
                }
            }
        }
        return passengers;
    }
}
