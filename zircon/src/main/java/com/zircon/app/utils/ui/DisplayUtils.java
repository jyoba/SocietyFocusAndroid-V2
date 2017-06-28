package com.zircon.app.utils.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zircon.app.R;
import com.zircon.app.model.Complaint;
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

        if (isForced) {
            builder.setTitle("Update required");
        } else {
            builder.setTitle("Update available");

        }

        builder.setMessage("Please update your app");

        builder.create().show();

    }


    public static void showComingSoon(Context context) {
        Toast.makeText(context, context.getString(R.string.msg_coming_soon), Toast.LENGTH_LONG).show();
    }

    public static void showOnlySocietyFeature(Context context) {
        Toast.makeText(context, context.getString(R.string.msg_only_society_feature), Toast.LENGTH_LONG).show();
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static void showYesNoDialog(Context context, String text, final View.OnClickListener yesClickListener, final View.OnClickListener noClickListener) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.popup_yes_no, null, false);
        dialogBuilder.setView(dialogView);

        TextView textView = (TextView) dialogView.findViewById(R.id.tv_title);
        ImageButton doneButton = (ImageButton) dialogView.findViewById(R.id.btn_done);
        ImageButton cancelButton = (ImageButton) dialogView.findViewById(R.id.btn_cancel);
        textView.setText(text);

        final AlertDialog alertDialog = dialogBuilder.create();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (noClickListener != null)
                    noClickListener.onClick(v);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (yesClickListener != null)
                    yesClickListener.onClick(v);
            }
        });


        alertDialog.show();

    }

    public static void setComplaintStatus(TextView statusTextView, int status) {
        statusTextView.setTextColor(statusTextView.getResources().getColor(android.R.color.white));
        switch (status) {
            case Complaint.Status.NEW:
                statusTextView.setText("OPEN");
                statusTextView.setBackgroundColor(statusTextView.getResources().getColor(android.R.color.holo_red_dark));
                break;
            case Complaint.Status.COMPLETED:
                statusTextView.setText("CLOSED");
                statusTextView.setBackgroundColor(statusTextView.getResources().getColor(android.R.color.holo_green_dark));
                break;
            case Complaint.Status.IN_PROGRESS:
                statusTextView.setText("IN PROGRESS");
                statusTextView.setBackgroundColor(statusTextView.getResources().getColor(android.R.color.holo_blue_dark));
                break;
            case Complaint.Status.REJECTED:
                statusTextView.setText("REJECTED");
                statusTextView.setBackgroundColor(statusTextView.getResources().getColor(android.R.color.holo_orange_dark));
                break;
        }
    }
}
