package melochi.com.yearinpixels;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import melochi.com.yearinpixels.constants.CalendarConstants;
import melochi.com.yearinpixels.constants.Extras;

public class CalendarActivity extends AppCompatActivity
        implements CalendarFragment.OnPixelDaySavedListener {
    private static final String TAG = "CalendarActivity";
    private TextView mDateTextView;
    private ImageView mPrevButton;
    private ImageView mNextButton;

    private Calendar currentMonth;
    public List<List<PixelDay>> pixelsPerMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        loadData();
        currentMonth = Calendar.getInstance();

        mDateTextView = findViewById(R.id.calendar_date_display);
        setMonthTitle(currentMonth);
        mPrevButton = findViewById(R.id.calendar_prev_button);
        mNextButton = findViewById(R.id.calendar_next_button);
        assignListeners();

        // initialize the calendar grid view for the current month
        FragmentManager manager = getSupportFragmentManager();
        Fragment calendarFragment = manager.findFragmentById(R.id.calendar_fragment_container);
        if (calendarFragment == null) {
            calendarFragment = CalendarFragment.newInstance(getCellsForCurrentMonth());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.calendar_fragment_container, calendarFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_settings) {
            Intent i = new Intent(CalendarActivity.this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return false;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof CalendarFragment) {
            CalendarFragment calendarFragment = (CalendarFragment) fragment;
            calendarFragment.setOnPixelDaySavedListener(this);
        }
    }

    private void replaceCalendarFragment(Calendar calendar) {
        Fragment calendarFragment = CalendarFragment.newInstance(getCellsForCurrentMonth());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.calendar_fragment_container, calendarFragment)
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

    private List<List<PixelDay>> initializePixelsList() {
        List<List<PixelDay>> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add(fillPixelsForMonth(i));
        }
        return list;
    }

    private List<PixelDay> fillPixelsForMonth(int monthIndex) {
        ArrayList<PixelDay> cells = new ArrayList<>();
        Calendar calender = (Calendar) currentMonth.clone();
        // start at the first day of the given month
        calender.set(Calendar.MONTH, monthIndex);
        calender.set(Calendar.DAY_OF_MONTH, 1);

        int position = 0;
        int offset = calender.get(Calendar.DAY_OF_WEEK) - 1;
        // add empty cells until we reach the position of the first day of this month
        while (position < offset) {
            cells.add(null);
            position++;
        }
        Date nextDate = calender.getTime();
        int daysInMonth = CalendarConstants.MONTH_LENGTHS[monthIndex];
        while (cells.size() < daysInMonth + offset) {
            cells.add(new PixelDay(nextDate, position));
            // increment by one day
            calender.add(Calendar.DAY_OF_MONTH, 1);
            nextDate = calender.getTime();
            position++;
        }
        return cells;
    }

    private List<PixelDay> getCellsForCurrentMonth(){
        return pixelsPerMonth.get(currentMonth.get(Calendar.MONTH));
    }

    @Override
    public void onPixelDaySaved(PixelDay pixelDay) {
        int month = pixelDay.getDate().getMonth();
        pixelsPerMonth.get(month).set(pixelDay.getPosition(), pixelDay);
    }

    @Override
    protected void onPause() {
        saveData();
        super.onPause();
    }

    private void saveData() {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        // convert the list into a JSON string so we can store it in SharedPreferences
        Gson gson = new Gson();
        String json = gson.toJson(pixelsPerMonth);
        editor.putString(Extras.PIXELS_PER_MONTH_KEY, json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString(Extras.PIXELS_PER_MONTH_KEY, null);
        if (json == null) {
            pixelsPerMonth = initializePixelsList();
        } else {
            Type type = new TypeToken<ArrayList<ArrayList<PixelDay>>>() {}.getType();
            pixelsPerMonth = gson.fromJson(json, type);
        }
    }
}