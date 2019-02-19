package melochi.com.yearinpixels;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class CalendarActivity extends FragmentActivity {
    private static final String TAG = "CalendarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        FragmentManager manager = getSupportFragmentManager();
        Fragment calendarFragment = manager.findFragmentById(R.id.fragment_container);

        if (calendarFragment == null) {
            calendarFragment = new CalendarFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, calendarFragment)
                    .commit();
        }
    }
}
