package com.zircon.app.utils.ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jikoobaruah on 30/04/17.
 */

public class VerticalSeparator extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;

    public VerticalSeparator(int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = mVerticalSpaceHeight;
    }
}
