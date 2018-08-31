package pl.kompu.helikopteremposlasku.fragments;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roomorama.caldroid.CaldroidFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.kompu.helikopteremposlasku.HelikopterApp;
import pl.kompu.helikopteremposlasku.listeners.CalendarListener;
import pl.kompu.helikopteremposlasku.model.Ride;
import pl.kompu.helikopteremposlasku.model.User;
import pl.kompu.helikopteremposlasku.R;
import retrofit.Callback;
import retrofit.Response;

public class CalendarFragment extends BaseFragment implements Callback<ArrayList<Ride>> {

    private CaldroidCustomFragment caldroidFragment;
    private CalendarListener calendarListener;

    public static CalendarFragment newInstance(ArrayList<User> users) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_USERS, users);
        fragment.setArguments(args);
        return fragment;
    }

    public CalendarFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        caldroidFragment = new CaldroidCustomFragment();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();

        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        args.putInt(CaldroidFragment.MONTH, month);
        args.putInt(CaldroidFragment.YEAR, year);
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putParcelableArrayList(CaldroidCustomFragment.USERS, userList);
        caldroidFragment.setArguments(args);

        calendarListener = new CalendarListener(this, userList, caldroidFragment);
        caldroidFragment.setCaldroidListener(calendarListener);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.calendar_fragment, caldroidFragment).commit();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int year = caldroidFragment.getYear();
        int month = caldroidFragment.getMonth();
        if (year != -1 && month != -1) {
            calendarListener.onChangeMonth(month, year);
        }
    }

    @Override
    public void onResponse(Response<ArrayList<Ride>> response) {
        if (response.code() == 200) {
            List<String> segments = response.raw().request().httpUrl().pathSegments();
            int year = Integer.valueOf(segments.get(segments.size() - 2));
            int month = Integer.valueOf(segments.get(segments.size() - 1));
            ArrayList<Ride> rides = response.body();
            caldroidFragment.addRides(year, month, rides);
        } else if (response.code() == 401) {
            onFailure(new Throwable(response.message()));
        }
    }

    @Override
    public void onFailure(Throwable t) {
        int i = 0;
        ++i;
    }
}
