package pl.kompu.helikopteremposlasku.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.kompu.helikopteremposlasku.activities.RidesActivity;
import pl.kompu.helikopteremposlasku.view.PassengerAllCheckBox;
import pl.kompu.helikopteremposlasku.adapters.RidePassengersAdapter;
import pl.kompu.helikopteremposlasku.model.Ride;
import pl.kompu.helikopteremposlasku.model.User;
import pl.kompu.helikopteremposlasku.R;
import pl.kompu.helikopteremposlasku.listeners.PassengerCheckBoxListener;
import pl.kompu.helikopteremposlasku.view.StatExpandCard;

/**
 * Created by Kompu on 2015-09-27.
 */
public class RideFragment extends BaseFragment implements MaterialDialog.ListCallbackSingleChoice {

    public static String EXTRA_RIDE = "ride";
    public static String SAVED_RIDE = "saved_ride";

    private static int lastId = 0;
    private int fragmentId;

    @Bind(R.id.driver_change_button) Button driverChange;
    @Bind(R.id.list_view) ListView listView;
    @Bind(R.id.checkbox_once) CheckBox onceCheckBox;
    @Bind(R.id.checkbox_twice) CheckBox twiceCheckBox;
    private RidePassengersAdapter listAdapter;

    private String[] userNames;
    private Ride ride;
    private int driverPosition;

    public static RideFragment newInstance(ArrayList<User> users, Ride ride) {
        RideFragment fragment = new RideFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_USERS, users);
        args.putParcelable(EXTRA_RIDE, ride);
        fragment.setArguments(args);
        return fragment;
    }

    public RideFragment() {
        fragmentId = lastId++;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ride = getArguments().getParcelable(EXTRA_RIDE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride, container, false);
        ButterKnife.bind(this, view);

        Ride stateRide;
        if (savedInstanceState != null) {
            stateRide = savedInstanceState.getParcelable(SAVED_RIDE);
        } else {
            stateRide = ride;
        }
        userNames = new String[userList.size()];
        driverPosition = 0;
        int index = 0;
        for (User user : userList) {
            userNames[index] = user.name;
            if (user.id == stateRide.driver) {
                driverPosition = index;
            }
            ++index;
        }

        driverChange.setText(userNames[driverPosition]);

        PassengerCheckBoxListener checkBoxListener = (PassengerCheckBoxListener) getActivity();
        listAdapter = new RidePassengersAdapter(getContext(), userList, stateRide.passengers, checkBoxListener);
        listView.setAdapter(listAdapter);
        StatExpandCard.setListViewHeightBasedOnItems(listView);

        PassengerAllCheckBox checkBoxAdapter = new PassengerAllCheckBox(onceCheckBox, twiceCheckBox, listAdapter, checkBoxListener);
        onceCheckBox.setOnCheckedChangeListener(checkBoxAdapter);
        twiceCheckBox.setOnCheckedChangeListener(checkBoxAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_RIDE, getRideFromState());
    }

    @Override
    public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
        driverPosition = i;
        driverChange.setText(userNames[driverPosition]);

        RidesActivity ridesActivity = (RidesActivity) getActivity();
        ridesActivity.tabNameChanged(this, userNames[driverPosition]);

        PassengerCheckBoxListener listener = (PassengerCheckBoxListener) getActivity();
        listener.checkChanged();

        return true;
    }

    @OnClick(R.id.driver_change_button)
    public void changeDriver() {
        new MaterialDialog.Builder(getContext())
                .title(R.string.dialog_chose_driver)
                .items(userNames)
                .itemsCallbackSingleChoice(driverPosition, this)
                .show();
    }

    public Ride getRideFromState() {
        Ride stateRide = new Ride(userList.get(driverPosition).id, ride.date);
        stateRide.id = ride.id;
        stateRide.passengers = listAdapter.getPassengers();
        return stateRide;
    }
}
