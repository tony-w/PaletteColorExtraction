package com.tonyw.sampleapps.palettecolorextraction;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;

import java.util.ArrayList;

/**
 * Adapter for the GridView that displays the cards.
 */
public class CardAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> mBitmaps;
    /** The GridView using this adapter. Used as a horrible hack to scroll to a position. */
    private GridView mGridView;

    // Variables for undo feature.
    private Bitmap mDismissedBitmap;
    private int mDismissedPosition;
    OnClickWrapper onUndoClickWrapper = new OnClickWrapper("undoclickwrapper",
            new SuperToast.OnClickListener() {
        @Override
        public void onClick(View view, Parcelable token) {
            if (mDismissedBitmap != null) {
                mBitmaps.add(mDismissedPosition, mDismissedBitmap);
                notifyDataSetChanged();
                mGridView.smoothScrollToPosition(mDismissedPosition);
                mDismissedBitmap = null;
            }
        }
    });

    public CardAdapter(Context context, ArrayList<Bitmap> bitmaps, GridView gridView) {
        mContext = context;
        mBitmaps = bitmaps;
        mGridView = gridView;
    }

    public void remove(int position) {
        if (mDismissedBitmap == null) {
            // Allow user to under one dismissal.
            SuperActivityToast mUndoToast = new SuperActivityToast((Activity) mContext,
                    SuperToast.Type.BUTTON);
            mUndoToast.setDuration(SuperToast.Duration.EXTRA_LONG);
            mUndoToast.setText("Card dismissed.");
            mUndoToast.setButtonIcon(SuperToast.Icon.Dark.UNDO, "UNDO");
            mUndoToast.setOnClickWrapper(onUndoClickWrapper);
            mUndoToast.show();
        }

        mDismissedBitmap = mBitmaps.remove(position);
        mDismissedPosition = position;
    }

    @Override
    public int getCount() {
        return mBitmaps.size();
    }

    @Override
    public Object getItem(int position) {
        // Not used.
        return null;
    }

    @Override
    public long getItemId(int position) {
        // Not used.
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CardView cardView;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            cardView = (CardView) inflater.inflate(R.layout.card_layout, null);
        } else {
            cardView = (CardView) convertView;
        }
        Bitmap bitmap = mBitmaps.get(position);
        ((ImageView) cardView.findViewById(R.id.card_image)).setImageBitmap(bitmap);

        // Extract prominent colors asynchronously and then update the card.
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                View vibrantView = cardView.findViewById(R.id.vibrant);
                int vibrantColor = palette.getVibrantColor(Color.BLACK);
                getGradientDrawable(vibrantView).setColor(vibrantColor);
                vibrantView.setTag(vibrantColor); // For retrieval when long-clicking.
                vibrantView.setOnLongClickListener(mLongClickListener);

                View vibrantDarkView = cardView.findViewById(R.id.vibrant_dark);
                int vibrantDarkColor = palette.getDarkVibrantColor(Color.BLACK);
                getGradientDrawable(vibrantDarkView).setColor(vibrantDarkColor);
                vibrantDarkView.setTag(vibrantDarkColor); // For retrieval when long-clicking.
                vibrantDarkView.setOnLongClickListener(mLongClickListener);

                View vibrantLightView = cardView.findViewById(R.id.vibrant_light);
                int vibrantLightColor = palette.getLightVibrantColor(Color.BLACK);
                getGradientDrawable(vibrantLightView).setColor(vibrantLightColor);
                vibrantLightView.setTag(vibrantLightColor); // For retrieval when long-clicking.
                vibrantLightView.setOnLongClickListener(mLongClickListener);

                View mutedView = cardView.findViewById(R.id.muted);
                int mutedColor = palette.getMutedColor(Color.BLACK);
                getGradientDrawable(mutedView).setColor(mutedColor);
                mutedView.setTag(mutedColor); // For retrieval when long-clicking.
                mutedView.setOnLongClickListener(mLongClickListener);

                View mutedDarkView = cardView.findViewById(R.id.muted_dark);
                int mutedDarkColor = palette.getDarkMutedColor(Color.BLACK);
                getGradientDrawable(mutedDarkView).setColor(mutedDarkColor);
                mutedDarkView.setTag(mutedDarkColor); // For retrieval when long-clicking.
                mutedDarkView.setOnLongClickListener(mLongClickListener);

                View mutedLightView = cardView.findViewById(R.id.muted_light);
                int mutedLightColor = palette.getLightMutedColor(Color.BLACK);
                getGradientDrawable(mutedLightView).setColor(mutedLightColor);
                mutedLightView.setTag(mutedLightColor); // For retrieval when long-clicking.
                mutedLightView.setOnLongClickListener(mLongClickListener);
            }
        });

        return cardView;
    }

    private GradientDrawable getGradientDrawable(View colorShape) {
        return (GradientDrawable) colorShape.getBackground();
    }

    private View.OnLongClickListener mLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            String colorHex = String.format("%06X", (0xFFFFFF & (int) v.getTag()));
            ClipboardManager clipboard = (ClipboardManager)
                    mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Color Hex", colorHex);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(mContext, "Copied color '" + colorHex + "' to clipboard.",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
    };
}
