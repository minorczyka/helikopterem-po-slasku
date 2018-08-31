package pl.kompu.helikopteremposlasku.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.kompu.helikopteremposlasku.HelikopterApp;
import pl.kompu.helikopteremposlasku.network.CredentialsProvider;
import pl.kompu.helikopteremposlasku.network.HelikopterService;
import pl.kompu.helikopteremposlasku.adapters.ViewPagerAdapter;
import pl.kompu.helikopteremposlasku.model.PostResponse;
import pl.kompu.helikopteremposlasku.model.Ride;
import pl.kompu.helikopteremposlasku.model.User;
import pl.kompu.helikopteremposlasku.R;
import pl.kompu.helikopteremposlasku.listeners.PassengerCheckBoxListener;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class RidesActivity extends AppCompatActivity implements PassengerCheckBoxListener, Callback<PostResponse> {

    public static String EXTRA_USERS = "users";
    public static String EXTRA_RIDES = "rides";
    public static String EXTRA_DATE = "date";
    public static int PAGE_LIMIT = 10;
    public static int FIXED_TAB_LIMIT = -1;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.rootLayout) CoordinatorLayout rootLayout;
    @Bind(R.id.view_pager) ViewPager viewPager;
    @Bind(R.id.tab_layout) TabLayout tabLayout;
    @Bind(R.id.fab_add) FloatingActionButton addButton;
    @Bind(R.id.fab_save) FloatingActionButton saveButton;
    @Bind(R.id.fab_remove) FloatingActionButton removeButton;
    private ViewPagerAdapter adapter;

    @Inject HelikopterService service;
    @Inject CredentialsProvider credentialsProvider;

    private ArrayList<User> users;
    private ArrayList<Ride> rides;
    private Date date;
    private int year;
    private int month;
    private int day;
    private boolean closeAfterSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);
        ButterKnife.bind(this);
        ((HelikopterApp) getApplication()).getHelikopterCompnent().inject(this);

        Intent intent = getIntent();
        users = intent.getParcelableArrayListExtra(EXTRA_USERS);
        rides = intent.getParcelableArrayListExtra(EXTRA_RIDES);
        long dateMillis = intent.getLongExtra(EXTRA_DATE, 0);

        date = new Date(dateMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), users, rides, date);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(PAGE_LIMIT);

        tabLayout.setupWithViewPager(viewPager);
        invalidateTabMode();
        invalidateFabs();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        ArrayList<Ride> stateRides = adapter.getStateRides();
        if (stateRides.equals(rides)) {
            super.onBackPressed();
        } else {
            new MaterialDialog.Builder(this)
                    .title("You have unsaved changes")
                    .content("Do you really want to go back?")
                    .positiveText("Save")
                    .negativeText("Don't save")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            closeAfterSave = true;
                            save();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void checkChanged() {
        ArrayList<Ride> stateRides = adapter.getStateRides();
        if (stateRides.equals(rides)) {
            saveButton.hide();
        } else {
            saveButton.show();
        }
    }

    @Override
    public void onResponse(Response<PostResponse> response) {
        if (response.code() == 201) {
            if (closeAfterSave) {
                finish();
            } else {
                Snackbar.make(rootLayout, R.string.save_success, Snackbar.LENGTH_SHORT).show();
            }
        }  else if (response.code() == 401) {
            closeAfterSave = false;
            onFailure(new Throwable(response.message()));
        }
    }

    @Override
    public void onFailure(Throwable t) {
        String message = t.getMessage();
        Snackbar.make(rootLayout, message != null ? message : getString(R.string.connection_error), Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab_add)
    public void addTab() {
        adapter.addFragment();
        invalidateTabMode();
        tabLayout.setTabsFromPagerAdapter(adapter);
        viewPager.setCurrentItem(adapter.getCount() - 1, true);

        invalidateFabs();
        checkChanged();
    }

    @OnClick(R.id.fab_remove)
    public void removeTab() {
        int position = tabLayout.getSelectedTabPosition();
        adapter.removeFragment(position);
        invalidateTabMode();
        tabLayout.setTabsFromPagerAdapter(adapter);
        if (position != 0) {
            viewPager.setCurrentItem(position - 1, false);
        }

        invalidateFabs();
        checkChanged();
    }

    @OnClick(R.id.fab_save)
    public void save() {
        String auth = credentialsProvider.getCredentials();
        ArrayList<Ride> stateRides = adapter.getStateRides();
        Call<PostResponse> responseCall = service.postRides(auth, year, month, day, stateRides);
        responseCall.enqueue(this);
        rides = stateRides;

        invalidateFabs();
        checkChanged();
    }

    public void tabNameChanged(Fragment fragment, String name) {
        int i = adapter.tabNameChanged(fragment, name);
        if (i != -1) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setText(name);
            }
        }
    }

    private void invalidateTabMode() {
        if (adapter.getCount() > FIXED_TAB_LIMIT) {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
    }

    private void invalidateFabs() {
        if (adapter.getCount() >= PAGE_LIMIT) {
            addButton.hide();
        } else {
            addButton.show();
        }

        if (adapter.getCount() >  0) {
            removeButton.show();
        } else {
            removeButton.hide();
        }
    }
}
