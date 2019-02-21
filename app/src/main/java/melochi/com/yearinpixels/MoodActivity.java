package melochi.com.yearinpixels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import melochi.com.yearinpixels.constants.CalendarConstants;
import melochi.com.yearinpixels.constants.Extras;

public class MoodActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        Bundle b = getIntent().getExtras();
        try {
            Date selectedDate = (Date) b.get(Extras.DATE_SELECTED_EXTRA_KEY);
            setDateTitle(selectedDate);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        assignButtonListeners();
    }

    private void setDateTitle(Date selectedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        String dateTitle = CalendarConstants.MONTH_NAMES[calendar.get(Calendar.MONTH)] + " "
                + calendar.get(Calendar.DATE) + ", "
                + calendar.get(Calendar.YEAR);
        TextView mDateTitle = findViewById(R.id.date_title);
        mDateTitle.setText(dateTitle);
    }

    private void assignButtonListeners() {
        Button mCancelButton = findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MoodActivity.this, CalendarActivity.class);
                setResult(Activity.RESULT_CANCELED, i);
                finish();
            }
        });

        Button mSaveButton = findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MoodActivity.this, CalendarActivity.class);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });
    }
}
