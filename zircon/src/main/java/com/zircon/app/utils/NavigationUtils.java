package com.zircon.app.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.zircon.app.model.Complaint;
import com.zircon.app.model.NoticeBoard;
import com.zircon.app.model.User;
import com.zircon.app.ui.carSearch.CarSearchFragment;
import com.zircon.app.ui.common.fragment.BaseActivity;
import com.zircon.app.ui.complaint.ComplaintAddFragment;
import com.zircon.app.ui.complaint.ComplaintDetailActivity;
import com.zircon.app.ui.complaint.ComplaintsActivity;
import com.zircon.app.ui.home.HomeActivity;
import com.zircon.app.ui.login.LoginActivity;
import com.zircon.app.ui.notice.NoticesActivity;
import com.zircon.app.ui.rwa.RwaActivity;
import com.zircon.app.ui.usr.UserDetailFragment;
import com.zircon.app.ui.usr.UsersActivity;

import java.util.ArrayList;

/**
 * Created by jikoobaruah on 28/04/17.
 */

public class NavigationUtils {

    public static void navigateToResidentsPage(Context context){
        Intent intent = new Intent(context, UsersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void navigateToRWAPage(Context context){
        Intent intent = new Intent(context, RwaActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    public static void navigateToNotices(Context context, ArrayList<NoticeBoard> noticeBoards){
        Intent intent = new Intent(context, NoticesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putParcelableArrayListExtra(NoticesActivity.KEY_NOTICES,noticeBoards);
        context.startActivity(intent);
    }

    public static void navigateToComplaints(Context context){
        Intent intent = new Intent(context, ComplaintsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void navigateToComplaintAdd( BaseActivity context) {
        BottomSheetDialogFragment bottomSheetDialogFragment =  new ComplaintAddFragment();
        bottomSheetDialogFragment.show(context.getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    public static void navigateToComplaintAdd( BaseActivity context,String title, String complaint) {
        BottomSheetDialogFragment bottomSheetDialogFragment =  new ComplaintAddFragment();
        Bundle args = new Bundle();
        args.putString(ComplaintAddFragment.ARG_COMPLAINT,complaint);
        args.putString(ComplaintAddFragment.ARG_TITLE,title);
        bottomSheetDialogFragment.setArguments(args);
        bottomSheetDialogFragment.show(context.getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    public static void navigateToComplaintDetail(BaseActivity context, Complaint complaint) {
        Intent intent = new Intent(context, ComplaintDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ComplaintDetailActivity.KEY_COMPLAINT,complaint);
        context.startActivity(intent);
    }

    public static void navigateToCarSearch(BaseActivity context) {
        FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
        Fragment prev = context.getSupportFragmentManager().findFragmentByTag("cardialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = new CarSearchFragment();
        newFragment.show(ft, "cardialog");
    }

    public static void navigateToHome(BaseActivity context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void navigateToLoginForResult(BaseActivity context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivityForResult(intent,LoginActivity.AUTH_REQUEST);
    }
}
