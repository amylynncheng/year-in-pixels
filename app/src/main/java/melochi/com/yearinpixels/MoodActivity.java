package melochi.com.yearinpixels;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import melochi.com.yearinpixels.constants.CalendarConstants;
import melochi.com.yearinpixels.constants.Extras;

public class MoodActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        Bundle b = getIntent().getExtras();
        try {
            Date selectedDate = (Date) b.get(Extras.DATE_SELECTED_EXTRA_KEY);
            setDateTitle(selectedDate);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }


    }

    private void setDateTitle(Date selectedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        String dateTitle = CalendarConstants.MONTH_NAMES[calendar.get(Calendar.MONTH)] + " "
                + calendar.get(Calendar.DATE) + ", "
                + calendar.get(Calendar.YEAR);
        TextView mDateTitle = findViewById(R.id.date_title);
        mDateTitle.setText(dateTitle);
    }
}
