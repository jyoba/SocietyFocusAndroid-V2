package com.zircon.app.ui.usr;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.model.User;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.Utils;
import com.zircon.app.utils.ui.TextDrawable;

/**
 * Created by jikoobaruah on 30/04/17.
 */

public class UserDetailFragment extends BottomSheetDialogFragment implements AppBarLayout.OnOffsetChangedListener {

    private LinearLayout titleContainer;

    public static final UserDetailFragment getInstance(User user,int color){
        if (user == null)
            throw  new RuntimeException("user is null");
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("k",user);
        bundle.putInt("kc",color);
        fragment.setArguments(bundle);
        return fragment;

    }

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean isTheTitleVisible          = false;
    private boolean isTheTitleContainerVisible = true;


    private Toolbar toolbar;
    private TextView title;
    private FrameLayout titleExpanedContainer;
    private AppBarLayout appBarLayout;


    private ImageView societyImageView;
    private ImageView profileImageView;
    private TextView expandedHeaderTextView;
    private TextView expandedSubHeaderTextView;

    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_user,container,false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = getArguments().getParcelable("k");
        int color = getArguments().getInt("kc");

        toolbar        = (Toolbar) view.findViewById(R.id.toolbar);
        title          = (TextView) view.findViewById(R.id.tv_title);
        titleContainer = (LinearLayout) view.findViewById(R.id.ll_title);
        titleExpanedContainer = (FrameLayout) view.findViewById(R.id.fl_expanded_content);
        appBarLayout   = (AppBarLayout) view.findViewById(R.id.abl);

        toolbar.setBackgroundColor(color);
        titleExpanedContainer.setBackgroundColor(color);
        appBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(toolbar, 0, View.INVISIBLE);

        societyImageView = (ImageView) view.findViewById(R.id.iv_society_bg);
        profileImageView = (ImageView) view.findViewById(R.id.iv_profile);

        expandedHeaderTextView = (TextView) view.findViewById(R.id.tv_header);
        expandedSubHeaderTextView = (TextView) view.findViewById(R.id.tv_sub_header);

        Picasso.with(getContext()).load(user.profilePic).placeholder(Utils.getTextDrawable(getContext(),user.firstname)).fit().into(profileImageView);
        Picasso.with(getContext()).load(AccountManager.getInstance().getloggedInSociety().societypic).placeholder(Utils.getTextDrawable(getContext(),user.firstname)).fit().into(societyImageView);

        expandedHeaderTextView.setText(user.getFullName());
        expandedSubHeaderTextView.setText(user.getOccupation());
        title.setText(user.getFullName());


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

            if(!isTheTitleVisible) {
                startAlphaAnimation(toolbar, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                isTheTitleVisible = true;
            }

        } else {

            if (isTheTitleVisible) {
                startAlphaAnimation(toolbar, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                isTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(isTheTitleContainerVisible) {
                startAlphaAnimation(titleExpanedContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                isTheTitleContainerVisible = false;
            }

        } else {

            if (!isTheTitleContainerVisible) {
                startAlphaAnimation(titleExpanedContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                isTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
