package com.zircon.app.ui.complaint;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zircon.app.R;
import com.zircon.app.model.Complaint;
import com.zircon.app.model.response.ComplaintListResponse;
import com.zircon.app.model.response.ComplaintResponse;
import com.zircon.app.ui.common.fragment.BaseDrawerActivity;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.AuthCallbackImpl;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.NavigationUtils;
import com.zircon.app.utils.NotificationUtils;
import com.zircon.app.utils.ui.VerticalSeparator;

import java.util.Collections;

import retrofit2.Response;

public class ComplaintsActivity extends BaseDrawerActivity implements ComplaintAddFragment.IComplaintAdd, ComplaintsAdapter.IComplaintsAdapter {

    private RecyclerView recyclerView;

    private SearchView searchView;

    private ComplaintsAdapter complaintsAdapter;

    private FloatingActionButton fab;

    private boolean isComplaintSyncing = false;
    private String filterString = "ALL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_complaints);
        setupToolbar();
        setTitle("Complaints");
        setupDrawer(R.id.nav_complaints);

        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isComplaintSyncing)
                    return;
                showComplaintDialog();
            }
        });

        complaintsAdapter = new ComplaintsAdapter(ComplaintsActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_complaints);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(complaintsAdapter);
        recyclerView.addItemDecoration(new VerticalSeparator((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.spacing_normal), getResources().getDisplayMetrics()
        )));


    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void showComplaintDialog() {
        NavigationUtils.navigateToComplaintAdd(ComplaintsActivity.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complaints_main, menu);

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                complaintsAdapter.filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                showFilterDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ComplaintsActivity.this);
        View dialogView = LayoutInflater.from(ComplaintsActivity.this).inflate(R.layout.popup_list, null, false);

        RecyclerView rv = (RecyclerView) dialogView.findViewById(R.id.rv_popup_list);
        rv.setLayoutManager(new LinearLayoutManager(ComplaintsActivity.this, LinearLayoutManager.VERTICAL, true));
        FilterAdapter filterAdapter = new FilterAdapter();
        rv.setAdapter(filterAdapter);


        dialogBuilder.setView(dialogView);


        final AlertDialog alertDialog = dialogBuilder.create();


        alertDialog.show();
    }

    @Override
    public void onComplaintAdded(final Complaint complaint) {

        complaintsAdapter.addItemAtTop(complaint);
        recyclerView.smoothScrollToPosition(0);
        complaint.creationdate = null;
        onTryAgain(complaint, 0);

    }

    @Override
    protected void load() {
        complaintsAdapter.clear();
        HTTP.getAPI().getUserComplaints(AccountManager.getInstance().getToken(), filterString).enqueue(new AuthCallbackImpl<ComplaintListResponse>(ComplaintsActivity.this) {
            @Override
            public void apiSuccess(Response<ComplaintListResponse> response) {
                Collections.sort(response.body().body, Complaint.getDescendingIdComparator());
                complaintsAdapter.addAllItems(response.body().body);
            }

            @Override
            public void apiFail(Throwable t) {

            }
        });
    }

    @Override
    public void onTryAgain(final Complaint complaint, final int position) {
        isComplaintSyncing = true;
        HTTP.getAPI().saveComplaint(AccountManager.getInstance().getToken(), complaint).enqueue(new AuthCallbackImpl<ComplaintResponse>(ComplaintsActivity.this) {
            @Override
            public void apiSuccess(Response<ComplaintResponse> response) {
                isComplaintSyncing = false;
                complaintsAdapter.notifyItemChanged(position, response.body().body);
                NotificationUtils.notifyComplaintRegistered(response.body().body);
            }

            @Override
            public void apiFail(Throwable t) {
                isComplaintSyncing = false;
                complaint.isSynced = true;
                complaint.isSyncFail = true;
                complaintsAdapter.notifyItemChanged(position, complaint);
            }
        });
    }

    private class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {

        String[] FILTERS = {"ALL", "OPEN", "CLOSED", "IN PROGRESS", "REJECTED"};


        @Override
        public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView view = new TextView(ComplaintsActivity.this);
            return new FilterHolder(view);
        }

        @Override
        public void onBindViewHolder(FilterHolder holder, int position) {
            holder.setView();
        }

        @Override
        public int getItemCount() {
            return FILTERS.length;
        }


        class FilterHolder extends RecyclerView.ViewHolder {


            FilterHolder(TextView view) {
                super(view);
            }

            void setView() {
                ((TextView) itemView).setText(FILTERS[getAdapterPosition()]);
                itemView.setClickable(true);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterString = FILTERS[getAdapterPosition()];
                        load();
                    }
                });
            }

        }
    }
}
