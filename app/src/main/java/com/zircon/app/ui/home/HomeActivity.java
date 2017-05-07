package com.zircon.app.ui.home;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.ui.common.fragment.BaseDrawerActivity;
import com.zircon.app.ui.widget.ToolsWidget;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.NavigationUtils;

public class HomeActivity extends BaseDrawerActivity {

    private View rwaView;
    private View residentsView;
    private View complaintsView;
    private View carSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupToolbar();
        setTitle(AccountManager.getInstance().getloggedInSociety().name);
        ToolsWidget.setupToolsWidget(this);
        setupDrawer(R.id.nav_manage);
        NoticeBoardHelper.setupNoticeBoard(findViewById(R.id.cv_nb));

        String bgUrl = AccountManager.getInstance().getloggedInSociety().societypic;
        Picasso.with(this).load(bgUrl).into((ImageView) findViewById(R.id.society_bg));

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}
