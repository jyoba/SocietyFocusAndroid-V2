package com.zircon.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by jikoobaruah on 25/04/17.
 */

public class BubbleViewPagerIndicator extends LinearLayout {


    public static final String TAG = BubbleViewPagerIndicator.class.getSimpleName();
    private static final int DIA = 8;
    private static final int MARGIN = 3;
    private volatile ArrayList<View> bubbles;

    private int selectedBubblePosition = 0;
    private int bubbleCount = 0;

    public BubbleViewPagerIndicator(Context context) {
        super(context);
        bubbles = new ArrayList<View>();
    }

    public BubbleViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        bubbles = new ArrayList<View>();
    }

    public BubbleViewPagerIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        bubbles = new ArrayList<View>();
    }

    public void makeBubbles(int resId, int count) {
        bubbleCount = count;
        bubbles.clear();
        removeAllViews();
        clearDisappearingChildren();
        int bubbleDiameter = (int) (getResources().getDisplayMetrics().density * DIA);
        int bubbleMargin = (int) (getResources().getDisplayMetrics().density * MARGIN);
        for (int i = 0; i < bubbleCount; i++) {
            View view = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(bubbleDiameter, bubbleDiameter);
            params.setMargins(bubbleMargin, bubbleMargin, bubbleMargin, bubbleMargin);
            view.setLayoutParams(params);
            view.setEnabled(false);

            view.setBackgroundResource(resId);
            bubbles.add(view);
            addView(view);
        }
        if (bubbles.size() > 0) {
            bubbles.get(0).setEnabled(true);
        }

    }

    public void setBubbleActive(int position) {
        for (int i = 0; i < bubbleCount; i++) {
            if (i == position) {
                bubbles.get(i).setEnabled(true);
            } else {
                bubbles.get(i).setEnabled(false);
            }
        }
    }
}
