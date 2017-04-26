package com.zircon.app.ui.widget;


import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.zircon.app.R;

/**
 * Created by jikoobaruah on 08/04/17.
 */

public class ToolsWidget {

    public static void setupToolsWidget(Activity activity){

        final FloatingActionButton fabNavigate = (FloatingActionButton) activity.findViewById(R.id.fabNav);
        fabNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        final FloatingActionButton fabCall = (FloatingActionButton) activity.findViewById(R.id.fabCall);
        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        final FloatingActionButton fabMessage = (FloatingActionButton) activity.findViewById(R.id.fabMesage);
        fabMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

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
