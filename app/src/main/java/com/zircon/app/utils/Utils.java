package com.zircon.app.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;

import com.zircon.app.utils.ui.TextDrawable;

import java.util.Random;

/**
 * Created by jikoobaruah on 02/05/17.
 */

public class Utils {

    public static int getRandomMaterialColor(Context context, String typeColor) {
        int returnColor = Color.BLACK;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getApplicationContext().getPackageName());

        if (arrayId != 0)
        {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }

    public static TextDrawable getTextDrawable(Context context,String s) {
        TextDrawable td = new TextDrawable(context);
        td.setText(s.substring(0,1).toUpperCase());
        td.setTextColor(Color.WHITE);
        return td;
    }
}
