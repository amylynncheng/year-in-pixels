package melochi.com.yearinpixels;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import melochi.com.yearinpixels.constants.CalendarConstants;

public class CalendarActivity extends FragmentActivity {
    private static final String TAG = "CalendarActivity";
    private TextView mDateTextView;
    private ImageView mPrevButton;
    private ImageView mNextButton;

    private Calendar currentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        currentMonth = Calendar.getInstance();

        mDateTextView = findViewById(R.id.calendar_date_display);
        setMonthTitle(currentMonth);
        mPrevButton = findViewById(R.id.calendar_prev_button);
        mNextButton = findViewById(R.id.calendar_next_button);
        assignListeners();

        // initialize the calendar grid view for the current month
        FragmentManager manager = getSupportFragmentManager();
        Fragment calendarFragment = manager.findFragmentById(R.id.fragment_container);
        if (calendarFragment == null) {
            calendarFragment = CalendarFragment.newInstance(currentMonth);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, calendarFragment)
                    .commit();
        }
    }

    private void replaceCalendarFragment(Calendar calendar) {
        Fragment calendarFragment = CalendarFragment.newInstance(calendar);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, calendarFragment)
                .commit();
    }

    private void assignListeners() {
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // re-enable the next button if we previously disabled it
                if (mNextButton.getVisibility() == View.INVISIBLE) {
                    mNextButton.setVisibility(View.VISIBLE);
                    mNextButton.setEnabled(true);
                }

                currentMonth.add(Calendar.MONTH, -1); // go back one month
                setMonthTitle(currentMonth);
                replaceCalendarFragment(currentMonth);
                // only show calendar for the current year
                if (isAtEdgeOfYear()) {
                    view.setVisibility(View.INVISIBLE);
                    view.setEnabled(false);
                }
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // re-enable the previous button if we previously disabled it
                if (mPrevButton.getVisibility() == View.INVISIBLE) {
                    mPrevButton.setVisibility(View.VISIBLE);
                    mPrevButton.setEnabled(true);
                }

                currentMonth.add(Calendar.MONTH, 1); // go forward one month
                setMonthTitle(currentMonth);
                replaceCalendarFragment(currentMonth);
                // only show calendar for the current year
                if (isAtEdgeOfYear()) {
                    view.setVisibility(View.INVISIBLE);
                    view.setEnabled(false);
                }
            }
        });
    }

    private boolean isAtEdgeOfYear() {
        return currentMonth.get(Calendar.MONTH) == 0 || currentMonth.get(Calendar.MONTH) == 11;
    }

    private void setMonthTitle(Calendar calendar) {
        mDateTextView.setText(CalendarConstants.MONTH_NAMES[calendar.get(Calendar.MONTH)]);
    }
}

/**
options:
    1. keep list of array lists with empty PixelDays first
        move prev/next buttons to Activity instead of Fragment;
        pass the list associated with the current month to fragment
        fragment displays all data of that list
        if a cell is updated,
    2. change to recycler view
    3. use grid only
*/