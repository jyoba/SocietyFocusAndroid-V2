package com.zircon.app.utils.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zircon.app.R;

import java.util.ArrayList;

/**
 * Created by jikoobaruah on 04/04/17.
 */

public class Overlay extends RelativeLayout {


    private Button okButton;

    private ArrayList<View> cdViews = new ArrayList<>();

    public Overlay(Context context) {
        super(context);
        init();
    }

    public Overlay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Overlay(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Overlay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setAlpha(0.90f);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });





    }

    public void drawOverlay() {
        okButton = (Button) ((Activity)getContext()).findViewById(R.id.btn_ok);
        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Overlay.this.setVisibility(GONE);
            }
        });
        ViewGroup descView = findViewForOverLay();
        if (descView != null) {
            findViewsWithDescription(descView);
            requestLayout();
        } else
            setVisibility(GONE);


    }

    private void findViewsWithDescription(ViewGroup descView) {
        View v = null;
        for (int i = 0; i < descView.getChildCount(); i++) {

            v= descView.getChildAt(i);
            if (v instanceof  ViewGroup){
                findViewsWithDescription((ViewGroup) v);
            }else if (v.getContentDescription() != null && v.getContentDescription().length() > 0) {
                cdViews.add(descView.getChildAt(i));
            }
        }
    }


    private ViewGroup findViewForOverLay() {
        ViewGroup parentViewGroup = (ViewGroup) getParent();
        ViewGroup searchViewGroup = null;
        for (int i = 0; i < parentViewGroup.getChildCount(); i++) {
            if (!(parentViewGroup.getChildAt(i) instanceof Overlay)) {
                searchViewGroup = (ViewGroup) parentViewGroup.getChildAt(i);
                break;
            }
        }
        return searchViewGroup;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        for (View v : cdViews) {
            drawOverlayforView(v, canvas);
        }

    }

    private void drawOverlayforView(View view, Canvas canvas) {
        Rect myViewRect = new Rect();
        view.getGlobalVisibleRect(myViewRect);
        float x = myViewRect.left;
        float y = myViewRect.top;

        Resources r = getResources();
        float px = r.getDimension(R.dimen.text_size_normal);


        float offsetY = 30;// TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());

        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setTextSize(px);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        String[] strings = view.getContentDescription().toString().split("\\n");
        float initialY = y - strings.length * px - offsetY;
        for (int i = 0 ;  i < strings.length; i++){
            canvas.drawText(strings[i],x, initialY,paint);
            initialY = initialY+px;
        }

//        canvas.drawLine((myViewRect.right-myViewRect.left)/2,initialY-px+5,(myViewRect.right-myViewRect.left)/2,y-25,paint);


    }
}
