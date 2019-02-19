package melochi.com.yearinpixels;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment {
    private static final String TAG = CalendarFragment.class.getSimpleName();
    private LinearLayout mHeader;
    private ImageView mPrevButton;
    private ImageView mNextButton;
    private TextView mDateTextView;
    private GridView mGrid;

    private Calendar currentDate;
    private static final int MAX_NUM_CELLS = 42;

    String MONTH_NAMES[] = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September",
            "October", "November", "December"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_component, container, false);
        // initialize widgets
        mHeader = view.findViewById(R.id.calendar_header);
        mPrevButton = view.findViewById(R.id.calendar_prev_button);
        mNextButton = view.findViewById(R.id.calendar_next_button);
        mDateTextView = view.findViewById(R.id.calendar_date_display);
        mGrid = view.findViewById(R.id.calendar_grid);
        assignListeners();

        // get the current date
        currentDate = Calendar.getInstance();

        populateCalendar();
        return view;
    }

    private void assignListeners() {
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDate.add(Calendar.MONTH, -1); // go back one month
                populateCalendar();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDate.add(Calendar.MONTH, 1); // go forward one month
                populateCalendar();
            }
        });
    }

    public void populateCalendar() {
        ArrayList<Date> cells = new ArrayList<>();
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

        Date next = calender.getTime();
        while (cells.size() < MAX_NUM_CELLS && !isMonthFilled(next, currentMonth, cells.size())) {
            cells.add(next);
            // increment by one day
            calender.add(Calendar.DAY_OF_MONTH, 1);
            next = calender.getTime();
        }
        // fill grid
        mGrid.setAdapter(new CalendarAdapter(getContext(), cells));
        // set title
        mDateTextView.setText(MONTH_NAMES[currentDate.get(Calendar.MONTH)]);
    }

    private boolean isMonthFilled(Date date, int month, int numFilled) {
        return date.getMonth() != month && numFilled != 0 && numFilled % 7 == 0;
    }

    private class CalendarAdapter extends ArrayAdapter<Date> {
        private LayoutInflater inflater;
        private Date today;

        public CalendarAdapter(Context context, ArrayList<Date> days) {
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
            Date date = getItem(position);

            // inflate item if it does not exist
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calendar_day, parent, false);
            }

            TextView dateTextView = (TextView) convertView;

            if (date.getMonth() != today.getMonth()) {
                // grey out the dates outside of the current month
                dateTextView.setTextColor(getResources().getColor(R.color.greyOut));
            }
            if (date.getDate() == today.getDate()){
                // set today's date to blue and bold the font
                dateTextView.setTypeface(null, Typeface.BOLD);
                dateTextView.setTextColor(getResources().getColor(R.color.lightBlue));
            }

            dateTextView.setText(String.valueOf(date.getDate()));
            return convertView;
        }
    }
}
