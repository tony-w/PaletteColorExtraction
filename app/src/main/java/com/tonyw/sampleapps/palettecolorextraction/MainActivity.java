package com.tonyw.sampleapps.palettecolorextraction;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            addCards();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addCards() throws IOException {
        ViewGroup mainView = (ViewGroup) findViewById(R.id.main_view);
        ViewGroup mainView2 = (ViewGroup) findViewById(R.id.main_view_2);
        LayoutInflater inflater = getLayoutInflater();
        AssetManager assetManager = getAssets();
        int numCardsAdded = 0;
        for (String assetName : assetManager.list("sample_images")) {
            InputStream assetStream = assetManager.open("sample_images/" + assetName);
            Bitmap bitmap = BitmapFactory.decodeStream(assetStream);
            final CardView cardView = (CardView) inflater.inflate(R.layout.card_layout, null);
            ((ImageView) cardView.findViewById(R.id.card_image)).setImageBitmap(bitmap);
            if (numCardsAdded++ % 2 == 1 && mainView2 != null) {
                mainView2.addView(cardView);
            } else {
                mainView.addView(cardView);
            }
            // Extract prominent colors asynchronously and then update the card.
            Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {
                    GradientDrawable vibrant = (GradientDrawable)
                            cardView.findViewById(R.id.vibrant).getBackground();
                    vibrant.setColor(palette.getVibrantColor(Color.BLACK));
                    GradientDrawable vibrantDark = (GradientDrawable)
                            cardView.findViewById(R.id.vibrant_dark).getBackground();
                    GradientDrawable vibrantLight = (GradientDrawable)
                            cardView.findViewById(R.id.vibrant_light).getBackground();
                    GradientDrawable muted = (GradientDrawable)
                            cardView.findViewById(R.id.muted).getBackground();
                    vibrant.setColor(palette.getVibrantColor(Color.BLACK));
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
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
