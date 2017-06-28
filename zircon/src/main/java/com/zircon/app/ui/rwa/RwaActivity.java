package com.zircon.app.ui.rwa;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.Menu;

import com.zircon.app.R;
import com.zircon.app.model.response.PanelResponse;
import com.zircon.app.ui.common.fragment.BaseDrawerActivity;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.AuthCallbackImpl;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.ui.GridSeparator;

import retrofit2.Response;

public class RwaActivity extends BaseDrawerActivity {

    private RecyclerView recyclerView;

    private SearchView searchView;

    private RwaAdapter rwaAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        setupToolbar();

        setTitle("RWA");

        setupDrawer(R.id.nav_rwa);

        rwaAdapter = new RwaAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.rv_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(rwaAdapter);
        recyclerView.addItemDecoration(new GridSeparator(
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.spacing_small), getResources().getDisplayMetrics()
                )
                , 2));

        load();


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
                rwaAdapter.filter(newText);
                return true;
            }
        });

        return true;
    }


    @Override
    protected void load() {

        HTTP.getAPI().getSocietyPanel(AccountManager.getInstance().getToken()).enqueue(new AuthCallbackImpl<PanelResponse>(RwaActivity.this) {
            @Override
            public void apiSuccess(Response<PanelResponse> response) {
                rwaAdapter.addAllItems(response.body().body);
            }

            @Override
            public void apiFail(Throwable t) {

            }
        });
    }
}
