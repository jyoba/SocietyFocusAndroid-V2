package com.zircon.app.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.model.NoticeBoard;
import com.zircon.app.model.response.NoticeBoardResponse;
import com.zircon.app.ui.common.fragment.BaseActivity;
import com.zircon.app.ui.common.fragment.BaseDrawerActivity;
import com.zircon.app.ui.widget.BubbleViewPagerIndicator;
import com.zircon.app.ui.widget.ToolsWidget;
import com.zircon.app.utils.API;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.HTTP;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseDrawerActivity {


    private BubbleViewPagerIndicator pageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupToolbar();
        setTitle(AccountManager.getInstance().getloggedInSociety().name);
        ToolsWidget.setupToolsWidget(this);
        setupDrawer();
        NoticeBoardHelper.setupNoticeBoard(findViewById(R.id.rl_nb));


        String bgUrl = AccountManager.getInstance().getloggedInSociety().societypic;

        Picasso.with(this).setIndicatorsEnabled(false);
        Picasso.with(this).load(bgUrl).into((ImageView) findViewById(R.id.society_bg));


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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        closeDrawer();

        return true;
    }



}
