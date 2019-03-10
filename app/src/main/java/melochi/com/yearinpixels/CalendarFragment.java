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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import melochi.com.yearinpixels.constants.Extras;

public class CalendarFragment extends Fragment {
    private static final String TAG = CalendarFragment.class.getSimpleName();
    private static final int MOOD_ACTIVITY_REQUEST_CODE = 1;

    private GridView mGrid;

    private ArrayList<PixelDay> cells;
    private CalendarAdapter mCalendarAdapter;
    private OnPixelDaySavedListener mCallback;

    public static CalendarFragment newInstance(List<PixelDay> cells) {
        Bundle args = new Bundle();
        args.putSerializable(Extras.MONTH_CELL_LIST_EXTRA_KEY, (ArrayList) cells);
        CalendarFragment fragment = new CalendarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the current date from fragment arguments
        assert getArguments() != null;
        cells = (ArrayList<PixelDay>) getArguments().getSerializable(Extras.MONTH_CELL_LIST_EXTRA_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        mGrid = view.findViewById(R.id.calendar_grid);
        assignListeners();
        // fill grid
        mCalendarAdapter = new CalendarAdapter(getContext(), cells);
        mGrid.setAdapter(mCalendarAdapter);
        return view;
    }

    private void assignListeners() {
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View cell, int position, long id) {
                PixelDay selectedPixelDay = mCalendarAdapter.getItem(position);
                // if the selected date has not been filled out before, start the blank edit activity
                if (selectedPixelDay == null || selectedPixelDay.isEmpty()) {
                    Intent i = new Intent(getActivity(), MoodActivity.class);
                    i.putExtra(Extras.PIXEL_SELECTED_EXTRA_KEY, selectedPixelDay);
                    startActivityForResult(i, MOOD_ACTIVITY_REQUEST_CODE);
                } else { // the selected date has already been edited -- show the current state
                    displaySavedDay(selectedPixelDay);
                }
            }
        });
    }

    private void displaySavedDay(PixelDay selectedPixelDay) {
        Activity activity = getActivity();
        FrameLayout reviewLayout = activity.findViewById(R.id.review_container);
        // clear any existing views before setting new ones
        reviewLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.fragment_review, reviewLayout);
        // set the corresponding views to this PixelDay's attributes
        TextView dateTextView = reviewLayout.findViewById(R.id.saved_date_header);
        dateTextView.setText(selectedPixelDay.getDateString());
        TextView moodTextView = reviewLayout.findViewById(R.id.saved_mood_rating);
        moodTextView.setText(selectedPixelDay.getMoodString());
        TextView description = reviewLayout.findViewById(R.id.saved_description);
        description.setText(selectedPixelDay.getDescription());
    }

    public void setOnPixelDaySavedListener(OnPixelDaySavedListener activity) {
        mCallback = activity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MOOD_ACTIVITY_REQUEST_CODE) {
            // TODO: replace debugging toasts
            if (resultCode == Activity.RESULT_OK) {
                PixelDay pixelDay = (PixelDay) data.getSerializableExtra(
                        Extras.RESULT_PIXEL_DAY_EXTRA_KEY);
                updateCellColor(pixelDay);
                mCallback.onPixelDaySaved(pixelDay);
                //updateCellInList(pixelDay);
            } else { // cancelled
                Toast.makeText(getActivity(), "cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateCellColor(PixelDay pixel) {
        View updatedCell = mGrid.getChildAt(pixel.getPosition());
        updatedCell.setBackgroundColor(getResources().getColor(pixel.getColor()));
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
            // inflate item if it does not exist
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calendar_day, parent, false);
            }
            TextView dateTextView = (TextView) convertView;

            PixelDay pixel = getItem(position);
            if (pixel == null) {
                // this cell is not a part of the current month, leave it as empty
                return convertView;
            }

            Date date = pixel.getDate();
            if (date.getMonth() == today.getMonth() && date.getDate() == today.getDate()){
                // set today's date to blue and bold the font
                dateTextView.setTypeface(null, Typeface.BOLD);
                dateTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            dateTextView.setText(String.valueOf(date.getDate()));
            dateTextView.setBackgroundColor(getResources().getColor(pixel.getColor()));
            return convertView;
        }
    }

    /**
     * Allow this fragment to deliver messages to its host activity by calling the onPixelDaySaved()
     * method using the mCallback instance of the OnPixelDaySavedListener interface.
     * (Container Activity must implement this interface)
     */
    public interface OnPixelDaySavedListener {
        void onPixelDaySaved(PixelDay pixelDay);
    }
}
