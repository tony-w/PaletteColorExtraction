package com.tonyw.sampleapps.palettecolorextraction;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends Activity {
    private static final int ACTION_ADD_REQUEST_CODE = 0;

    private ArrayList<Bitmap> mBitmaps;
    private CardAdapter mCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBitmaps = new ArrayList<>();
        mCardAdapter = new CardAdapter(this, mBitmaps);
        GridView gridView = (GridView) findViewById(R.id.main_view);
        gridView.setAdapter(mCardAdapter);

        try {
            addCards();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds cards with the default images stored in assets.
     */
    private void addCards() throws IOException {
        AssetManager assetManager = getAssets();
        for (String assetName : assetManager.list("sample_images")) {
            InputStream assetStream = assetManager.open("sample_images/" + assetName);
            Bitmap bitmap = BitmapFactory.decodeStream(assetStream);
            assetStream.close();
            addCard(bitmap);
        }
    }

    /**
     * Adds the provided bitmap to a list, and repopulates the main GridView with the new card.
     */
    private void addCard(Bitmap bitmap) {
        mBitmaps.add(bitmap);
        mCardAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            // Start Intent to retrieve an image (see OnActivityResult).
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, ACTION_ADD_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK)
            try {
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                stream.close();
                addCard(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
