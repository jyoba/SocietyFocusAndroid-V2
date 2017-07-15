package com.zircon.app.ui.usr;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.model.User;
import com.zircon.app.ui.common.fragment.BaseActivity;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.Utils;

/**
 * Created by jyotishman on 10/07/17.
 */

public class UserDetailActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    public static final String KEY_USER = "user";
    public static final String KEY_COLOR = "color";

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean isTheTitleVisible = false;
    private boolean isTheTitleContainerVisible = true;

    private LinearLayout titleContainer;
    private TextView title;
    private AppBarLayout appBarLayout;
    private ImageView imageparallax;
    private FrameLayout frameParallax;
    private Toolbar toolbar;
    private TextView collapsingTitle;
    private TextView collapsingSubTitle;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        bindActivity();

        toolbar.setTitle("");
        appBarLayout.addOnOffsetChangedListener(this);

        setSupportActionBar(toolbar);
        startAlphaAnimation(title, 0, View.INVISIBLE);
        initParallaxValues();

        setUserValues();
    }

    private void setUserValues() {
        User user = getIntent().getParcelableExtra(KEY_USER);
//        toolbar.setTitle(user.getFullName());

        title.setText(user.getFullName());
        collapsingTitle.setText(user.getFullName());
        collapsingSubTitle.setText(user.description);

        Picasso.with(this).load(user.profilePic).placeholder(getResources().getDrawable(R.drawable.ic_logo_white)).fit().centerCrop().into(imageView);
        Picasso.with(this).load(AccountManager.getInstance().getloggedInSociety().societypic).placeholder(Utils.getTextDrawable(this,  AccountManager.getInstance().getloggedInSociety().name)).fit().centerCrop().into(imageparallax);

        int color  =getIntent().getIntExtra(KEY_COLOR,0);
        frameParallax.setBackgroundColor(color);


    }

    private void bindActivity() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.textview_title);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        titleContainer = (LinearLayout) findViewById(R.id.linearlayout_title);
        collapsingTitle = (TextView) findViewById(R.id.collapsing_title);
        collapsingSubTitle = (TextView) findViewById(R.id.collapsing_sub_title);

        imageparallax = (ImageView) findViewById(R.id.imageview_placeholder);
        frameParallax = (FrameLayout) findViewById(R.id.framelayout_title);

        imageView  = (ImageView)findViewById(R.id.civ);


        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        imageView.setY((float) (getResources().getDimension(R.dimen.app_bar_height) - 1.5 * actionBarHeight));

    }

    private void initParallaxValues() {
        CollapsingToolbarLayout.LayoutParams petDetailsLp =
                (CollapsingToolbarLayout.LayoutParams) imageparallax.getLayoutParams();

        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) frameParallax.getLayoutParams();

        petDetailsLp.setParallaxMultiplier(0.9f);
        petBackgroundLp.setParallaxMultiplier(0.3f);

        imageparallax.setLayoutParams(petDetailsLp);
        frameParallax.setLayoutParams(petBackgroundLp);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!isTheTitleVisible) {
                startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                isTheTitleVisible = true;
            }

        } else {

            if (isTheTitleVisible) {
                startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                isTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (isTheTitleContainerVisible) {
                startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                isTheTitleContainerVisible = false;
            }

        } else {

            if (!isTheTitleContainerVisible) {
                startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                isTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    protected void load() {

    }
}