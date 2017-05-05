package com.zircon.app.ui.complaint;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zircon.app.R;
import com.zircon.app.model.Comment;
import com.zircon.app.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by jikoobaruah on 05/05/17.
 */

class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentHolder> {

    private ArrayList<Comment> comments;

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment,parent,false));
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        holder.setup();
    }

    @Override
    public int getItemCount() {
        return comments==null?0:comments.size();
    }

    public void addItem(Comment comment){
        if (comments == null)
            comments = new ArrayList<>();

        comments.add(comment);

        notifyItemInserted(comments.size()-1);

    }

    public void addItem(Comment comment,int position){
        if (comment == null)
            return;
        if (comments == null)
            comments = new ArrayList<>();

        if (position>comments.size()-1)
            throw new IndexOutOfBoundsException();

        comments.add(position,comment);

        notifyItemInserted(position);

    }

    public void addItems(ArrayList<Comment> comments){
        if (comments == null)
            return;
        if (this.comments == null)
            this.comments = new ArrayList<>();

        int start = comments.size();

        this.comments.addAll(comments);
        notifyItemRangeInserted(start,comments.size());
    }


    public class CommentHolder extends RecyclerView.ViewHolder{

        private TextView authorTextView;
        private TextView dateTextView;
        private TextView commentTextView;

        public CommentHolder(View itemView) {
            super(itemView);
            authorTextView = (TextView) itemView.findViewById(R.id.tv_author);
            dateTextView = (TextView) itemView.findViewById(R.id.tv_date);
            commentTextView = (TextView) itemView.findViewById(R.id.tv_comment);
        }

        public void setup(){
            Comment comment = comments.get(getAdapterPosition());
            authorTextView.setText(comment.user.getFullName());
            try {
                dateTextView.setText(Utils.parseServerDate(comment.creationdate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            commentTextView.setText(comment.comment);
        }

    }

}
