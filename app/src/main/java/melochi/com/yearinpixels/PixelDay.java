package melochi.com.yearinpixels;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import melochi.com.yearinpixels.constants.CalendarConstants;

public class PixelDay implements Serializable {
    private Date date;
    private int moodRating;
    private String dayDescription;

    public PixelDay(Date date) {
        this.date = date;
    }

    public PixelDay(Date date, int moodRating, String description) {
        this.date = date;
        this.moodRating = moodRating;
        this.dayDescription = description;
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
