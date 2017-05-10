package com.zircon.app.ui.complaint;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zircon.app.R;
import com.zircon.app.model.Comment;
import com.zircon.app.model.Complaint;
import com.zircon.app.model.response.AddCommentResponse;
import com.zircon.app.model.response.ComplaintCommentResponse;
import com.zircon.app.ui.common.fragment.BaseActivity;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.AccountUtils;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.Utils;
import com.zircon.app.utils.ui.VerticalSeparator;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintDetailActivity extends BaseActivity {

    private Complaint complaint;

    private boolean isCommentSyncing = false;

    public static final String KEY_COMPLAINT = "complaint";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        complaint = getIntent().getParcelableExtra(KEY_COMPLAINT);

        TextView tileTextView = (TextView) findViewById(R.id.tv_title);
        //TODO refactore to id
        TextView authorTextView = (TextView) findViewById(R.id.tv_author);
        TextView dateTextView = (TextView) findViewById(R.id.tv_date);

        tileTextView.setText(complaint.title);
        authorTextView.setText(complaint.complaintid);

        try {
            dateTextView.setText(Utils.parseServerDate(complaint.creationdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView descTextView = (TextView) findViewById(R.id.tv_complaint);
        descTextView.setMovementMethod(new ScrollingMovementMethod());
        descTextView.setText(complaint.description);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_comments);
        recyclerView.addItemDecoration(new VerticalSeparator(2));
        recyclerView.setLayoutManager(new LinearLayoutManager(ComplaintDetailActivity.this,LinearLayoutManager.VERTICAL,false));
        final CommentsAdapter commentsAdapter = new CommentsAdapter();
        recyclerView.setAdapter(commentsAdapter);


        final EditText commentEditText = (EditText) findViewById(R.id.et_comment);
        ImageView commentAddImageView = (ImageView) findViewById(R.id.iv_send);
        commentAddImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCommentSyncing)
                    return;
                String input = commentEditText.getText().toString();
                if (TextUtils.isEmpty(input)){
                    return;
                }
                Comment comment = new Comment();
                comment.status = Comment.Status.SENDING_TO_SERVER;
                comment.complaintid = complaint.complaintid;
                comment.comment = input;
                comment.user = AccountManager.getInstance().getloggedInUser();
                commentEditText.setText("");
                commentsAdapter.addItem(0,comment);
                recyclerView.smoothScrollToPosition(0);
                isCommentSyncing = true;

                HTTP.getAPI().getAddComment(AccountManager.getInstance().getToken(),complaint.complaintid,input).enqueue(new Callback<AddCommentResponse>() {
                    @Override
                    public void onResponse(Response<AddCommentResponse> response) {
                        commentsAdapter.notifyItemChanged(0,response.body().body);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });



        HTTP.getAPI().getComplaintDetails(AccountManager.getInstance().getToken(),complaint.complaintid).enqueue(new Callback<ComplaintCommentResponse>() {
            @Override
            public void onResponse(Response<ComplaintCommentResponse> response) {

                Collections.sort(response.body().body.comments,new Comparator<Comment>() {

                    @Override
                    public int compare(Comment o1, Comment o2) {
                        return Integer.parseInt(o1.commentid)>Integer.parseInt(o2.comment) ? 1:0;
                    }
                });
                commentsAdapter.addItems(response.body().body.comments);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }

}
