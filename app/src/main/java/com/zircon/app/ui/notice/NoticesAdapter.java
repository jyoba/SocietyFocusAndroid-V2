package com.zircon.app.ui.notice;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.model.NoticeBoard;
import com.zircon.app.model.User;
import com.zircon.app.ui.common.fragment.BaseActivity;
import com.zircon.app.ui.home.NoticeBoardHelper;
import com.zircon.app.utils.NavigationUtils;
import com.zircon.app.utils.Utils;
import com.zircon.app.utils.ui.AbsSearchListAdapter;

import java.util.ArrayList;

/**
 * Created by jikoobaruah on 30/04/17.
 */

public class NoticesAdapter extends AbsSearchListAdapter<NoticeBoard,NoticesAdapter.ViewHolder> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notice_board, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setNoticeBoard(getItem(position));
    }

    @Override
    protected ArrayList<NoticeBoard> getFilteredList(String query) {
        ArrayList<NoticeBoard> filteredList = new ArrayList<>();
        if (query == null || query.trim().length() == 0)
            filteredList = masterItems;
        else {
            int size = masterItems.size();
            for (int i = 0; i < size; i++) {
                if (masterItems.get(i).title.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(masterItems.get(i));
                }
            }
        }
        return filteredList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder( View itemView) {
            super(itemView);

        }
        public void setNoticeBoard(NoticeBoard item) {
            NoticeBoardHelper.setupView(itemView,item);
        }
    }

}
