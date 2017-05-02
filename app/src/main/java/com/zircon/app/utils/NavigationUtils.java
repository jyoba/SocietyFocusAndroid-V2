package com.zircon.app.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialogFragment;

import com.zircon.app.model.User;
import com.zircon.app.ui.common.fragment.BaseActivity;
import com.zircon.app.ui.home.HomeActivity;
import com.zircon.app.ui.rwa.RwaActivity;
import com.zircon.app.ui.usr.UserDetailFragment;
import com.zircon.app.ui.usr.UsersActivity;

/**
 * Created by jikoobaruah on 28/04/17.
 */

public class NavigationUtils {

    public static void navigateToResidentsPage(Context context){
        Intent intent = new Intent(context, UsersActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToRWAPage(Context context){
        Intent intent = new Intent(context, RwaActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToDialer(Context context, String phoneNumber) {
        String uri = "tel:" + phoneNumber.trim() ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        context.startActivity(intent);
    }

    public static void navigateToEmail(Context context, String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        context.startActivity(intent);
    }

    public static void navigateToUserDetailPage( BaseActivity context, int color,User user) {
        BottomSheetDialogFragment bottomSheetDialogFragment =  UserDetailFragment.getInstance(user,color);
        bottomSheetDialogFragment.show(context.getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }
}
