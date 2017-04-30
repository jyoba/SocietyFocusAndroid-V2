package com.zircon.app.ui.common.fragment;

import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.utils.AccountManager;

/**
 * Created by jikoobaruah on 10/04/17.
 */

public abstract class BaseDrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout drawer;

    private ImageView profileImageView;
    private TextView nameTextView;
    private TextView emailTextView;

    protected void setupDrawer(@IdRes int checkedId) {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(checkedId);

        profileImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_profile);
        nameTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_name);
        emailTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_email);

//        Picasso.with(this).setIndicatorsEnabled(true);
        Picasso.with(this).load(AccountManager.getInstance().getloggedInUser().profilePic).fit().centerCrop().into(profileImageView);

        nameTextView.setText(AccountManager.getInstance().getloggedInUser().firstname);

        emailTextView.setText(AccountManager.getInstance().getloggedInUser().email);

    }

    protected void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
