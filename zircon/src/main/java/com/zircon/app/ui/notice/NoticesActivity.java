package com.zircon.app.ui.notice;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.Menu;

import com.zircon.app.R;
import com.zircon.app.model.NoticeBoard;
import com.zircon.app.model.response.NoticeBoardResponse;
import com.zircon.app.ui.common.fragment.BaseDrawerActivity;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.AuthCallbackImpl;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.ui.VerticalSeparator;

import java.util.ArrayList;

import retrofit2.Response;

public class NoticesActivity extends BaseDrawerActivity {

    public static final String KEY_NOTICES = "notices";

    private RecyclerView recyclerView;

    private SearchView searchView;

    private NoticesAdapter noticesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        ArrayList<NoticeBoard> noticeBoards = getIntent().getExtras().getParcelableArrayList(KEY_NOTICES);

        setupToolbar();

        setTitle("Notice Board");

        setupDrawer(R.id.nav_notice);

        noticesAdapter = new NoticesAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.rv_notice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(noticesAdapter);
        recyclerView.addItemDecoration(new VerticalSeparator((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.spacing_normal), getResources().getDisplayMetrics()
        )));

        if (noticeBoards != null)
            noticesAdapter.addAllItems(noticeBoards);
        else {
            load();

        }


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
                noticesAdapter.filter(newText);
                return true;
            }
        });

        return true;
    }


    @Override
    protected void load() {

        HTTP.getAPI().getAllNotices(AccountManager.getInstance().getToken()).enqueue(new AuthCallbackImpl<NoticeBoardResponse>(NoticesActivity.this) {
            @Override
            public void apiSuccess(Response<NoticeBoardResponse> response) {
                noticesAdapter.addAllItems(response.body().body);
            }

            @Override
            public void apiFail(Throwable t) {

            }
        });
    }
}
