package pl.kompu.helikopteremposlasku.view;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import pl.kompu.helikopteremposlasku.adapters.RidePassengersAdapter;
import pl.kompu.helikopteremposlasku.listeners.PassengerCheckBoxListener;

/**
 * Created by Kompu on 2015-09-27.
 */
public class PassengerAllCheckBox extends PassengerCheckBox {

    private RidePassengersAdapter listAdapter;

    public PassengerAllCheckBox(CheckBox once, CheckBox twice, RidePassengersAdapter listAdapter, PassengerCheckBoxListener listener) {
        super(once, twice, listener);
        this.listAdapter = listAdapter;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        super.onCheckedChanged(compoundButton, b);
        if (compoundButton == once) {
            listAdapter.changeChecked(true, once.isChecked());
        } else {
            listAdapter.changeChecked(false, twice.isChecked());
        }
    }
}
