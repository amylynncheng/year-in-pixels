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
    private static final String[] MOOD_STRINGS = {"Terrible", "Bad", "Okay", "Good", "Great"};
    private static final int[] MOOD_ICONS = {
            R.drawable.ic_mood_terrible_56dp, R.drawable.ic_mood_bad_56dp, R.drawable.ic_mood_okay_56dp,
            R.drawable.ic_mood_good_56dp, R.drawable.ic_mood_great_56dp};

    public PixelDay(Date date, int position) {
        this.date = date;
        this.position = position;
        this.moodRating = -1;
        this.dayDescription = "";
    }

    public PixelDay(Date date, int position, int moodRating, String description) {
        this.date = date;
        this.position = position;
        this.moodRating = moodRating;
        this.dayDescription = description;
    }

    public boolean isEmpty() {
        return this.moodRating == -1;
    }

    public int getPosition() {
        return position;
    }

    public Date getDate() {
        return date;
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

    public String getDateString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dateString = CalendarConstants.MONTH_NAMES[calendar.get(Calendar.MONTH)] + " "
                + calendar.get(Calendar.DATE) + ", "
                + calendar.get(Calendar.YEAR);
        return dateString;
    }

    public String getMoodString() {
        if (this.moodRating < 0 || this.moodRating >= MOOD_COLORS.length) {
            return ""; // this should never occur
        }
        return MOOD_STRINGS[this.moodRating];
    }

    public int getMoodIcon() {
        if (this.moodRating < 0 || this.moodRating >= MOOD_COLORS.length) {
            return R.drawable.ic_mood_okay_56dp; // this should never occur
        }
        return MOOD_ICONS[this.moodRating];
    }

    public String toString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String string = getDateString() + ": "
                + "POSITION = " + position + ", "
                + "MOOD = " + moodRating + ", "
                + "DESCRIPTION = " + dayDescription;
        return string;
    }
}
