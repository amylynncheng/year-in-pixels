package melochi.com.yearinpixels;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;

public class CalendarActivity extends AppCompatActivity {
    private static final String TAG = "CalendarActivity";

    private CalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mCalendarView = (CalendarView) findViewById(R.id.calendar_view);
        mCalendarView.setOnDateChangeListener(new SelectDateListener());
    }

    private class SelectDateListener implements CalendarView.OnDateChangeListener {
        @Override
        public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
            Log.d(TAG, month + "/" + day + "/" + year);
        }
    }
}
