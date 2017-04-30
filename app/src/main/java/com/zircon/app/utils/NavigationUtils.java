package com.zircon.app.utils;

import android.content.Context;
import android.content.Intent;

import com.zircon.app.ui.home.HomeActivity;
import com.zircon.app.ui.usr.UsersActivity;

/**
 * Created by jikoobaruah on 28/04/17.
 */

public class NavigationUtils {

    public static void navigateToUserPage(Context context , int type){
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(UsersActivity.KEY_PAGE_TYPE,type);
        context.startActivity(intent);

    }
}
