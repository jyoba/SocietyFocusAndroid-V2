package com.zircon.app.ui.widget;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.zircon.app.R;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.NavigationUtils;

/**
 * Created by jikoobaruah on 08/04/17.
 */

public class ToolsWidget {

    public static void setupToolsWidget(final Activity activity) {

        final FloatingActionButton fabNavigate = (FloatingActionButton) activity.findViewById(R.id.fabNav);
        fabNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(String.format("https://www.google.com/maps/dir/?api=1&destination=%f,%f", AccountManager.getInstance().getloggedInSociety().location.latitude, AccountManager.getInstance().getloggedInSociety().location.longitude)));
                activity.startActivity(intent);

            }
        });

        final FloatingActionButton fabCall = (FloatingActionButton) activity.findViewById(R.id.fabCall);
        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationUtils.navigateToDialer(view.getContext(), AccountManager.getInstance().getloggedInSociety().contactDetail);
            }
        });

        final FloatingActionButton fabMessage = (FloatingActionButton) activity.findViewById(R.id.fabMesage);
        fabMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationUtils.navigateToEmail(view.getContext(), AccountManager.getInstance().getloggedInSociety().contactDetail);
            }
        });

        final FloatingActionButton fabMenu = (FloatingActionButton) activity.findViewById(R.id.fabMenu);
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabCall.getX() == fabMenu.getX() && fabCall.getY() == fabMenu.getY()) {
                    fabNavigate.animate().y(fabNavigate.getY() - fabNavigate.getMeasuredHeight() - 30).x(fabNavigate.getX() - fabNavigate.getMeasuredWidth() - 30).setInterpolator(new OvershootInterpolator());
                    fabCall.animate().y(fabNavigate.getY() - fabNavigate.getMeasuredHeight() - 30).setInterpolator(new OvershootInterpolator());
                    fabMessage.animate().x(fabNavigate.getX() - fabNavigate.getMeasuredWidth() - 30).setInterpolator(new OvershootInterpolator());
                } else {
                    fabNavigate.animate().y(fabMenu.getY()).x(fabMenu.getX());
                    fabCall.animate().y(fabMenu.getY()).x(fabMenu.getX());
                    fabMessage.animate().y(fabMenu.getY()).x(fabMenu.getX());
                }
            }
        });


    }


}
