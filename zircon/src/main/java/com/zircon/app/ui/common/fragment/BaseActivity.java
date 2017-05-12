package com.zircon.app.ui.common.fragment;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zircon.app.R;

/**
 * Created by jikoobaruah on 10/04/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    protected void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
