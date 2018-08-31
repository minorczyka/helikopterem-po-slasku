package pl.kompu.helikopteremposlasku.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import pl.kompu.helikopteremposlasku.R;
import pl.kompu.helikopteremposlasku.model.HistoryEntry;
import pl.kompu.helikopteremposlasku.model.HistoryList;
import pl.kompu.helikopteremposlasku.model.User;
import pl.kompu.helikopteremposlasku.utils.StringDateFormatter;

/**
 * Created by Kompu on 2016-04-02.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    @BindString(R.string.history_add) String addedMessage;
    @BindString(R.string.history_remove) String removedMessage;
    @BindString(R.string.history_modify) String modifiedMessage;

    private Map<Long, User> users;
    private List<HistoryEntry> entries = new ArrayList<>();
    private StringDateFormatter formatter = new StringDateFormatter();

    public HistoryAdapter(Map<Long, User> users, View view) {
        this.users = users;
        ButterKnife.bind(this, view);
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_row, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        HistoryEntry history = entries.get(position);
        String message;
        switch (history.event) {
            case 0:
                message = addedMessage;
                holder.layout.setBackgroundColor(0xFFDFF0D8);
                break;
            case 1:
                message = removedMessage;
                holder.layout.setBackgroundColor(0xFFF2DEDE);
                break;
            case 2:
                message = modifiedMessage;
                holder.layout.setBackgroundColor(0xFFD9EDF7);
                break;
            default:
                message = "";
                break;
        }
        holder.message.setText(String.format(message, users.get(history.person).name));
        holder.relatedDate.setText(history.relatedDate);
        holder.creationDate.setText(formatter.formatDate(history.historyDate));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public void appendHistoryList(HistoryList historyList) {
        entries.addAll(historyList.results);
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.history_layout) ViewGroup layout;
        @Bind(R.id.history_message) TextView message;
        @Bind(R.id.history_related_date) TextView relatedDate;
        @Bind(R.id.history_creation_date) TextView creationDate;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
