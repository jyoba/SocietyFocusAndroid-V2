package com.zircon.app.ui.complaint;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.model.Complaint;
import com.zircon.app.ui.common.fragment.BaseActivity;
import com.zircon.app.utils.NavigationUtils;
import com.zircon.app.utils.Utils;
import com.zircon.app.utils.ui.AbsSearchListAdapter;
import com.zircon.app.utils.ui.DisplayUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jikoobaruah on 30/04/17.
 */

public class ComplaintsAdapter extends AbsSearchListAdapter<Complaint, ComplaintsAdapter.ViewHolder> {


    private IComplaintsAdapter callback;

    public ComplaintsAdapter(IComplaintsAdapter callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_complaint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setComplaint();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads != null && payloads.size() > 0) {
            getItem(position).setFromObject((Complaint) payloads.get(0));
        }
        holder.setComplaint();


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

    public void clear() {
        masterItems.clear();
        notifyDataSetChanged();
    }


    public interface IComplaintsAdapter {
        void onTryAgain(Complaint complaint, int position);
    }

    public static class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageHolder> {
        private ArrayList<String> imgUrls = new ArrayList<>();

        @Override
        public ImagesAdapter.ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image, parent, false));
        }

        @Override
        public void onBindViewHolder(ImagesAdapter.ImageHolder holder, int position) {
            holder.setUp();
        }

        @Override
        public int getItemCount() {
            return imgUrls.size();
        }

        public void addItems(ArrayList<String> imgUrls) {
            int start = this.imgUrls.size();
            this.imgUrls.addAll(imgUrls);
            notifyItemRangeInserted(start, imgUrls.size());
        }

        public void clear() {
            int size = imgUrls.size();
            imgUrls.clear();
            notifyItemRangeRemoved(0, size);
        }

        public class ImageHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ImageHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
            }

            public void setUp() {
                Picasso.with(itemView.getContext()).load(imgUrls.get(getAdapterPosition())).fit().into(imageView);
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImagesAdapter adapter;
        TextView idTextView;
        TextView titleTextView;
        TextView dateTextView;
        TextView descTextView;
        TextView statusTextView;
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            idTextView = (TextView) itemView.findViewById(R.id.tv_id);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            dateTextView = (TextView) itemView.findViewById(R.id.tv_date);
            descTextView = (TextView) itemView.findViewById(R.id.tv_desc);
            statusTextView = (TextView) itemView.findViewById(R.id.tv_status);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_complaint_imgs);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            adapter = new ImagesAdapter();
            recyclerView.setAdapter(adapter);

        }

        public void setComplaint() {
            final Complaint item = getItem(getAdapterPosition());


            if (item.imgUrls == null )
            for (int i = 0; i < getAdapterPosition() % 4; i++) {
                item.addImageUrl("https://cdn.pixabay.com/photo/2013/07/12/19/22/iris-154659_1280.png");
            }

            adapter.clear();
            if (item.imgUrls != null && item.imgUrls.size() > 0) {
                adapter.addItems(item.imgUrls);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.GONE);
            }

            idTextView.setText(item.complaintid);
            titleTextView.setText(item.title);
            if (item.isSynced && !item.isSyncFail) {
                try {
                    dateTextView.setText(Utils.parseServerDate(item.creationdate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                dateTextView.setText(item.creationdate);
            }
            descTextView.setText(item.description);

            statusTextView.setBackgroundColor(statusTextView.getContext().getResources().getColor(android.R.color.transparent));
            statusTextView.setVisibility(View.VISIBLE);
            statusTextView.setClickable(false);
            statusTextView.setOnClickListener(null);
            if (!item.isSynced) {
                statusTextView.setText("Sending to server...");
            } else if (item.isSyncFail) {
                statusTextView.setText("Sending to server failed. Try again.");
                statusTextView.setClickable(true);
                statusTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback != null)
                            callback.onTryAgain(item, getAdapterPosition());
                    }
                });
            } else {
                DisplayUtils.setComplaintStatus(statusTextView, item.status);
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationUtils.navigateToComplaintDetail((BaseActivity) v.getContext(), getItem(getAdapterPosition()
                    ));
                }
            });


        }
    }
}
