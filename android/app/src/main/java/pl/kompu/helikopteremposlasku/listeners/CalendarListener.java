package pl.kompu.helikopteremposlasku.listeners;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import pl.kompu.helikopteremposlasku.HelikopterApp;
import pl.kompu.helikopteremposlasku.activities.RidesActivity;
import pl.kompu.helikopteremposlasku.fragments.CalendarFragment;
import pl.kompu.helikopteremposlasku.network.CredentialsProvider;
import pl.kompu.helikopteremposlasku.network.HelikopterService;
import pl.kompu.helikopteremposlasku.fragments.CaldroidCustomFragment;
import pl.kompu.helikopteremposlasku.model.Ride;
import pl.kompu.helikopteremposlasku.model.User;
import retrofit.Call;
import retrofit.Callback;

/**
 * Created by Kompu on 2015-09-25.
 */
public class CalendarListener extends CaldroidListener {

    @Inject HelikopterService service;
    @Inject CredentialsProvider credentialsProvider;
    private Callback<ArrayList<Ride>> listener;
    private ArrayList<User> users;
    private CaldroidCustomFragment fragment;

    public CalendarListener(CalendarFragment calendarFragment, ArrayList<User> users, CaldroidCustomFragment caldroidFragment) {
        this.listener = calendarFragment;
        this.users = users;
        this.fragment = caldroidFragment;

        ((HelikopterApp) calendarFragment.getActivity().getApplication()).getHelikopterCompnent().inject(this);
    }

    @Override
    public void onSelectDate(Date date, View view) {
        Intent intent = new Intent(fragment.getContext(), RidesActivity.class);

        intent.putExtra(RidesActivity.EXTRA_USERS, users);
        intent.putExtra(RidesActivity.EXTRA_DATE, date.getTime());

        ArrayList<Ride> rides = fragment.getRides(date);
        if (rides != null) {
            intent.putExtra(RidesActivity.EXTRA_RIDES, rides);
        }

        fragment.startActivity(intent);
    }

    @Override
    public void onChangeMonth(int month, int year) {
        String auth = credentialsProvider.getCredentials();
        Call<ArrayList<Ride>> stats = service.getRides(auth, year, month);
        stats.enqueue(listener);
    }
}
