package com.zircon.app.ui.complaint;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zircon.app.R;
import com.zircon.app.model.Comment;
import com.zircon.app.model.Complaint;
import com.zircon.app.model.response.AddCommentResponse;
import com.zircon.app.model.response.ComplaintCommentResponse;
import com.zircon.app.model.response.ComplaintResponse;
import com.zircon.app.ui.common.fragment.BaseActivity;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.AuthCallbackImpl;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.Utils;
import com.zircon.app.utils.ui.DisplayUtils;
import com.zircon.app.utils.ui.VerticalSeparator;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Response;

public class ComplaintDetailActivity extends BaseActivity implements CommentsAdapter.ICommentsAdapter {

    public static final String KEY_COMPLAINT = "complaint";
    private Complaint complaint;
    private CommentsAdapter commentsAdapter;
    private boolean isCommentSyncing = false;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        complaint = getIntent().getParcelableExtra(KEY_COMPLAINT);

        TextView tileTextView = (TextView) findViewById(R.id.tv_title);
        TextView idTextView = (TextView) findViewById(R.id.tv_complaint_id);
        TextView dateTextView = (TextView) findViewById(R.id.tv_date);
        setStatus(false);

        tileTextView.setText(complaint.title);
        idTextView.setText("COMPLAINT ID :" + complaint.complaintid);

        try {
            dateTextView.setText(Utils.parseServerDate(complaint.creationdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView descTextView = (TextView) findViewById(R.id.tv_complaint);
        descTextView.setMovementMethod(new ScrollingMovementMethod());
        descTextView.setText(complaint.description);

        final FloatingActionButton closeComplaint = (FloatingActionButton) findViewById(R.id.fab_close);
        closeComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(complaint.status == Complaint.Status.NEW || complaint.status == Complaint.Status.IN_PROGRESS);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rv_comments);
        recyclerView.addItemDecoration(new VerticalSeparator(2));
        recyclerView.setLayoutManager(new LinearLayoutManager(ComplaintDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        commentsAdapter = new CommentsAdapter(ComplaintDetailActivity.this);
        recyclerView.setAdapter(commentsAdapter);


        final EditText commentEditText = (EditText) findViewById(R.id.et_comment);
        ImageView commentAddImageView = (ImageView) findViewById(R.id.iv_send);
        commentAddImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment(commentEditText);
            }
        });

        load();

    }


    private void addComment(EditText commentEditText) {

        String input = commentEditText.getText().toString();
        if (TextUtils.isEmpty(input)) {
            return;
        }
        commentEditText.setText("");
        addComment(input);

    }

    private void addComment(String input) {
        if (isCommentSyncing)
            return;
        final Comment comment = new Comment();
        comment.status = Comment.Status.SENDING_TO_SERVER;
        comment.complaintid = complaint.complaintid;
        comment.comment = input;
        comment.user = AccountManager.getInstance().getloggedInUser();
        commentsAdapter.addItem(0, comment);
        recyclerView.smoothScrollToPosition(0);
        onTryAgain(comment, 0);
    }


    private void showConfirmDialog(final boolean isClose) {

        DisplayUtils.showYesNoDialog(ComplaintDetailActivity.this,
                isClose ? "Are you sure you want to close this complaint?" : "Are you sure you want to reopen this complaint?",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeOpenComplaint(isClose);
                    }
                },
                null
        );
    }

    private void closeOpenComplaint(boolean isClose) {
        final int oldStatus = complaint.status;
        addComment(isClose ? "I am closing this complaint" : "I am reopening this complaint");
        complaint.status = isClose ? Complaint.Status.COMPLETED : Complaint.Status.NEW;
        setStatus(true);
        HTTP.getAPI().modifyComplaint(AccountManager.getInstance().getToken(), complaint).enqueue(new AuthCallbackImpl<ComplaintResponse>(ComplaintDetailActivity.this) {
            @Override
            public void apiSuccess(Response<ComplaintResponse> response) {
                complaint = response.body().body;
                setStatus(false);
            }

            @Override
            public void apiFail(Throwable t) {
                Toast.makeText(ComplaintDetailActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                complaint.status = oldStatus;
                setStatus(false);
            }
        });

    }

    private void setStatus(boolean isSyncing) {
        TextView statusTextView = (TextView) findViewById(R.id.tv_status);
        if (isSyncing) {
            statusTextView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            statusTextView.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
            statusTextView.setText("Syncing...");
            return;
        }
        DisplayUtils.setComplaintStatus(statusTextView, complaint.status);

    }

    @Override
    protected void load() {
        HTTP.getAPI().getComplaintDetails(AccountManager.getInstance().getToken(), complaint.complaintid).enqueue(new AuthCallbackImpl<ComplaintCommentResponse>(ComplaintDetailActivity.this) {
            @Override
            public void apiSuccess(Response<ComplaintCommentResponse> response) {
                Collections.sort(response.body().body.comments, new Comparator<Comment>() {

                    @Override
                    public int compare(Comment o1, Comment o2) {
                        return Integer.parseInt(o1.commentid) > Integer.parseInt(o2.commentid) ? -1 : 1;
                    }
                });
                commentsAdapter.addItems(response.body().body.comments);
            }

            @Override
            public void apiFail(Throwable t) {

            }
        });
    }

    @Override
    public void onTryAgain(final Comment comment, final int position) {
        isCommentSyncing = true;
        HTTP.getAPI().getAddComment(AccountManager.getInstance().getToken(), complaint.complaintid, comment.comment).enqueue(new AuthCallbackImpl<AddCommentResponse>(ComplaintDetailActivity.this) {
            @Override
            public void apiSuccess(Response<AddCommentResponse> response) {
                commentsAdapter.notifyItemChanged(position, response.body().body);
                isCommentSyncing = false;
            }

            @Override
            public void apiFail(Throwable t) {
                comment.status = Comment.Status.SENDING_TO_SERVER_FAIL;
                commentsAdapter.notifyItemChanged(position, comment);
                isCommentSyncing = false;
            }
        });
    }
}
