package com.zircon.app.ui.home;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zircon.app.R;
import com.zircon.app.model.NoticeBoard;
import com.zircon.app.model.response.NoticeBoardResponse;
import com.zircon.app.ui.widget.BubbleViewPagerIndicator;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.HTTP;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jikoobaruah on 26/04/17.
 */

class NoticeBoardHelper {

    public static void setupNoticeBoard(final View container){

        final ViewPager noticeViewPager = (ViewPager) container.findViewById(R.id.vp_notice);
        noticeViewPager.setPageMargin(25);
        noticeViewPager.setPadding(50,0,50,0);

        final BubbleViewPagerIndicator pageIndicator = (BubbleViewPagerIndicator) container.findViewById(R.id.pager_indicator);

        final Button seeAllButton = (Button) container.findViewById(R.id.btn_see_all);

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
            public void onResponse(Response<NoticeBoardResponse> response) {
                ArrayList<NoticeBoard> nbs = new ArrayList<NoticeBoard>();

                if (response.body().body.size() > 8){

                    seeAllButton.setVisibility(View.VISIBLE);
                    seeAllButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    for (int i = 0 ; i < 8 ; i++){
                        nbs.add(response.body().body.get(i));
                    }
                }else{
                    seeAllButton.setVisibility(View.GONE);
                    nbs = response.body().body;
                }

                noticeViewPager.setAdapter(new NoticeAdapter(nbs));
                pageIndicator.makeBubbles(R.drawable.bg_bubble_indicator, nbs.size());
                pageIndicator.setBubbleActive(0);
            }

            @Override
            public void onFailure(Throwable t) {

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
            container.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
