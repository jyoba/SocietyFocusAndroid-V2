package com.zircon.app.ui.carSearch;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zircon.app.R;
import com.zircon.app.model.CarSearch;
import com.zircon.app.model.response.CarSearchResponse;
import com.zircon.app.ui.common.fragment.BaseActivity;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.AuthCallbackImpl;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.NavigationUtils;
import com.zircon.app.utils.Utils;

import retrofit2.Response;

/**
 * Created by jikoobaruah on 04/05/17.
 */

public class CarSearchFragment extends DialogFragment {

    private EditText inputEditText;
    private ImageView searchImageView;

    private TextView initLayout;
    private LinearLayout progressLayout;
    private LinearLayout noResultsLayout;
    private CardView resultView;

    private TextView carNoTextView;
    private TextView carMakeTextView;
    private TextView carParkingTextView;
    private ImageView profileImageView;
    private TextView nameTextView;
    private TextView addressTextView;

    private Button complaintButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_car_search, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEditText = (EditText) view.findViewById(R.id.et_car_search);
        searchImageView = (ImageView) view.findViewById(R.id.iv_search);

        initLayout = (TextView) view.findViewById(R.id.tv_init);
        progressLayout = (LinearLayout) view.findViewById(R.id.ll_progress);
        noResultsLayout = (LinearLayout) view.findViewById(R.id.ll_no_result);
        resultView = (CardView) view.findViewById(R.id.cv_result);

        initLayout.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.INVISIBLE);
        noResultsLayout.setVisibility(View.INVISIBLE);
        resultView.setVisibility(View.INVISIBLE);


        carNoTextView = (TextView) view.findViewById(R.id.tv_car_no);
        carMakeTextView = (TextView) view.findViewById(R.id.tv_car_make);
        carParkingTextView = (TextView) view.findViewById(R.id.tv_car_parking);
        profileImageView = (ImageView) view.findViewById(R.id.iv_profile);
        nameTextView = (TextView) view.findViewById(R.id.tv_name);
        addressTextView = (TextView) view.findViewById(R.id.tv_address);

        complaintButton = (Button) view.findViewById(R.id.btn_complaint);
        complaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = inputEditText.getText().toString().trim();
                NavigationUtils.navigateToComplaintAdd((BaseActivity) getContext(), "Unregistered Car", "Unregistered Car with no : " + query + " has entered our soiety.");
            }
        });


        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String query = inputEditText.getText().toString().trim();
                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(getContext(), "Enter the registration number of the car you want to search", Toast.LENGTH_SHORT).show();
                    return;
                }


                initLayout.setVisibility(View.INVISIBLE);
                progressLayout.setVisibility(View.VISIBLE);
                noResultsLayout.setVisibility(View.INVISIBLE);
                resultView.setVisibility(View.INVISIBLE);

                HTTP.getAPI().searchVehicleNumber(AccountManager.getInstance().getToken(), query).enqueue(new AuthCallbackImpl<CarSearchResponse>(getContext()) {
                    @Override
                    public void apiSuccess(Response<CarSearchResponse> response) {
                        initLayout.setVisibility(View.INVISIBLE);
                        progressLayout.setVisibility(View.INVISIBLE);
                        noResultsLayout.setVisibility(View.INVISIBLE);
                        resultView.setVisibility(View.VISIBLE);
                        if (response.body().body != null)
                            setupResultView(response.body().body);
                        else
                            apiFail(null);
                    }

                    @Override
                    public void apiFail(Throwable t) {
                        initLayout.setVisibility(View.INVISIBLE);
                        progressLayout.setVisibility(View.INVISIBLE);
                        noResultsLayout.setVisibility(View.VISIBLE);
                        resultView.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    private void setupResultView(final CarSearch body) {

        carNoTextView.setText(body.VehicleNumber);
        carMakeTextView.setText("N/A");//body.Type);
        carParkingTextView.setText(body.ParkingSlot);
        nameTextView.setText(body.user.getFullName());
        nameTextView.setClickable(true);
        nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.navigateToUserDetailPage((BaseActivity) getActivity(), Utils.getRandomMaterialColor(getContext(), "300"), body.user);
            }
        });
        Picasso.with(getContext()).load(body.user.profilePic).placeholder(Utils.getTextDrawable(getContext(), body.user.firstname)).fit().centerCrop().into(profileImageView);

        addressTextView.setText(body.user.address);

    }

}
