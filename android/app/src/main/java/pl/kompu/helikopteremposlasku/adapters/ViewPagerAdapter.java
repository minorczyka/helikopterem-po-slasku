package pl.kompu.helikopteremposlasku.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.Date;

import pl.kompu.helikopteremposlasku.fragments.RideFragment;
import pl.kompu.helikopteremposlasku.model.Ride;
import pl.kompu.helikopteremposlasku.model.User;

/**
 * Created by Kompu on 2015-09-27.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<RideFragment> fragmentList = new ArrayList<>();
    private final ArrayList<String> fragmentTitleList = new ArrayList<>();

    private ArrayList<User> users;
    private Date date;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<User> users, ArrayList<Ride> rides, Date date) {
        super(fm);
        this.users = users;
        this.date = date;

        if (rides != null) {
            for (Ride ride : rides) {
                insertFragment(ride);
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return fragmentList.get(position).getFragmentId();
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        int index = fragmentList.indexOf(object);
        if (index != -1) {
            return index;
        } else {
            return PagerAdapter.POSITION_NONE;
        }
    }

    private void insertFragment(Ride ride) {
        User driver = null;
        for (User user : users) {
            if (user.id == ride.driver) {
                driver = user;
                break;
            }
        }

        fragmentList.add(RideFragment.newInstance(users, ride));
        fragmentTitleList.add(driver.name);
    }

    public void addFragment() {
        Ride ride = new Ride(users.get(0).id, date);
        insertFragment(ride);
        notifyDataSetChanged();
    }

    public void removeFragment(int position) {
        if (getCount() > position) {
            fragmentList.remove(position);
            fragmentTitleList.remove(position);
            notifyDataSetChanged();
        }
    }

    public int tabNameChanged(Fragment fragment, String name) {
        int i = 0;
        for (Fragment it : fragmentList) {
            if (it == fragment) {
                if (!fragmentTitleList.get(i).equals(name)) {
                    fragmentTitleList.set(i, name);
                    return i;
                } else {
                    return -1;
                }
            }
            ++i;
        }
        return -1;
    }

    public ArrayList<Ride> getStateRides() {
        ArrayList<Ride> rides = new ArrayList<>(fragmentList.size());
        for (RideFragment it : fragmentList) {
            rides.add(it.getRideFromState());
        }
        return rides;
    }
}
