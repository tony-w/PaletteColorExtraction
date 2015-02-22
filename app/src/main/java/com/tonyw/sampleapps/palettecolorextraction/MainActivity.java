package com.tonyw.sampleapps.palettecolorextraction;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
        LayoutInflater inflater = getLayoutInflater();
        AssetManager assetManager = getAssets();
        for (String assetName : assetManager.list("sample_images")) {
            InputStream assetStream = assetManager.open("sample_images/" + assetName);
            Bitmap bitmap = BitmapFactory.decodeStream(assetStream);
            CardView cardView = (CardView) inflater.inflate(R.layout.card_layout, null);
            ((ImageView) cardView.findViewById(R.id.card_image)).setImageBitmap(bitmap);
            mainView.addView(cardView);
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
