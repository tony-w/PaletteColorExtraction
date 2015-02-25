package com.tonyw.sampleapps.palettecolorextraction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Activity featuring colored text on a colored background. Colors are extracted from a Bitmap using
 * the Palette class.
 */
public class ColorWithTextActivity extends Activity {
    public static final String EXTRA_BACKGROUND_COLOR = "extra_background_color";
    public static final String EXTRA_TITLE_TEXT_COLOR = "extra_title_text_color";
    public static final String EXTRA_BODY_TEXT_COLOR = "extra_body_text_color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_with_text);

        Intent callingIntent = getIntent();
        int backgroundColor = callingIntent.getIntExtra(EXTRA_BACKGROUND_COLOR, Color.WHITE);
        int titleTextColor = callingIntent.getIntExtra(EXTRA_TITLE_TEXT_COLOR, Color.BLACK);
        int bodyTextColor = callingIntent.getIntExtra(EXTRA_BODY_TEXT_COLOR, Color.BLACK);
        findViewById(R.id.main_view).setBackgroundColor(backgroundColor);
        ((TextView) findViewById(R.id.title_text)).setTextColor(titleTextColor);
        ((TextView) findViewById(R.id.body_text)).setTextColor(bodyTextColor);
    }
}
