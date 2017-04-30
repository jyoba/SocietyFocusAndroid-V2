package com.zircon.app.ui.usr;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.model.response.MembersResponse;
import com.zircon.app.ui.common.fragment.BaseDrawerActivity;
import com.zircon.app.ui.widget.ToolsWidget;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.ui.VerticalSeparator;

import retrofit2.Callback;
import retrofit2.Response;

public class UsersActivity extends BaseDrawerActivity {

    public static final String KEY_PAGE_TYPE = "kpt";
    public static final int PAGE_TYPE_RESIDENTS = -999;
    public static final int PAGE_TYPE_RWA = -998;

    private RecyclerView recyclerView;

    private SearchView searchView;

    private UsersAdapter usersAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        setupToolbar();

        switch (getIntent().getExtras().getInt(KEY_PAGE_TYPE,-1)){
            case PAGE_TYPE_RESIDENTS:
                setTitle("Residents");
                break;
            case PAGE_TYPE_RWA:
                setTitle("RWA members");
                break;
            default:
                throw new RuntimeException("Invalid page type");
        }

        setupDrawer(R.id.nav_manage);

        usersAdapter = new UsersAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.rv_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(usersAdapter);
        recyclerView.addItemDecoration(new VerticalSeparator(2));

        HTTP.getAPI().getAllUsers(AccountManager.getInstance().getToken()).enqueue(new Callback<MembersResponse>() {
            @Override
            public void onResponse(Response<MembersResponse> response) {
                usersAdapter.addAllItems(response.body().body);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });



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
                usersAdapter.filter(newText);
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





}
