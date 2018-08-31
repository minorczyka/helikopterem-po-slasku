package pl.kompu.helikopteremposlasku.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Map;

import pl.kompu.helikopteremposlasku.model.User;
import pl.kompu.helikopteremposlasku.model.UserPoint;
import pl.kompu.helikopteremposlasku.model.UserStat;
import pl.kompu.helikopteremposlasku.R;

/**
 * Created by Kompu on 2015-09-24.
 */
public class CardStatInnerAdapter extends BaseAdapter {

    private Context context;
    private Map<Long, User> users;
    private UserStat userStat;

    public CardStatInnerAdapter(Context context, Map<Long, User> users, UserStat userStat) {
        this.context = context;
        this.users = users;
        this.userStat = userStat;
    }

    @Override
    public int getCount() {
        return userStat.points.size();
    }

    @Override
    public Object getItem(int i) {
        return userStat.points.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_card_inner_list, viewGroup, false);
        }

        TextView name = (TextView) view.findViewById(R.id.item_inner_name);
        TextView points = (TextView) view.findViewById(R.id.item_inner_points);

        UserPoint userPoint = (UserPoint) getItem(i);
        User user = users.get(userPoint.id);

        name.setText(user.name);
        points.setText(Integer.toString(userPoint.points));

        return view;
    }
}
