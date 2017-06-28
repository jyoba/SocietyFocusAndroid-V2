package com.zircon.app.ui.notice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.model.NoticeBoard;
import com.zircon.app.utils.Utils;
import com.zircon.app.utils.ui.AbsSearchListAdapter;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by jikoobaruah on 30/04/17.
 */

public class NoticesAdapter extends AbsSearchListAdapter<NoticeBoard, NoticesAdapter.ViewHolder> {


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

        TextView titleTextView;
        TextView dateTextView;
        TextView desTextView;
        ImageView picImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            dateTextView = (TextView) itemView.findViewById(R.id.tv_date);
            desTextView = (TextView) itemView.findViewById(R.id.tv_desc);
            picImageView = (ImageView) itemView.findViewById(R.id.iv_pic);


        }

        public void setNoticeBoard(NoticeBoard noticeBoard) {

            titleTextView.setText(noticeBoard.title);
            try {
                dateTextView.setText(Utils.parseServerDate(noticeBoard.creationDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            desTextView.setText(noticeBoard.description);
            Picasso.with(picImageView.getContext()).load(noticeBoard.imageUrl1).placeholder(Utils.getTextDrawable(picImageView.getContext(), noticeBoard.title)).fit().into(picImageView);

        }
    }

}
