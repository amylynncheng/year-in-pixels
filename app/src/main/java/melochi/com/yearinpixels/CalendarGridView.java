package melochi.com.yearinpixels;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarGridView extends LinearLayout {
    private LinearLayout mHeader;
    private ImageView mPrevButton;
    private ImageView mNextButton;
    private TextView mDateTextView;
    private GridView mGrid;

    public CalendarGridView(Context context) {
        super(context);
        inflateLayout(context);
    }

    private void inflateLayout(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_component, this);
        mHeader = (LinearLayout) findViewById(R.id.calendar_header);
        mPrevButton = (ImageView) findViewById(R.id.calendar_prev_button);
        mNextButton = (ImageView) findViewById(R.id.calendar_next_button);
        mDateTextView = (TextView) findViewById(R.id.calendar_date_display);
        mGrid = (GridView) findViewById(R.id.calendar_grid);
    }
}
