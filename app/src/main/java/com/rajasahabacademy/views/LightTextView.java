package com.rajasahabacademy.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Arthonsys on 12/2/2019.
 */

public class LightTextView extends AppCompatTextView {
    public LightTextView(Context context) {
        super(context);
        setassets(context);
    }

    public LightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setassets(context);
    }

    public LightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setassets(context);
    }


    private void setassets(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "font/open-sans.light.ttf"));

    }
}
