package com.zircon.app.ui.home;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.ui.common.fragment.BaseDrawerActivity;
import com.zircon.app.ui.widget.ToolsWidget;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.NavigationUtils;
import com.zircon.app.utils.ui.ForceUpdateHelper;

public class HomeActivity extends BaseDrawerActivity {

    private View rwaView;
    private View residentsView;
    private View complaintsView;
    private View carSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ForceUpdateHelper.checkForUpdates(HomeActivity.this);

        setupToolbar();
        ToolsWidget.setupToolsWidget(this);
        setupDrawer(R.id.nav_home);

        load();

        rwaView = findViewById(R.id.tv_rwa);
        residentsView = findViewById(R.id.tv_residents);
        complaintsView = findViewById(R.id.tv_complaints);
        carSearchView = findViewById(R.id.tv_car_search);

        residentsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.navigateToResidentsPage(HomeActivity.this);
            }
        });

        rwaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.navigateToRWAPage(HomeActivity.this);
            }
        });

        complaintsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.navigateToComplaints(HomeActivity.this);
            }
        });

        carSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.navigateToCarSearch(HomeActivity.this);
            }
        });

    }

    @Override
    protected void load() {

        if (AccountManager.getInstance().getloggedInUser() != null) {
            setTitle(AccountManager.getInstance().getloggedInSociety().name);
            NoticeBoardHelper.setupNoticeBoard(findViewById(R.id.cv_nb));
            Picasso.with(this).load(AccountManager.getInstance().getloggedInSociety().societypic).into((ImageView) findViewById(R.id.society_bg));
        }
    }
}
