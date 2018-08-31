package pl.kompu.helikopteremposlasku.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.kompu.helikopteremposlasku.model.User;

/**
 * Created by Kompu on 2015-09-25.
 */
public class BaseFragment extends Fragment {

    public static String EXTRA_USERS = "users";

    protected ArrayList<User> userList;
    protected Map<Long, User> users;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userList = getArguments().getParcelableArrayList(EXTRA_USERS);
            users = new HashMap<>();
            for (User user : userList) {
                this.users.put(user.id, user);
            }
        }
    }
}
