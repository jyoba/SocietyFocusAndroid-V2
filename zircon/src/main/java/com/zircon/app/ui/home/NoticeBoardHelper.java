package com.zircon.app.ui.home;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.model.NoticeBoard;
import com.zircon.app.model.response.NoticeBoardResponse;
import com.zircon.app.ui.widget.BubbleViewPagerIndicator;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.NavigationUtils;
import com.zircon.app.utils.Utils;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jikoobaruah on 26/04/17.
 */

public class NoticeBoardHelper {

    public static void setupNoticeBoard(final View container){

        final ViewPager noticeViewPager = (ViewPager) container.findViewById(R.id.vp_notice);
        noticeViewPager.setPageMargin(25);
        noticeViewPager.setPadding(50,0,50,0);

        final BubbleViewPagerIndicator pageIndicator = (BubbleViewPagerIndicator) container.findViewById(R.id.pager_indicator);

        final TextView seeAllView = (TextView) container.findViewById(R.id.tv_see_all);

        noticeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                startAutoScroll(noticeViewPager);
                pageIndicator.setBubbleActive(i);
            }



            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        HTTP.getAPI().getAllNotices(AccountManager.getInstance().getToken()).enqueue(new Callback<NoticeBoardResponse>() {
            @Override
            public void onResponse(final Response<NoticeBoardResponse> response) {
                ArrayList<NoticeBoard> nbs = new ArrayList<NoticeBoard>();

                if (response.body().body.size() > 8){

                    seeAllView.setVisibility(View.VISIBLE);
                    seeAllView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NavigationUtils.navigateToNotices(v.getContext(),response.body().body);

                        }
                    });

                    for (int i = 0 ; i < 8 ; i++){
                        nbs.add(response.body().body.get(i));
                    }
                }else{
                    seeAllView.setVisibility(View.GONE);
                    nbs = response.body().body;
                }

                noticeViewPager.setAdapter(new NoticeAdapter(nbs));
                pageIndicator.makeBubbles(R.drawable.bg_bubble_indicator, nbs.size());
                pageIndicator.setBubbleActive(0);
            }

            @Override
            public void onFailure(Throwable t) {

                container.setVisibility(View.GONE);

            }
        });
    }

    private static void startAutoScroll(ViewPager noticeViewPager) {
        new Timer().schedule(new AutoScrollTask(noticeViewPager),8*1000);
    }


    private static class AutoScrollTask extends TimerTask {

        private WeakReference<ViewPager> pager;

        public AutoScrollTask(ViewPager vp){
            pager = new WeakReference<ViewPager>(vp);
        }

        @Override
        public void run() {

            if (pager.get() != null){
               if (pager.get().getContext() == null)
                   return;


                ((Activity)pager.get().getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pager.get().getCurrentItem() < (pager.get().getChildCount() - 1))
                            pager.get().setCurrentItem(pager.get().getCurrentItem() + 1, true);
                        else
                            pager.get().setCurrentItem(0, true);
                    }
                });
            }



        }
    }



    private static class NoticeAdapter extends PagerAdapter {

        public NoticeAdapter(ArrayList<NoticeBoard> list) {
            this.list = list;
        }

        private ArrayList<NoticeBoard> list ;

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
            setupView(layout,list.get(position));
            container.addView(layout);
            return layout;
        }



        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }


    public static void setupView(View layout, NoticeBoard noticeBoard) {
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
        Picasso.with(layout.getContext()).load(noticeBoard.imageUrl1).placeholder(Utils.getTextDrawable(layout.getContext(),noticeBoard.title)).fit().into(picImageView);

    }
}