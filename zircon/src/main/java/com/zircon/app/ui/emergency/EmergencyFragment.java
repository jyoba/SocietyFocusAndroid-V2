package com.zircon.app.ui.emergency;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.zircon.app.R;
import com.zircon.app.model.Emergency;
import com.zircon.app.utils.NavigationUtils;

import java.util.ArrayList;

/**
 * Created by jyotishman on 13/07/17.
 */

public class EmergencyFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private ArrayList<Emergency> emergencies;


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emergencies = Emergency.getEmergency(FirebaseRemoteConfig.getInstance().getString("emergency"));

    }

//    @Override
//    public void setupDialog(Dialog dialog, int style) {
//        super.setupDialog(dialog, style);
//        View contentView = View.inflate(getContext(), R.layout.fragment_emergency, null);
//        recyclerView = (RecyclerView) contentView.findViewById(R.id.rv_emergency);
//        recyclerView.setAdapter(new EmergencyAdapter());
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        dialog.setContentView(contentView);
//        CoordinatorLayout.LayoutParams layoutParams =
//                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
//        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
//        if (behavior != null && behavior instanceof BottomSheetBehavior) {
//            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_emergency, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_emergency);
        recyclerView.setAdapter(new EmergencyAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.ViewHolder> {


        @Override
        public EmergencyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_emergency,parent,false));
        }

        @Override
        public void onBindViewHolder(EmergencyAdapter.ViewHolder holder, int position) {
            holder.setup();
        }

        @Override
        public int getItemCount() {
            return emergencies.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            private TextView tv;
            public ViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv_emergency);
            }

            void setup(){
                tv.setText(emergencies.get(getAdapterPosition()).title);
                itemView.setClickable(true);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavigationUtils.navigateToDialer(getContext(),emergencies.get(getAdapterPosition()).number);
                    }
                });
            }
        }


    }


}


