package com.tonyw.sampleapps.palettecolorextraction;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends Activity {
    private static final int REQUEST_CODE_ACTION_ADD_FROM_STORAGE = 0;
    private static final int REQUEST_CODE_ACTION_ADD_FROM_CAMERA = 1337;
    private static final String BUNDLE_SAVED_BITMAPS = "bitmaps";

    private ArrayList<Bitmap> mBitmaps;
    private GridView mGridView;
    private CardAdapter mCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState != null) {
            mBitmaps = savedInstanceState.getParcelableArrayList(BUNDLE_SAVED_BITMAPS);
        } else {
            mBitmaps = new ArrayList<>();
        }

        mCardAdapter = new CardAdapter(this, mBitmaps);
        mGridView = (GridView) findViewById(R.id.main_view);
        mGridView.setAdapter(mCardAdapter);

        if (savedInstanceState == null) {
            try {
                addCards();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mCardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedState) {
        super.onSaveInstanceState(savedState);

        savedState.putParcelableArrayList(BUNDLE_SAVED_BITMAPS, mBitmaps);
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
        if (R.id.action_add_from_camera == item.getItemId()) {
            // Start Intent to retrieve an image (see OnActivityResult).
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CODE_ACTION_ADD_FROM_CAMERA);
            return true;
        } else if (R.id.action_add_from_storage == item.getItemId()) {
            // Start Intent to retrieve an image (see OnActivityResult).
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, REQUEST_CODE_ACTION_ADD_FROM_STORAGE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        if (Activity.RESULT_OK == resultCode) {
            if (REQUEST_CODE_ACTION_ADD_FROM_STORAGE == requestCode) {
                try {
                    InputStream stream = getContentResolver().openInputStream(
                            data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (REQUEST_CODE_ACTION_ADD_FROM_CAMERA == requestCode) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
            }
        }
        if (bitmap != null) {
            addCard(bitmap);
            mGridView.smoothScrollToPosition(mBitmaps.size() - 1);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
