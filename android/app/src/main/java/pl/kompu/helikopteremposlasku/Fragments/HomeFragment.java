package pl.kompu.helikopteremposlasku.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.kompu.helikopteremposlasku.HelikopterApp;
import pl.kompu.helikopteremposlasku.adapters.CardStatAdapter;
import pl.kompu.helikopteremposlasku.network.CredentialsProvider;
import pl.kompu.helikopteremposlasku.network.HelikopterService;
import pl.kompu.helikopteremposlasku.model.User;
import pl.kompu.helikopteremposlasku.model.UserStat;
import pl.kompu.helikopteremposlasku.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class HomeFragment extends BaseFragment implements Callback<ArrayList<UserStat>> {

    @Inject HelikopterService service;
    @Inject CredentialsProvider credentialsProvider;
    @Bind(R.id.list_view) ListView listView;

    private ArrayList<UserStat> userStats;
    private CardStatAdapter cardArrayAdapter;

    public static HomeFragment newInstance(ArrayList<User> users) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_USERS, users);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((HelikopterApp) getActivity().getApplication()).getHelikopterCompnent().inject(this);

        String auth = credentialsProvider.getCredentials();
        Call<ArrayList<UserStat>> stats = service.getStats(auth);
        stats.enqueue(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onResponse(Response<ArrayList<UserStat>> response) {
        if (response.code() == 200) {
            userStats = response.body();
            Collections.sort(userStats, new Comparator<UserStat>() {
                @Override
                public int compare(UserStat first, UserStat second) {
                    String firstName = users.get(first.id).name;
                    String secondName = users.get(second.id).name;
                    return firstName.compareTo(secondName);
                }
            });
            cardArrayAdapter = new CardStatAdapter(getContext(), users, userStats);
            listView.setAdapter(cardArrayAdapter);
        } else if (response.code() == 401) {
            onFailure(new Throwable(response.message()));
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
