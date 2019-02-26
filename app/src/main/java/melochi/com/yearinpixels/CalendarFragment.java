package melochi.com.yearinpixels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import melochi.com.yearinpixels.constants.Extras;

public class CalendarFragment extends Fragment {
    private static final String TAG = CalendarFragment.class.getSimpleName();
    private static final int MOOD_ACTIVITY_REQUEST_CODE = 1;

    private GridView mGrid;

    private CalendarAdapter mCalendarAdapter;
    private Calendar currentDate;
    private static final int MAX_NUM_CELLS = 42;

    public static CalendarFragment newInstance(Calendar calendar) {
        Bundle args = new Bundle();
        args.putSerializable(Extras.CURRENT_DATE_CALENDAR_EXTRA_KEY, calendar);

        CalendarFragment fragment = new CalendarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the current date from fragment arguments
        assert getArguments() != null;
        currentDate = (Calendar) getArguments()
                .getSerializable(Extras.CURRENT_DATE_CALENDAR_EXTRA_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_component, container, false);
        mGrid = view.findViewById(R.id.calendar_grid);
        assignListeners();
        populateCalendar();
        return view;
    }

    private void assignListeners() {
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View cell, int position, long id) {
                Intent i = new Intent(getActivity(), MoodActivity.class);
                // TODO: remove passing position if redundant
                i.putExtra(Extras.CELL_POSITION_EXTRA, position);
                i.putExtra(Extras.PIXEL_SELECTED_EXTRA_KEY, mCalendarAdapter.getItem(position));
                startActivityForResult(i, MOOD_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MOOD_ACTIVITY_REQUEST_CODE) {
            // TODO: replace debugging toasts
            if (resultCode == Activity.RESULT_OK) {
                PixelDay pixelDay = (PixelDay) data.getSerializableExtra(
                        Extras.RESULT_PIXEL_DAY_EXTRA_KEY);
                updateCellColor(pixelDay);
            } else { // cancelled
                Toast.makeText(getActivity(), "cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateCellColor(PixelDay pixel) {
        View updatedCell = mGrid.getChildAt(pixel.getPosition());
        updatedCell.setBackgroundColor(getResources().getColor(pixel.getColor()));
    }

    public void populateCalendar() {
        ArrayList<PixelDay> cells = new ArrayList<>();
        Calendar calender = (Calendar) currentDate.clone();
        int currentMonth = currentDate.getTime().getMonth();

        // start at the first day of this month
        calender.set(Calendar.DAY_OF_MONTH, 1);
        // determine the cell for first day of the current month based on what day of the week it is
        // note: get(DAY_OF_WEEK) returns 1-7 for Sunday-Saturday
        int startCell = calender.get(Calendar.DAY_OF_WEEK) - 1;

        // start from x number of days BEFORE the start of the current month
        // purpose: fill empty cells with previous month's dates
        calender.add(Calendar.DAY_OF_MONTH, -startCell);

        int position = 0;
        Date nextDate = calender.getTime();
        while (cells.size() < MAX_NUM_CELLS && !isMonthFilled(nextDate, currentMonth, cells.size())) {
            cells.add(new PixelDay(nextDate, position));
            position++;
            // increment by one day
            calender.add(Calendar.DAY_OF_MONTH, 1);
            nextDate = calender.getTime();
        }
        // fill grid
        mCalendarAdapter = new CalendarAdapter(getContext(), cells);
        mGrid.setAdapter(mCalendarAdapter);
    }

    private boolean isMonthFilled(Date date, int month, int numFilled) {
        return date.getMonth() != month && numFilled != 0 && numFilled % 7 == 0;
    }

    private class CalendarAdapter extends ArrayAdapter<PixelDay> {
        private LayoutInflater inflater;
        private Date today;

        CalendarAdapter(Context context, ArrayList<PixelDay> days) {
            super(context, R.layout.calendar_day, days);
            inflater = LayoutInflater.from(context);
            today = new Date();
        }

        /**
         * GridView uses an Adapter to create the views and recycles views when they go offscreen
         * and asks the Adapter to reuse the recycled views for new views that come on screen.
         * GridView will call getView(...) on the Adapter each time it wants to display a child view
         * on screen.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PixelDay pixel = getItem(position);
            Date date = pixel.getDate();

            // inflate item if it does not exist
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calendar_day, parent, false);
            }

            TextView dateTextView = (TextView) convertView;

            if (date.getMonth() != currentDate.getTime().getMonth()) {
                // grey out the dates outside of the current month
                dateTextView.setTextColor(getResources().getColor(R.color.greyOut));
            } else if (date.getMonth() == today.getMonth() && date.getDate() == today.getDate()){
                // set today's date to blue and bold the font
                dateTextView.setTypeface(null, Typeface.BOLD);
                dateTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            dateTextView.setText(String.valueOf(date.getDate()));
            return convertView;
        }
    }
}
