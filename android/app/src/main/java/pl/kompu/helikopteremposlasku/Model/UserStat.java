package pl.kompu.helikopteremposlasku.model;

import java.util.List;

/**
 * Created by Kompu on 2015-09-24.
 */
public class UserStat {
    public long id;
    public int driver;
    public List<UserPoint> points;

    public int pointsSummary() {
        int result = 0;
        for (UserPoint it : points) {
            result += it.points;
        }
        return result;
    }
}
