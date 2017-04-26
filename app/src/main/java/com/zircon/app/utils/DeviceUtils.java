package com.zircon.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.zircon.app.App;

import java.util.UUID;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by jikoobaruah on 07/04/17.
 */

public class DeviceUtils {

    private static final String DEVICE_PREF_FILE = "device";
    private static final String KEY_DEVICE_ID = "device_id";

    public static String getUniqueDeviceID(){
        SharedPreferences pref = App.appInstance.getSharedPreferences(DEVICE_PREF_FILE, Context.MODE_PRIVATE);
        if (pref.contains(KEY_DEVICE_ID)){
            return pref.getString(KEY_DEVICE_ID,null);
        }else{
            String uniqueID = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(KEY_DEVICE_ID,uniqueID);
            editor.apply();
            return uniqueID;
        }


    }

//    public static void hideSoftKeyboard() {
//        if(getCurrentFocus()!=null) {
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        }
//    }
//
//    /**
//     * Shows the soft keyboard
//     */
//    public void static showSoftKeyboard(View view) {
//        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        view.requestFocus();
//        inputMethodManager.showSoftInput(view, 0);
//    }
}
