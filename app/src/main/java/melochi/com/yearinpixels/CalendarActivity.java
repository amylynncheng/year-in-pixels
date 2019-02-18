package melochi.com.yearinpixels;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

public class CalendarActivity extends FragmentActivity {
    private static final String TAG = "CalendarActivity";
    private FrameLayout mContainer;

    String MONTHS[] = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September",
            "October", "November", "December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mContainer = findViewById(R.id.fragment_container);

        if (mContainer != null) {
            // do not do anything if restoring from previous state
            // (do not override existing fragment)
            if (savedInstanceState != null) {
                return;
            }

            CalendarFragment calendarFragment = new CalendarFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, calendarFragment).commit();
        }
    }
}
