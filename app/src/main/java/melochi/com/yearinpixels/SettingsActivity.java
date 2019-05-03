package melochi.com.yearinpixels;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;

import melochi.com.yearinpixels.constants.ColorConstants;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initializeColorSquares();

        Button saveSettingsButton = findViewById(R.id.save_settings_button);
        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorConstants.get().commitUnsavedChanges();
                Intent i = new Intent(SettingsActivity.this, CalendarActivity.class);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        ColorConstants.get().discardUnsavedChanges();
        super.onBackPressed();
    }

    public void showColorPickerDialog(View v) {
        ImageButton selectedButton = (ImageButton) v;
        RelativeLayout parent = (RelativeLayout) selectedButton.getParent();

        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.setContentView(R.layout.color_picker_dialog);

        ColorPicker picker = dialog.findViewById(R.id.color_picker);
        SaturationBar saturationBar = dialog.findViewById(R.id.saturation_bar);
        picker.addSaturationBar(saturationBar);

        Button discardButton = dialog.findViewById(R.id.cancel_color_button);
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button saveButton = dialog.findViewById(R.id.save_color_button);
        saveButton.setOnClickListener(new ColorSavedListener(selectedButton, picker, dialog, parent));
        dialog.show();
    }

    private class ColorSavedListener implements View.OnClickListener {
        private ImageButton selectedButton;
        private ColorPicker colorPicker;
        private Dialog dialog;
        private RelativeLayout moodLayout;

        public ColorSavedListener(ImageButton button, ColorPicker picker, Dialog dialog, RelativeLayout parent) {
            this.selectedButton = button;
            this.colorPicker = picker;
            this.dialog = dialog;
            this.moodLayout = parent;
        }

        @Override
        public void onClick(View view) {
            int colorChosen = colorPicker.getColor();
            ColorConstants palette = ColorConstants.get();
            View colorSquare = null;

            switch (selectedButton.getId()) {
                case (R.id.edit_great_color_button):
                    palette.setGreatColor(colorChosen);
                    colorSquare = this.moodLayout.findViewById(R.id.great_color_square);
                    break;
                case (R.id.edit_good_color_button):
                    palette.setGoodColor(colorChosen);
                    colorSquare = this.moodLayout.findViewById(R.id.good_color_square);
                    break;
                case (R.id.edit_okay_color_button):
                    palette.setOkayColor(colorChosen);
                    colorSquare = this.moodLayout.findViewById(R.id.okay_color_square);
                    break;
                case (R.id.edit_bad_color_button):
                    palette.setBadColor(colorChosen);
                    colorSquare = this.moodLayout.findViewById(R.id.bad_color_square);
                    break;
                case (R.id.edit_terrible_color_button):
                    palette.setTerribleColor(colorChosen);
                    colorSquare = this.moodLayout.findViewById(R.id.terrible_color_square);
                    break;
                default:
                    Toast.makeText(SettingsActivity.this,
                            "An error occurred",
                            Toast.LENGTH_SHORT).show();
            }
            if (colorSquare != null) {
                colorSquare.setBackgroundColor(colorChosen);
            }
            dialog.dismiss();
        }
    }

    public void initializeColorSquares() {
        int[] currentColors = ColorConstants.get().getPalette();

        View terribleSquare = findViewById(R.id.terrible_color_square);
        terribleSquare.setBackgroundColor(currentColors[0]);
        View badSquare = findViewById(R.id.bad_color_square);
        badSquare.setBackgroundColor(currentColors[1]);
        View okaySquare = findViewById(R.id.okay_color_square);
        okaySquare.setBackgroundColor(currentColors[2]);
        View goodSquare = findViewById(R.id.good_color_square);
        goodSquare.setBackgroundColor(currentColors[3]);
        View greatSquare = findViewById(R.id.great_color_square);
        greatSquare.setBackgroundColor(currentColors[4]);
    }
}
