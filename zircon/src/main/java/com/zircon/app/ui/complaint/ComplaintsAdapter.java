package com.zircon.app.ui.complaint;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zircon.app.R;
import com.zircon.app.model.Complaint;
import com.zircon.app.model.NoticeBoard;
import com.zircon.app.ui.common.fragment.BaseActivity;
import com.zircon.app.ui.home.NoticeBoardHelper;
import com.zircon.app.utils.NavigationUtils;
import com.zircon.app.utils.Utils;
import com.zircon.app.utils.ui.AbsSearchListAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jikoobaruah on 30/04/17.
 */

public class ComplaintsAdapter extends AbsSearchListAdapter<Complaint,ComplaintsAdapter.ViewHolder> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_complaint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setComplaint(getItem(position));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads != null && payloads.size() > 0){
            getItem(position).setFromObject((Complaint)payloads.get(0));
        }
        holder.setComplaint(getItem(position));


    }

    @Override
    protected ArrayList<Complaint> getFilteredList(String query) {
        ArrayList<Complaint> filteredList = new ArrayList<>();
        if (query == null || query.trim().length() == 0)
            filteredList = masterItems;
        else {
            int size = masterItems.size();
            for (int i = 0; i < size; i++) {
                if (masterItems.get(i).title.toLowerCase().contains(query.toLowerCase()) || masterItems.get(i).complaintid.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(masterItems.get(i));
                }
            }
        }
        return filteredList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView idTextView;
        TextView titleTextView;
        TextView dateTextView;
        TextView descTextView;

        public ViewHolder( View itemView) {
            super(itemView);
            idTextView = (TextView) itemView.findViewById(R.id.tv_id);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            dateTextView = (TextView) itemView.findViewById(R.id.tv_date);
            descTextView = (TextView) itemView.findViewById(R.id.tv_desc);

        }

        public void setComplaint(Complaint item) {
            idTextView.setText(item.complaintid);
            titleTextView.setText(item.title);
            if (item.isSynced) {
                try {
                    dateTextView.setText(Utils.parseServerDate(item.creationdate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                dateTextView.setText(item.creationdate);
            }
            descTextView.setText(item.description);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationUtils.navigateToComplaintDetail((BaseActivity) v.getContext(),getItem(getAdapterPosition()
                    ));
                }
            });


        }
    }

}
