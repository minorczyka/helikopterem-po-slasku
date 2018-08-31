package pl.kompu.helikopteremposlasku.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CellView;

import java.util.ArrayList;
import java.util.HashMap;

import hirondelle.date4j.DateTime;
import pl.kompu.helikopteremposlasku.model.Ride;
import pl.kompu.helikopteremposlasku.model.User;
import pl.kompu.helikopteremposlasku.R;
import pl.kompu.helikopteremposlasku.view.BackgroundDrawable;

/**
 * Created by Kompu on 2015-09-25.
 */
public class CaldroidCustomAdapter extends CaldroidGridAdapter {

    private static String TAG = "CaldroidCustomAdapter";

    private HashMap<Long, User> users;
    private HashMap<DateTime, ArrayList<Ride>> ridesMap;

    public CaldroidCustomAdapter(Context context, int month, int year, HashMap<String, Object> caldroidData, HashMap<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CellView cellView = (CellView)convertView;
        LayoutInflater localInflater = CaldroidFragment.getLayoutInflater(this.context, inflater, this.themeResource);
        if(convertView == null) {
            cellView = (CellView)localInflater.inflate(com.caldroid.R.layout.square_date_cell, null);
        }

        this.customizeTextView(position, cellView);

        DateTime dateTime = (DateTime)this.datetimeList.get(position);
        dateTime = dateTime.truncate(DateTime.Unit.DAY);
        if (ridesMap != null && ridesMap.containsKey(dateTime)) {
            ArrayList<Ride> rides = ridesMap.get(dateTime);
            ArrayList<Integer> colors = new ArrayList<>(rides.size());
            for (Ride ride : rides) {
                User user = users.get(ride.driver);
                String color = user.color.backgroundColor;
                colors.add(Color.parseColor(color));
            }

            Drawable background = new BackgroundDrawable(colors);
            if (this.month != dateTime.getMonth()) {
                background.setAlpha(127);
            }
            cellView.setBackground(background);
            cellView.setTextColor(context.getResources().getColor(R.color.white));
//            cellView.setTextColor(color);
        }

        return cellView;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = new HashMap<>(users.size());
        for (User user : users) {
            this.users.put(user.id, user);
        }
    }

    public void setRidesMap(HashMap<DateTime, ArrayList<Ride>> ridesMap) {
        this.ridesMap = ridesMap;
    }
}
