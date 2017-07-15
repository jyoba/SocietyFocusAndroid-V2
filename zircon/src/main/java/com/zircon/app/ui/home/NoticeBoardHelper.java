package com.zircon.app.ui.home;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.model.NoticeBoard;
import com.zircon.app.model.response.NoticeBoardResponse;
import com.zircon.app.ui.widget.BubbleViewPagerIndicator;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.AuthCallbackImpl;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.NavigationUtils;
import com.zircon.app.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by jikoobaruah on 26/04/17.
 */

public class NoticeBoardHelper {

    public static void setupNoticeBoard(final View container) {

        final ViewPager noticeViewPager = (ViewPager) container.findViewById(R.id.vp_notice);
        noticeViewPager.setPageMargin(25);
        noticeViewPager.setPadding(50, 0, 50, 0);

        final BubbleViewPagerIndicator pageIndicator = (BubbleViewPagerIndicator) container.findViewById(R.id.pager_indicator);

        final TextView seeAllView = (TextView) container.findViewById(R.id.tv_see_all);

        noticeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                pageIndicator.setBubbleActive(i);
                startAutoScroll(noticeViewPager);
            }


            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        HTTP.getAPI().getAllNotices(AccountManager.getInstance().getToken()).enqueue(new AuthCallbackImpl<NoticeBoardResponse>(container.getContext()) {
            @Override
            public void apiSuccess(final Response<NoticeBoardResponse> response) {

                ArrayList<NoticeBoard> nbs = new ArrayList<NoticeBoard>();

                if (response.body().body.size() > 8) {

                    seeAllView.setVisibility(View.VISIBLE);
                    seeAllView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NavigationUtils.navigateToNotices(v.getContext(), response.body().body);

                        }
                    });

                    for (int i = 0; i < 8; i++) {
                        nbs.add(response.body().body.get(i));
                    }
                } else {
                    seeAllView.setVisibility(View.GONE);
                    nbs = response.body().body;
                }

                noticeViewPager.setAdapter(new NoticeAdapter(nbs));
                noticeViewPager.setOffscreenPageLimit(nbs.size());
                startAutoScroll(noticeViewPager);
                pageIndicator.makeBubbles(R.drawable.bg_bubble_indicator, nbs.size());
                pageIndicator.setBubbleActive(0);
            }

            @Override
            public void apiFail(Throwable t) {
                container.setVisibility(View.GONE);

            }
        });
    }

    private static Runnable autoScrollRunnable;

    private static void startAutoScroll(final ViewPager noticeViewPager) {

        if (autoScrollRunnable == null){
            autoScrollRunnable = new Runnable() {
                @Override
                public void run() {
                    if (noticeViewPager.getCurrentItem() < (noticeViewPager.getChildCount() - 1))
                        noticeViewPager.setCurrentItem(noticeViewPager.getCurrentItem() + 1, true);
                    else
                        noticeViewPager.setCurrentItem(0, true);
                }
            };
        }

        noticeViewPager.removeCallbacks(autoScrollRunnable);

        noticeViewPager.postDelayed(autoScrollRunnable, 3000);
    }



    private static class NoticeAdapter extends PagerAdapter {

        private ArrayList<NoticeBoard> list;

        public NoticeAdapter(ArrayList<NoticeBoard> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View layout = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_notice_board, container, false);
            setupView(layout, list.get(position));
            container.addView(layout);
            return layout;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        private void setupView(View layout, NoticeBoard noticeBoard) {
            TextView titleTextView = (TextView) layout.findViewById(R.id.tv_title);
            TextView dateTextView = (TextView) layout.findViewById(R.id.tv_date);
            TextView desTextView = (TextView) layout.findViewById(R.id.tv_desc);
            ImageView picImageView = (ImageView) layout.findViewById(R.id.iv_pic);

            titleTextView.setText(noticeBoard.title);
            try {
                dateTextView.setText(Utils.parseServerDate(noticeBoard.creationDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            desTextView.setText(noticeBoard.description);
            Picasso.with(layout.getContext()).load(noticeBoard.imageUrl1).placeholder(Utils.getTextDrawable(layout.getContext(), noticeBoard.title)).fit().into(picImageView);

        }

    }


}
