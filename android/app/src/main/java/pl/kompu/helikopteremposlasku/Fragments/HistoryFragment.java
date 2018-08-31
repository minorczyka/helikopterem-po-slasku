package pl.kompu.helikopteremposlasku.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.kompu.helikopteremposlasku.HelikopterApp;
import pl.kompu.helikopteremposlasku.R;
import pl.kompu.helikopteremposlasku.adapters.HistoryAdapter;
import pl.kompu.helikopteremposlasku.listeners.EndlessRecyclerViewScrollListener;
import pl.kompu.helikopteremposlasku.model.HistoryList;
import pl.kompu.helikopteremposlasku.model.User;
import pl.kompu.helikopteremposlasku.network.CredentialsProvider;
import pl.kompu.helikopteremposlasku.network.HelikopterService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class HistoryFragment extends BaseFragment implements Callback<HistoryList> {

    @Inject HelikopterService service;
    @Inject CredentialsProvider credentialsProvider;

    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    private HistoryAdapter historyAdapter;

    public static HistoryFragment newInstance(ArrayList<User> users) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_USERS, users);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((HelikopterApp) getActivity().getApplication()).getHelikopterCompnent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);

        historyAdapter = new HistoryAdapter(users, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(historyAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadHistory(page);
            }
        });

        loadHistory(1);

        return view;
    }


    @Override
    public void onResponse(Response<HistoryList> response) {
        if (response.code() == 200) {
//            userStats = response.body();
//            Collections.sort(userStats, new Comparator<UserStat>() {
//                @Override
//                public int compare(UserStat first, UserStat second) {
//                    String firstName = users.get(first.id).name;
//                    String secondName = users.get(second.id).name;
//                    return firstName.compareTo(secondName);
//                }
//            });
//            cardArrayAdapter = new CardStatAdapter(getContext(), users, userStats);
//            listView.setAdapter(cardArrayAdapter);
            HistoryList historyList = response.body();

            int oldSize = historyAdapter.getItemCount();
            historyAdapter.appendHistoryList(historyList);
            historyAdapter.notifyItemRangeInserted(oldSize, historyList.results.size());
        } else if (response.code() == 401) {
            onFailure(new Throwable(response.message()));
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }

    private void loadHistory(int page) {
        String auth = credentialsProvider.getCredentials();
        Call<HistoryList> history = service.getHistoryList(auth, page);
        history.enqueue(this);
    }
}
