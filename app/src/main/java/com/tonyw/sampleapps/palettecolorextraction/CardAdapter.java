package com.tonyw.sampleapps.palettecolorextraction;

import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Adapter for the GridView that displays the cards.
 */
public class CardAdapter extends BaseAdapter {
    private ArrayList<CardView> mCards;

    public CardAdapter(ArrayList<CardView> cards) {
        mCards = cards;
    }

    @Override
    public int getCount() {
        return mCards.size();
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
        return /*convertView != null ? convertView :*/ mCards.get(position);
    }
}
