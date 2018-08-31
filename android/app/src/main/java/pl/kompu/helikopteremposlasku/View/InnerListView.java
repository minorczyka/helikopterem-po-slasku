package pl.kompu.helikopteremposlasku.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Kompu on 2015-09-24.
 */
public class InnerListView extends ListView {
    public InnerListView(Context context) {
        super(context);
    }

    public InnerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
