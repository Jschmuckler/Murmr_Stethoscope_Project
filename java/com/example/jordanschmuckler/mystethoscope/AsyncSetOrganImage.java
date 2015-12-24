package com.example.jordanschmuckler.mystethoscope;

import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Jordan Schmuckler on 4/13/2015.
 */
public class AsyncSetOrganImage extends AsyncTask<String, Void, String> {

    CustomAdapter context;
    private  ImageView imageView;
    private String whichImage;
    private Drawable drawableImage;
    public AsyncSetOrganImage(CustomAdapter activity,ImageView view, String whichImage)
    {
        this.context = activity;
        this.imageView = view;
        this.whichImage = whichImage;
    }

    @Override
    protected String doInBackground(String... params) {
        switch (whichImage)
        {
            case "HEART":
                drawableImage = imageView.getResources().getDrawable(R.drawable.heartlm);
                break;

            case "LUNGS":
                drawableImage = imageView.getResources().getDrawable(R.drawable.lungslm);
                break;

            case "QUESTIONMARK":
                drawableImage = imageView.getResources().getDrawable(R.drawable.questionmarklm);
                break;

        }
        return whichImage;
    }
        @Override
        protected void onPostExecute(String result)
        {
            imageView.setImageBitmap(ResourcesToBitmap.drawableToBitmap(drawableImage));

        }
}
