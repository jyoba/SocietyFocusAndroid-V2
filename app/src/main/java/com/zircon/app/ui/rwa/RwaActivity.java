package com.zircon.app.ui.rwa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zircon.app.R;
import com.zircon.app.model.response.MembersResponse;
import com.zircon.app.model.response.PanelResponse;
import com.zircon.app.ui.common.fragment.BaseDrawerActivity;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.NavigationUtils;
import com.zircon.app.utils.ui.GridSeparator;
import com.zircon.app.utils.ui.VerticalSeparator;

import retrofit2.Callback;
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
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(rwaAdapter);
        recyclerView.addItemDecoration(new GridSeparator(
                (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.spacing_small), getResources().getDisplayMetrics()
                )
                ,2));

        HTTP.getAPI().getSocietyPanel(AccountManager.getInstance().getToken()).enqueue(new Callback<PanelResponse>() {
            @Override
            public void onResponse(Response<PanelResponse> response) {
                for (int i = 0 ; i < 5; i++) {
                    rwaAdapter.addAllItems(response.body().body);
                }
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
                rwaAdapter.filter(newText);
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
