package melochi.com.yearinpixels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import melochi.com.yearinpixels.constants.Extras;

public class MoodActivity extends Activity {
    private PixelDay mSelectedPixel;
    private SmileRating mMoodRating;
    private EditText mDescriptionBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        Bundle b = getIntent().getExtras();
        try {
            mSelectedPixel = (PixelDay) b.get(Extras.PIXEL_SELECTED_EXTRA_KEY);
            TextView mDateTitle = findViewById(R.id.date_title);
            mDateTitle.setText(mSelectedPixel.getDateString());
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        mMoodRating = findViewById(R.id.mood_rating);
        mDescriptionBox = findViewById(R.id.description_edit_text);
        assignButtonListeners();
    }

    private void assignButtonListeners() {
        ImageButton mAddDescriptionButton = findViewById(R.id.writing_button);
        mAddDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
                mDescriptionBox.setVisibility(View.VISIBLE);
            }
        });

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
                if (mMoodRating.getSelectedSmile() == BaseRating.NONE) {
                    Toast.makeText(MoodActivity.this,
                            R.string.select_mood_warning,
                            Toast.LENGTH_SHORT).show();
                } else {
                    updatePixelData();
                    Intent i = new Intent(MoodActivity.this, CalendarActivity.class);
                    i.putExtra(Extras.RESULT_PIXEL_DAY_EXTRA_KEY, mSelectedPixel);
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
            }
        });
    }

    private void updatePixelData() {
        int selectedMood = mMoodRating.getSelectedSmile();
        String description = mDescriptionBox.getText().toString();
        mSelectedPixel.setMoodRating(selectedMood);
        mSelectedPixel.setDescription(description);
    }
}