package pl.kompu.helikopteremposlasku.view;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import pl.kompu.helikopteremposlasku.listeners.PassengerCheckBoxListener;

/**
 * Created by Kompu on 2015-09-27.
 */
public class PassengerCheckBox implements CheckBox.OnCheckedChangeListener {

    protected CheckBox once;
    protected CheckBox twice;
    protected PassengerCheckBoxListener listener;

    public PassengerCheckBox(CheckBox once, CheckBox twice, PassengerCheckBoxListener listener) {
        this.once = once;
        this.twice = twice;
        this.listener = listener;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == once) {
            twice.setOnCheckedChangeListener(null);
            twice.setChecked(false);
            twice.setOnCheckedChangeListener(this);
        } else {
            once.setOnCheckedChangeListener(null);
            once.setChecked(false);
            once.setOnCheckedChangeListener(this);
        }

        if (listener != null) {
            listener.checkChanged();
        }
    }
}
