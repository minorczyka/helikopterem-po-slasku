package pl.kompu.helikopteremposlasku.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.view.CardViewNative;
import pl.kompu.helikopteremposlasku.model.User;
import pl.kompu.helikopteremposlasku.model.UserStat;
import pl.kompu.helikopteremposlasku.R;
import pl.kompu.helikopteremposlasku.view.StatExpandCard;
import pl.kompu.helikopteremposlasku.view.SummaryCardHeader;

/**
 * Created by Kompu on 2015-09-24.
 */
public class CardStatAdapter extends BaseAdapter {

    private static String TAG = "CardStatAdapter";

    private Context context;
    private Map<Long, User> users;
    private List<UserStat> userStats;
    private List<Card> cards;

    public CardStatAdapter(Context context, Map<Long, User> users, List<UserStat> userStats) {
        this.context = context;
        this.users = users;
        this.userStats = userStats;

        cards = new ArrayList<>(userStats.size());
        for (int i = 0; i < userStats.size(); ++i) {
            cards.add(null);
        }
    }

    @Override
    public int getCount() {
        return userStats.size();
    }

    @Override
    public Object getItem(int i) {
        return userStats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CardViewNative cardView;
        ViewToClickToExpand viewToClickToExpand;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cardView = (CardViewNative) inflater.inflate(R.layout.card_stat, viewGroup, false);

        Card card = getCard(i);

        viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
        card.setViewToClickToExpand(viewToClickToExpand);

        cardView.setCard(card);
        return cardView;
    }

    private Card getCard(int i) {
        Card card = cards.get(i);
        if (card == null) {
            UserStat userStat = (UserStat) getItem(i);
            User user = users.get(userStat.id);

            card = new Card(context);

            SummaryCardHeader header = new SummaryCardHeader(context);
            header.setTitle(user.name);
            header.setPoints(Integer.toString(userStat.pointsSummary()));
            header.setColor(user.color.backgroundColor);
            card.addCardHeader(header);

            StatExpandCard expand = new StatExpandCard(context, users, userStat);
            card.addCardExpand(expand);

            cards.set(i, card);
        }
        return card;
    }
}
