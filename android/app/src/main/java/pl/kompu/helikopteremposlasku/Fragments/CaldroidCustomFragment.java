package pl.kompu.helikopteremposlasku.fragments;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import hirondelle.date4j.DateTime;
import pl.kompu.helikopteremposlasku.adapters.CaldroidCustomAdapter;
import pl.kompu.helikopteremposlasku.model.Ride;
import pl.kompu.helikopteremposlasku.model.User;

/**
 * Created by Kompu on 2015-09-25.
 */
public class CaldroidCustomFragment extends CaldroidFragment {

    public static String USERS = "users";

    private ArrayList<CaldroidCustomAdapter> adapters = new ArrayList<>();
    private HashMap<DateTime, ArrayList<Ride>> ridesMap = new HashMap<>();

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        ArrayList<User> users = getArguments().getParcelableArrayList(USERS);
        CaldroidCustomAdapter adapter = new CaldroidCustomAdapter(getActivity(), month, year, getCaldroidData(), extraData);
        adapter.setUsers(users);
        adapter.setRidesMap(ridesMap);
        adapters.add(adapter);
        return adapter;
    }

    public void addRides(int year, int month, ArrayList<Ride> rides) {
        Calendar calendar = Calendar.getInstance();

        ArrayList<DateTime> toBeRemoved = new ArrayList<>();
        for (Map.Entry<DateTime, ArrayList<Ride>> entry : ridesMap.entrySet()) {
            DateTime dateTime = entry.getKey();
            if (dateTime.getYear() == year && dateTime.getMonth() == month) {
                toBeRemoved.add(dateTime);
            }
        }
        for (DateTime dateTime : toBeRemoved) {
            ridesMap.remove(dateTime);
        }

        for (Ride ride : rides) {
            calendar.setTime(ride.date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DateTime dateTime = DateTime.forDateOnly(year, month, day);

            ArrayList<Ride> list = ridesMap.get(dateTime);
            if (list == null) {
                list = new ArrayList<>();
                ridesMap.put(dateTime, list);
            }
            list.add(ride);
        }
        for (CaldroidCustomAdapter adapter : adapters) {
            adapter.notifyDataSetChanged();
        }
        refreshView();
    }

    public ArrayList<Ride> getRides(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DateTime dateTime = DateTime.forDateOnly(year, month, day);

        ArrayList<Ride> list = ridesMap.get(dateTime);
        return list != null ? list : new ArrayList<Ride>();
    }
}
