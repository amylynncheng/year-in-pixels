package melochi.com.yearinpixels;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void showColorPickerDialog(View v) {
        Button selectedButton = (Button) v;

        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.setContentView(R.layout.color_picker_dialog);

        ColorPicker picker = dialog.findViewById(R.id.color_picker);
        SaturationBar saturationBar = dialog.findViewById(R.id.saturation_bar);
        picker.addSaturationBar(saturationBar);
        dialog.show();
    }
}
