package com.zircon.app.ui.usr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.model.response.MembersResponse;
import com.zircon.app.ui.common.fragment.BaseDrawerActivity;
import com.zircon.app.ui.widget.ToolsWidget;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.AuthCallbackImpl;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.NavigationUtils;
import com.zircon.app.utils.ui.VerticalSeparator;

import retrofit2.Callback;
import retrofit2.Response;

public class UsersActivity extends BaseDrawerActivity {

    private RecyclerView recyclerView;

    private SearchView searchView;

    private UsersAdapter usersAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        setupToolbar();

        setTitle("Residents");

        setupDrawer(R.id.nav_residents);

        usersAdapter = new UsersAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.rv_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(usersAdapter);
        recyclerView.addItemDecoration(new VerticalSeparator(2));

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                String phoneNumber = ((UsersAdapter.ViewHolder)viewHolder).phoneTextView.getText().toString();
                String email = ((UsersAdapter.ViewHolder)viewHolder).emailTextView.getText().toString();

                switch (swipeDir){
                    case ItemTouchHelper.RIGHT:
                        NavigationUtils.navigateToDialer(UsersActivity.this, phoneNumber);
                        usersAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        break;
                    case ItemTouchHelper.LEFT:
                        NavigationUtils.navigateToEmail(UsersActivity.this, email);
                        usersAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        break;
                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                Paint p = new Paint();
                View itemView = viewHolder.itemView;

                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.DKGRAY);
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.sym_action_call);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.GREEN);
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.sym_action_email);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
                usersAdapter.filter(newText);
                return true;
            }
        });

        return true;
    }


    @Override
    protected void load() {

        HTTP.getAPI().getAllUsers(AccountManager.getInstance().getToken()).enqueue(new AuthCallbackImpl<MembersResponse>(UsersActivity.this) {
            @Override
            public void apiSuccess(Response<MembersResponse> response) {
                usersAdapter.addAllItems(response.body().body);
            }

            @Override
            public void apiFail(Throwable t) {

            }
        });

    }
}
