package com.tonyw.sampleapps.palettecolorextraction;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
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
        Palette.Swatch swatch = palette.getVibrantSwatch();
        vibrantView.setTag(swatch); // For retrieving colors when clicking or long-clicking.
        vibrantView.setAlpha(swatch != null ? 1.0f : 0.26f);
        vibrantView.setOnLongClickListener(mOnLongClickListener);
        vibrantView.setOnClickListener(mOnClickListener);

        View vibrantDarkView = mCardView.findViewById(R.id.vibrant_dark);
        int vibrantDarkColor = palette.getDarkVibrantColor(Color.WHITE);
        getGradientDrawable(vibrantDarkView).setColor(vibrantDarkColor);
        swatch = palette.getDarkVibrantSwatch();
        vibrantDarkView.setTag(swatch);
        vibrantDarkView.setAlpha(swatch != null ? 1.0f : 0.26f);
        vibrantDarkView.setOnLongClickListener(mOnLongClickListener);
        vibrantDarkView.setOnClickListener(mOnClickListener);

        View vibrantLightView = mCardView.findViewById(R.id.vibrant_light);
        int vibrantLightColor = palette.getLightVibrantColor(Color.WHITE);
        getGradientDrawable(vibrantLightView).setColor(vibrantLightColor);
        swatch = palette.getLightVibrantSwatch();
        vibrantLightView.setTag(swatch);
        vibrantLightView.setAlpha(swatch != null ? 1.0f : 0.26f);
        vibrantLightView.setOnLongClickListener(mOnLongClickListener);
        vibrantLightView.setOnClickListener(mOnClickListener);

        View mutedView = mCardView.findViewById(R.id.muted);
        int mutedColor = palette.getMutedColor(Color.WHITE);
        getGradientDrawable(mutedView).setColor(mutedColor);
        swatch = palette.getMutedSwatch();
        mutedView.setTag(swatch);
        mutedView.setAlpha(swatch != null ? 1.0f : 0.26f);
        mutedView.setOnLongClickListener(mOnLongClickListener);
        mutedView.setOnClickListener(mOnClickListener);

        View mutedDarkView = mCardView.findViewById(R.id.muted_dark);
        int mutedDarkColor = palette.getDarkMutedColor(Color.WHITE);
        getGradientDrawable(mutedDarkView).setColor(mutedDarkColor);
        swatch = palette.getDarkMutedSwatch();
        mutedDarkView.setTag(swatch);
        mutedDarkView.setAlpha(swatch != null ? 1.0f : 0.26f);
        mutedDarkView.setOnLongClickListener(mOnLongClickListener);
        mutedDarkView.setOnClickListener(mOnClickListener);

        View mutedLightView = mCardView.findViewById(R.id.muted_light);
        int mutedLightColor = palette.getLightMutedColor(Color.WHITE);
        getGradientDrawable(mutedLightView).setColor(mutedLightColor);
        swatch = palette.getLightMutedSwatch();
        mutedLightView.setTag(swatch);
        mutedLightView.setAlpha(swatch != null ? 1.0f : 0.26f);
        mutedLightView.setOnLongClickListener(mOnLongClickListener);
        mutedLightView.setOnClickListener(mOnClickListener);
    }

    private GradientDrawable getGradientDrawable(View colorShape) {
        return (GradientDrawable) colorShape.getBackground();
    }

    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (v.getTag() == null) {
                Toast.makeText(mContext, "No color available.", Toast.LENGTH_SHORT).show();
            } else {
                Palette.Swatch swatch = (Palette.Swatch) v.getTag();
                String colorHex = String.format("%06X", (0xFFFFFF & swatch.getRgb()));
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

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Palette.Swatch swatch = (Palette.Swatch) v.getTag();
            if (swatch == null) return;
            int backgroundColor = swatch.getRgb();
            int titleTextColor = swatch.getTitleTextColor();
            int bodyTextColor = swatch.getBodyTextColor();
            Intent colorWithTextIntent = new Intent(mContext, ColorWithTextActivity.class);
            colorWithTextIntent.putExtra(ColorWithTextActivity.EXTRA_BACKGROUND_COLOR,
                    backgroundColor);
            colorWithTextIntent.putExtra(ColorWithTextActivity.EXTRA_TITLE_TEXT_COLOR,
                    titleTextColor);
            colorWithTextIntent.putExtra(ColorWithTextActivity.EXTRA_BODY_TEXT_COLOR,
                    bodyTextColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Rect rect = new Rect();
                v.getGlobalVisibleRect(rect);
                CircularRevealTransition.x = rect.centerX();
                CircularRevealTransition.y = rect.centerY()
                        // Subtract off height of action bar and status bar, which apparently isn't
                        // done when calculating the global visible rect... (status bar height=25dp)
                        - ((Activity) mContext).getActionBar().getHeight()
                        - (int) Math.ceil(25 * mContext.getResources().getDisplayMetrics().density);
                mContext.startActivity(colorWithTextIntent,
                        ActivityOptions.makeSceneTransitionAnimation(
                                (Activity) mContext).toBundle());
            } else {
                mContext.startActivity(colorWithTextIntent);
            }
        }
    };
}
