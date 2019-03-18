package melochi.com.yearinpixels;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;

import melochi.com.yearinpixels.constants.ColorConstants;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void showColorPickerDialog(View v) {
        ImageButton selectedButton = (ImageButton) v;

        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.setContentView(R.layout.color_picker_dialog);

        ColorPicker picker = dialog.findViewById(R.id.color_picker);
        SaturationBar saturationBar = dialog.findViewById(R.id.saturation_bar);
        picker.addSaturationBar(saturationBar);

        Button saveButton = dialog.findViewById(R.id.save_color_button);
        saveButton.setOnClickListener(new ColorSavedListener(selectedButton, picker, dialog));
        dialog.show();
    }

    private class ColorSavedListener implements View.OnClickListener {
        private ImageButton selectedButton;
        private ColorPicker colorPicker;
        private Dialog dialog;

        public ColorSavedListener(ImageButton button, ColorPicker picker, Dialog dialog) {
            this.selectedButton = button;
            this.colorPicker = picker;
            this.dialog = dialog;
        }

        @Override
        public void onClick(View view) {
            int colorChosen = colorPicker.getColor();
            ColorConstants palette = ColorConstants.get();
            switch (selectedButton.getId()) {
                case (R.id.edit_great_color_button):
                    palette.setGreatColor(colorChosen);
                    break;
                case (R.id.edit_good_color_button):
                    palette.setGoodColor(colorChosen);
                    break;
                case (R.id.edit_okay_color_button):
                    palette.setOkayColor(colorChosen);
                    break;
                case (R.id.edit_bad_color_button):
                    palette.setBadColor(colorChosen);
                    break;
                case (R.id.edit_terrible_color_button):
                    palette.setTerribleColor(colorChosen);
                    break;
                default:
                    Toast.makeText(SettingsActivity.this,
                            "An error occurred",
                            Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }
}
