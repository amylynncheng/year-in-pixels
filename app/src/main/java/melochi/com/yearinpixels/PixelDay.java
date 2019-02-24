package melochi.com.yearinpixels;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import melochi.com.yearinpixels.constants.CalendarConstants;

public class PixelDay implements Serializable {
    private Date date;
    private int position;
    private int moodRating;
    private String dayDescription;

    private static final int DEFAULT = R.color.defaultColor;
    private static final int[] MOOD_COLORS = {
            R.color.terrible, R.color.bad, R.color.average, R.color.good, R.color.great};

    public PixelDay(Date date, int position) {
        this.date = date;
        this.position = position;
    }

    public PixelDay(Date date, int position, int moodRating, String description) {
        this.date = date;
        this.position = position;
        this.moodRating = moodRating;
        this.dayDescription = description;
    }

    public int getPosition() {
        return position;
    }

    public int getMoodRating() {
        return moodRating;
    }

    public void setMoodRating(int moodRating) {
        this.moodRating = moodRating;
    }

    public String getDescription() {
        return dayDescription;
    }

    public void setDescription(String description) {
        this.dayDescription = description;
    }

    public int getColor() {
        if (this.moodRating < 0 || this.moodRating >= MOOD_COLORS.length) {
            return DEFAULT;
        }
        return MOOD_COLORS[this.moodRating];
    }

    public String toString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String string = CalendarConstants.MONTH_NAMES[calendar.get(Calendar.MONTH)] + " "
                + calendar.get(Calendar.DATE) + ", "
                + calendar.get(Calendar.YEAR) + ": "
                + "MOOD = " + moodRating + ", "
                + "DESCRIPTION = " + dayDescription;
        return string;
    }
}
