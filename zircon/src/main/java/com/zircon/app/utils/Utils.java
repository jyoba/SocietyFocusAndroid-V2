package com.zircon.app.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;

import com.zircon.app.utils.ui.TextDrawable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
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

    public static String parseServerDate(String serverDate) throws ParseException {
        //2017-03-20T10:23:00 +0000
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss z").parse(serverDate);
        return new SimpleDateFormat("dd MMM,yyyy").format(date);
    }

    public static String getNow() {
        //2017-03-20T10:23:00 +0000
        return new SimpleDateFormat("dd MMM,yyyy").format(new Date());
    }

}
