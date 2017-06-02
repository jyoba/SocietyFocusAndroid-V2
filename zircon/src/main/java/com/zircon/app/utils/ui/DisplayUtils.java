package com.zircon.app.utils.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.zircon.app.R;
import com.zircon.app.utils.NavigationUtils;

/**
 * Created by jikoobaruah on 26/05/17.
 */

public class DisplayUtils {

    public static void showUpdateDialog(final Context context, boolean isForced, DialogInterface.OnClickListener dismissClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NavigationUtils.navigateToPlaystore(context);
            }
        });
        builder.setNegativeButton("No, thanks", dismissClickListener);

        if (isForced){
            builder.setTitle("Update required");
        } else {
            builder.setTitle("Update available");

        }

        builder.setMessage("Please update your app");

        builder.create().show();

    }


    public static void showComingSoon(Context context){
        Toast.makeText(context,context.getString(R.string.msg_coming_soon),Toast.LENGTH_LONG).show();
    }

    public static void showOnlySocietyFeature(Context context){
        Toast.makeText(context,context.getString(R.string.msg_only_society_feature),Toast.LENGTH_LONG).show();
    }

}
