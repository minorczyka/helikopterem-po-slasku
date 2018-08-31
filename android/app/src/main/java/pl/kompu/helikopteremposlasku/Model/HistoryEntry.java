package pl.kompu.helikopteremposlasku.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Kompu on 2016-04-02.
 */
public class HistoryEntry {

    @SerializedName("history_date")
    public String historyDate;
    @SerializedName("related_date")
    public String relatedDate;
    public long person;
    public int event;
}
