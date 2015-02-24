package com.tonyw.sampleapps.palettecolorextraction;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Adapter for the GridView that displays the cards.
 */
public class CardAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> mBitmaps;

    public CardAdapter(Context context, ArrayList<Bitmap> bitmaps) {
        mContext = context;
        mBitmaps = bitmaps;
    }

    public void remove(int position) {
        mBitmaps.remove(position);
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
                GradientDrawable vibrant = (GradientDrawable)
                        cardView.findViewById(R.id.vibrant).getBackground();
                GradientDrawable vibrantDark = (GradientDrawable)
                        cardView.findViewById(R.id.vibrant_dark).getBackground();
                GradientDrawable vibrantLight = (GradientDrawable)
                        cardView.findViewById(R.id.vibrant_light).getBackground();
                GradientDrawable muted = (GradientDrawable)
                        cardView.findViewById(R.id.muted).getBackground();
                GradientDrawable mutedDark = (GradientDrawable)
                        cardView.findViewById(R.id.muted_dark).getBackground();
                GradientDrawable mutedLight = (GradientDrawable)
                        cardView.findViewById(R.id.muted_light).getBackground();
                vibrant.setColor(palette.getVibrantColor(Color.BLACK));
                vibrantDark.setColor(palette.getDarkVibrantColor(Color.BLACK));
                vibrantLight.setColor(palette.getLightVibrantColor(Color.BLACK));
                muted.setColor(palette.getMutedColor(Color.BLACK));
                mutedDark.setColor(palette.getDarkMutedColor(Color.BLACK));
                mutedLight.setColor(palette.getLightMutedColor(Color.BLACK));
            }
        });

        return cardView;
    }
}
