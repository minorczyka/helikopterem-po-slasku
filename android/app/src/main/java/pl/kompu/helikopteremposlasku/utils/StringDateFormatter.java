package pl.kompu.helikopteremposlasku.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringDateFormatter {

    Pattern pattern;

    public StringDateFormatter() {
        pattern = Pattern.compile("^(\\d\\d\\d\\d-\\d\\d-\\d\\d)T(\\d\\d:\\d\\d:\\d\\d)(.*)$");
    }

    public String formatDate(String date) {
        Matcher matcher = pattern.matcher(date);
        if (matcher.matches()) {
            return matcher.group(2) + " " + matcher.group(1);
        } else {
            return date;
        }
    }
}
