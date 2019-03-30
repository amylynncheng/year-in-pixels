package melochi.com.yearinpixels.constants;

import android.graphics.Color;

/**
 * Singleton, utility class that handles setting and getting the colors used
 * for corresponding "moods" of a pixel.
 */
public class ColorConstants {
    public static final int NUM_COLORS = 5;

    // ints represent the ARBG integer of the color used by Android's Color class
    private static final int NO_COLOR = Color.parseColor("#FFFFFF");
    private static final int[] DEFAULT_MOOD_COLORS = {
            Color.parseColor("#0077BE"),
            Color.parseColor("#67F967"),
            Color.parseColor("#FFE94d"),
            Color.parseColor("#FF9932"),
            Color.parseColor("#FF3232")};
    private static int[] CUSTOM_MOOD_COLORS;
    private static int[] UNSAVED_MOOD_COLORS;

    private static ColorConstants sColors;

    private ColorConstants() {
        CUSTOM_MOOD_COLORS = DEFAULT_MOOD_COLORS;
        UNSAVED_MOOD_COLORS = CUSTOM_MOOD_COLORS;
    }

    public static ColorConstants get() {
        if (sColors == null) {
            sColors = new ColorConstants();
        }
        return sColors;
    }

    public int getDefaultColor() {
        return NO_COLOR;
    }

    public int[] getDefaultPalette() {
        return DEFAULT_MOOD_COLORS;
    }

    public int[] getPalette() {
        return CUSTOM_MOOD_COLORS;
    }

    public void setPalette(int[] colors) {
        CUSTOM_MOOD_COLORS = colors;
    }

    public void setTerribleColor(int color) {
        UNSAVED_MOOD_COLORS[0] = color;
    }

    public void setBadColor(int color) {
        UNSAVED_MOOD_COLORS[1] = color;
    }

    public void setOkayColor(int color) {
        UNSAVED_MOOD_COLORS[2] = color;
    }

    public void setGoodColor(int color) {
        UNSAVED_MOOD_COLORS[3] = color;
    }

    public void setGreatColor(int color) {
        UNSAVED_MOOD_COLORS[4] = color;
    }

    public void commitUnsavedChanges() {
        CUSTOM_MOOD_COLORS = UNSAVED_MOOD_COLORS;
    }

    public void discardUnsavedChanges() {
        UNSAVED_MOOD_COLORS = CUSTOM_MOOD_COLORS;
    }
}
