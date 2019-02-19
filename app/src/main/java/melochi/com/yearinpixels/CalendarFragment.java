package melochi.com.yearinpixels;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private static final int NUM_CELLS = 42;

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
        // get the current date
        currentDate = Calendar.getInstance();

        populateCalendar();
        return view;
    }

    public void populateCalendar() {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calender = (Calendar) currentDate.clone();
        // start at the first day of this month
        calender.set(Calendar.DAY_OF_MONTH, 1);
        // determine the cell for first day of the current month based on what day of the week it is
        // note: get(DAY_OF_WEEK) returns 1-7 for Sunday-Saturday
        int startCell = calender.get(Calendar.DAY_OF_WEEK) - 1;
        Log.d(TAG, "day of the week of the first day: " + calender.get(Calendar.DAY_OF_WEEK));

        // start from x number of days BEFORE the start of the current month
        // purpose: fill empty cells with previous month's dates
        calender.add(Calendar.DAY_OF_MONTH, -startCell);

        while (cells.size() < NUM_CELLS) {
            // getTime() returns a Date object
            cells.add(calender.getTime());
            // increment by one day
            calender.add(Calendar.DAY_OF_MONTH, 1);
        }
        // fill grid
        mGrid.setAdapter(new CalendarAdapter(getContext(), cells));
        // set title
        mDateTextView.setText(MONTH_NAMES[currentDate.get(Calendar.MONTH)]);
    }

    private class CalendarAdapter extends ArrayAdapter<Date> {
        private LayoutInflater inflater;

        public CalendarAdapter(Context context, ArrayList<Date> days) {
            super(context, R.layout.calendar_day, days);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Date date = getItem(position);

            // inflate item if it does not exist
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calendar_day, parent, false);
            }

            ((TextView) convertView).setText(String.valueOf(date.getDate()));
            Log.d(TAG, "getDate of element in cells list: " + date.getDate());
            return convertView;
        }
    }
}
