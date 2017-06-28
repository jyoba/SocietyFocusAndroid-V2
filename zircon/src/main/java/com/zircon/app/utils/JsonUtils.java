package com.zircon.app.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * Created by jikoobaruah on 10/04/17.
 */

public class JsonUtils {

    public static String getJson(Object obj) {
        if (obj != null) {
            return new Gson().toJson(obj);
        }
        return null;
    }

    public static <T> T getObject(String jsonString, Class<T> clas) {
        if (!TextUtils.isEmpty(jsonString)) {
            return new Gson().fromJson(jsonString, clas);
        }
        return null;
    }


}
