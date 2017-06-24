package com.zircon.app.ui.complaint;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zircon.app.R;
import com.zircon.app.model.Comment;
import com.zircon.app.model.Complaint;
import com.zircon.app.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jikoobaruah on 05/05/17.
 */

class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentHolder> {

    private ICommentsAdapter callback;

    private ArrayList<Comment> comments;

    public CommentsAdapter(ICommentsAdapter callback){
        this.callback = callback;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        holder.setup();
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position, List<Object> payloads) {
        if (payloads != null && payloads.size()>0){
            comments.get(position).setFromObject((Comment) payloads.get(0));
        }
        holder.setup();
    }

    @Override
    public int getItemCount() {
        return comments == null ? 0 : comments.size();
    }

    public void addItem(Comment comment) {
        if (comments == null)
            comments = new ArrayList<>();

        comments.add(comment);

        notifyItemInserted(comments.size() - 1);

    }

    public void addItem(int position,Comment comment) {
        if (comment == null)
            return;
        if (comments == null)
            comments = new ArrayList<>();

        if (position > comments.size())
            throw new IndexOutOfBoundsException();

        comments.add(position, comment);

        notifyItemInserted(position);

    }

    public void addItems(ArrayList<Comment> comments) {
        if (comments == null)
            return;
        if (this.comments == null)
            this.comments = new ArrayList<>();

        int start = comments.size();

        this.comments.addAll(comments);
        notifyItemRangeInserted(start, comments.size());
    }


    public class CommentHolder extends RecyclerView.ViewHolder {

        private TextView authorTextView;
        private TextView dateTextView;
        private TextView commentTextView;
        private TextView statusTextView;

        public CommentHolder(View itemView) {
            super(itemView);
            authorTextView = (TextView) itemView.findViewById(R.id.tv_author);
            dateTextView = (TextView) itemView.findViewById(R.id.tv_date);
            commentTextView = (TextView) itemView.findViewById(R.id.tv_comment);
            statusTextView = (TextView) itemView.findViewById(R.id.tv_sync);
        }

        public void setup() {
            final Comment comment = comments.get(getAdapterPosition());
            authorTextView.setText(comment.user.getFullName());

            try {
                if (comment.creationdate != null)
                    dateTextView.setText(Utils.parseServerDate(comment.creationdate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            statusTextView.setClickable(false);
            statusTextView.setOnClickListener(null);
            if (comment.status == Comment.Status.SENDING_TO_SERVER){
                statusTextView.setVisibility(View.VISIBLE);
                statusTextView.setText("Sending to server...");
            }else  if (comment.status == Comment.Status.SENDING_TO_SERVER_FAIL) {
                statusTextView.setVisibility(View.VISIBLE);
                statusTextView.setText("Sending to server failed. Try again.");
                statusTextView.setClickable(true);
                statusTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback != null)
                            callback.onTryAgain(comment,getAdapterPosition());
                    }
                });
            }else{
                statusTextView.setVisibility(View.GONE);
                statusTextView.setText("Succesfully sent.");
            }
            commentTextView.setText(comment.comment);
        }

    }

    public interface ICommentsAdapter {
        void onTryAgain(Comment comment,int position);
    }
}
