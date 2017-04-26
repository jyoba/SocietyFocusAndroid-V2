package com.zircon.app.utils.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.zircon.app.R;

/**
 * Created by jikoobaruah on 26/04/17.
 */

//aspect ratio = height/width;

public class AspectRatioCardView extends CardView {

    private float aspectRatio;

    public AspectRatioCardView(Context context) {
        super(context);
        aspectRatio = 1;
    }

    public AspectRatioCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        processAttrs(attrs);
    }
    public AspectRatioCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttrs(attrs);
    }

    private void processAttrs(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AspectRatio,
                0, 0);

        try {
            aspectRatio = a.getFloat(R.styleable.AspectRatio_ratio, 1);
        } finally {
            a.recycle();
        }
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        float height = width*aspectRatio;
        setMeasuredDimension(width, (int)height);
    }
}
