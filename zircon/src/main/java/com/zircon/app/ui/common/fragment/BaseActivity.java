package com.zircon.app.ui.common.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zircon.app.R;
import com.zircon.app.ui.login.LoginActivity;
import com.zircon.app.utils.AccountManager;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by jikoobaruah on 10/04/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    private ArrayList<Fragment> activityResultFragments;

    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void logoutUI() {
        final AlertDialog alertDialog = new AlertDialog.Builder(BaseActivity.this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }

    private void logout() {
        AccountManager.getInstance().logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.AUTH_REQUEST && resultCode == RESULT_OK) {
            load();
        }
        for (Fragment f : activityResultFragments) {
            f.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected abstract void load();

    public void registerFragmentForActivityResult(Fragment fragment) {
        if (activityResultFragments == null)
            activityResultFragments = new ArrayList<>();
        activityResultFragments.add(fragment);
    }


    public void unregisterFragmentForActivityResult(Fragment fragment) {
        if (activityResultFragments != null)
            activityResultFragments.remove(fragment);
    }
}
