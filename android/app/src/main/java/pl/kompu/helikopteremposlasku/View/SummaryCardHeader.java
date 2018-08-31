package pl.kompu.helikopteremposlasku.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.CardHeader;
import pl.kompu.helikopteremposlasku.R;

/**
 * Created by Kompu on 2015-10-05.
 */
public class SummaryCardHeader extends CardHeader {

    private String points;
    private Integer color;

    public SummaryCardHeader(Context context) {
        super(context, R.layout.card_stat_header);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);

        if (view != null) {
            TextView pointsView = (TextView) view.findViewById(R.id.card_header_points);
            if (pointsView != null && points != null) {
                pointsView.setText(points);
            }

            View colorView = view.findViewById(R.id.card_header_color);
            if (colorView != null && color != null) {
                ArrayList<Integer> colors = new ArrayList<>(1);
                colors.add(color);
                BackgroundDrawable drawable = new BackgroundDrawable(colors);
                colorView.setBackground(drawable);
            }
        }
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public void setColor(String color) {
        this.color = Color.parseColor(color);
    }
}
