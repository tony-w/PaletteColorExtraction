package com.tonyw.sampleapps.palettecolorextraction;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.Toast;

/**
 * An AsyncTask dedicated to extracting prominent colors from bitmaps and updating them on a card.
 */
public class ExtractPaletteColorsAsyncTask extends AsyncTask<Bitmap, Void, Palette> {
    private Context mContext;
    private View mCardView;

    public ExtractPaletteColorsAsyncTask(Context context, View cardView) {
        mContext = context;
        mCardView = cardView;
    }

    @Override
    protected Palette doInBackground(Bitmap... bitmaps) {
        return Palette.generate(bitmaps[0]);
    }

    @Override
    protected void onPostExecute(Palette palette) {
        View vibrantView = mCardView.findViewById(R.id.vibrant);
        int vibrantColor = palette.getVibrantColor(Color.WHITE);
        getGradientDrawable(vibrantView).setColor(vibrantColor);
        boolean colorFound = palette.getVibrantSwatch() != null;
        vibrantView.setTag(colorFound ? vibrantColor : null); // For retrieval when long-clicking.
        vibrantView.setAlpha(colorFound ? 1.0f : 0.26f);
        vibrantView.setOnLongClickListener(mLongClickListener);

        View vibrantDarkView = mCardView.findViewById(R.id.vibrant_dark);
        int vibrantDarkColor = palette.getDarkVibrantColor(Color.WHITE);
        getGradientDrawable(vibrantDarkView).setColor(vibrantDarkColor);
        colorFound = palette.getDarkVibrantSwatch() != null;
        vibrantDarkView.setTag(colorFound ? vibrantDarkColor : null);
        vibrantDarkView.setAlpha(colorFound ? 1.0f : 0.26f);
        vibrantDarkView.setOnLongClickListener(mLongClickListener);

        View vibrantLightView = mCardView.findViewById(R.id.vibrant_light);
        int vibrantLightColor = palette.getLightVibrantColor(Color.WHITE);
        getGradientDrawable(vibrantLightView).setColor(vibrantLightColor);
        colorFound = palette.getLightVibrantSwatch() != null;
        vibrantLightView.setTag(colorFound ? vibrantLightColor : null);
        vibrantLightView.setAlpha(colorFound ? 1.0f : 0.26f);
        vibrantLightView.setOnLongClickListener(mLongClickListener);

        View mutedView = mCardView.findViewById(R.id.muted);
        int mutedColor = palette.getMutedColor(Color.WHITE);
        getGradientDrawable(mutedView).setColor(mutedColor);
        colorFound = palette.getMutedSwatch() != null;
        mutedView.setTag(colorFound ? mutedColor : null);
        mutedView.setAlpha(colorFound ? 1.0f : 0.26f);
        mutedView.setOnLongClickListener(mLongClickListener);

        View mutedDarkView = mCardView.findViewById(R.id.muted_dark);
        int mutedDarkColor = palette.getDarkMutedColor(Color.WHITE);
        getGradientDrawable(mutedDarkView).setColor(mutedDarkColor);
        colorFound = palette.getDarkMutedSwatch() != null;
        mutedDarkView.setTag(colorFound ? mutedDarkColor : null);
        mutedDarkView.setAlpha(colorFound ? 1.0f : 0.26f);
        mutedDarkView.setOnLongClickListener(mLongClickListener);

        View mutedLightView = mCardView.findViewById(R.id.muted_light);
        int mutedLightColor = palette.getLightMutedColor(Color.WHITE);
        getGradientDrawable(mutedLightView).setColor(mutedLightColor);
        colorFound = palette.getLightMutedSwatch() != null;
        mutedLightView.setTag(colorFound ? mutedLightColor : null);
        mutedLightView.setAlpha(colorFound ? 1.0f : 0.26f);
        mutedLightView.setOnLongClickListener(mLongClickListener);
    }

    private GradientDrawable getGradientDrawable(View colorShape) {
        return (GradientDrawable) colorShape.getBackground();
    }

    private View.OnLongClickListener mLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (v.getTag() == null) {
                Toast.makeText(mContext, "No color available.", Toast.LENGTH_SHORT).show();
            } else {
                String colorHex = String.format("%06X", (0xFFFFFF & (int) v.getTag()));
                ClipboardManager clipboard = (ClipboardManager)
                        mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Color Hex", colorHex);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mContext, "Copied color '" + colorHex + "' to clipboard.",
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };
}
