package com.tonyw.sampleapps.palettecolorextraction;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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
        new ExtractPaletteColorsAsyncTask(mContext, cardView).execute(bitmap);

        return cardView;
    }
}
