package com.zircon.app.ui.carSearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zircon.app.R;
import com.zircon.app.model.response.CarSearchResponse;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.HTTP;

import retrofit2.Callback;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  LayoutInflater.from(getContext()).inflate(R.layout.fragment_car_search,null,false);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


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

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = inputEditText.getText().toString().trim();
                if (TextUtils.isEmpty(query)){
                    Toast.makeText(getContext(),"Enter the registration number of the car you want to search",Toast.LENGTH_SHORT).show();
                    return;
                }


                initLayout.setVisibility(View.INVISIBLE);
                progressLayout.setVisibility(View.VISIBLE);
                noResultsLayout.setVisibility(View.INVISIBLE);
                resultView.setVisibility(View.INVISIBLE);

                HTTP.getAPI().searchVehicleNumber(AccountManager.getInstance().getToken(),query).enqueue(new Callback<CarSearchResponse>() {
                    @Override
                    public void onResponse(Response<CarSearchResponse> response) {
                        if (response.isSuccess()) {
                            initLayout.setVisibility(View.INVISIBLE);
                            progressLayout.setVisibility(View.INVISIBLE);
                            noResultsLayout.setVisibility(View.INVISIBLE);
                            resultView.setVisibility(View.VISIBLE);
                        }else
                            onFailure(new Throwable(response.message()));
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        initLayout.setVisibility(View.INVISIBLE);
                        progressLayout.setVisibility(View.INVISIBLE);
                        noResultsLayout.setVisibility(View.VISIBLE);
                        resultView.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });
    }

}
