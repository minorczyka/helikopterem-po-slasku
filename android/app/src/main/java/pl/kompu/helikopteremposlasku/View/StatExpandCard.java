package pl.kompu.helikopteremposlasku.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Map;

import it.gmariotti.cardslib.library.internal.CardExpand;
import pl.kompu.helikopteremposlasku.adapters.CardStatInnerAdapter;
import pl.kompu.helikopteremposlasku.model.User;
import pl.kompu.helikopteremposlasku.model.UserStat;
import pl.kompu.helikopteremposlasku.R;

/**
 * Created by Kompu on 2015-09-24.
 */
public class StatExpandCard extends CardExpand {

    private CardStatInnerAdapter listAdapter;
    private UserStat userStat;

    public StatExpandCard(Context context, Map<Long, User> users, UserStat userStat) {
        super(context, R.layout.card_stat_inner);
        listAdapter = new CardStatInnerAdapter(context, users, userStat);
        this.userStat = userStat;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        if (view == null) return;

        TextView driverLabel = (TextView) view.findViewById(R.id.inner_driver_value);
        driverLabel.setText(Long.toString(userStat.driver));

        ListView innerListView = (ListView) view.findViewById(R.id.inner_list_view);
        innerListView.setAdapter(listAdapter);
        setListViewHeightBasedOnItems(innerListView);
    }

    public static void setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();

            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }
}
