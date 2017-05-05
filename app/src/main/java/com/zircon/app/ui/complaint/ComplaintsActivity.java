package com.zircon.app.ui.complaint;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zircon.app.R;
import com.zircon.app.model.Complaint;
import com.zircon.app.model.NoticeBoard;
import com.zircon.app.model.response.ComplaintListResponse;
import com.zircon.app.model.response.ComplaintResponse;
import com.zircon.app.ui.common.fragment.BaseActivity;
import com.zircon.app.ui.common.fragment.BaseDrawerActivity;
import com.zircon.app.utils.API;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.NavigationUtils;
import com.zircon.app.utils.ui.VerticalSeparator;

import java.util.ArrayList;

import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintsActivity extends BaseDrawerActivity implements ComplaintAddFragment.IComplaintAdd {

    private RecyclerView recyclerView;

    private SearchView searchView;

    private ComplaintsAdapter complaintsAdapter;

    private FloatingActionButton fab;

    private boolean isComplaintSyncing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_complaints);
        setupToolbar();
        setTitle("Complaints");
        setupDrawer(R.id.nav_manage);

        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isComplaintSyncing)
                    return;
                showComplaintDialog();
            }
        });

        complaintsAdapter = new ComplaintsAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.rv_complaints);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(complaintsAdapter);
        recyclerView.addItemDecoration(new VerticalSeparator((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.spacing_normal), getResources().getDisplayMetrics()
        )));

        HTTP.getAPI().getUserComplaints(AccountManager.getInstance().getToken()).enqueue(new Callback<ComplaintListResponse>() {
            @Override
            public void onResponse(Response<ComplaintListResponse> response) {
                complaintsAdapter.addAllItems(response.body().body);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }

    private void showComplaintDialog() {
        NavigationUtils.navigateToComplaintAdd(ComplaintsActivity.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.users_main, menu);

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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onComplaintAdded(final Complaint complaint) {

        complaintsAdapter.addItem(complaintsAdapter.getItemCount(),complaint);
        complaint.creationdate = null;
        isComplaintSyncing = true;
        HTTP.getAPI().saveComplaint(AccountManager.getInstance().getToken(), complaint).enqueue(new Callback<ComplaintResponse>() {
            @Override
            public void onResponse(Response<ComplaintResponse> response) {
                isComplaintSyncing = false;
                complaintsAdapter.removeItem(complaintsAdapter.getItemCount()-1);
                ArrayList<Complaint> complaints = new ArrayList<Complaint>();
                complaints.add(response.body().body);
                complaintsAdapter.addAllItems(complaints);
            }

            @Override
            public void onFailure(Throwable t) {
                isComplaintSyncing = false;
            }
        });
    }
}
