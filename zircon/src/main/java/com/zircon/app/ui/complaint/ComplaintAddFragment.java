package com.zircon.app.ui.complaint;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.zircon.app.R;
import com.zircon.app.model.Complaint;
import com.zircon.app.model.response.ComplaintResponse;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.AuthCallbackImpl;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.NotificationUtils;
import com.zircon.app.utils.Utils;

import retrofit2.Response;

/**
 * Created by jikoobaruah on 03/05/17.
 */

public class ComplaintAddFragment extends BottomSheetDialogFragment {

    public static final String ARG_COMPLAINT = "arg_complaint";
    public static final String ARG_TITLE = "arg_title";
    private IComplaintAdd callback;

    private ImageView doneImageView;
    private EditText titleEditText;
    private EditText descEditText;
    private String title;
    private String description;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IComplaintAdd)
            callback = (IComplaintAdd) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_complaint_add, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            description = getArguments().getString(ARG_COMPLAINT, null);
            title = getArguments().getString(ARG_TITLE, null);
        }

        doneImageView = (ImageView) view.findViewById(R.id.iv_done);
        titleEditText = (EditText) view.findViewById(R.id.et_title);
        descEditText = (EditText) view.findViewById(R.id.et_content);

        if (!TextUtils.isEmpty(title)) {
            titleEditText.setText(title);
        }

        if (!TextUtils.isEmpty(description))
            descEditText.setText(description);

        doneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate())
                    return;
                dismiss();

                Complaint complaint = new Complaint();
                complaint.title = title;
                complaint.description = description;
                complaint.isSynced = false;

                if (callback != null) {
                    complaint.creationdate = Utils.getNow();
                    callback.onComplaintAdded(complaint);
                } else {
                    HTTP.getAPI().saveComplaint(AccountManager.getInstance().getToken(), complaint).enqueue(new AuthCallbackImpl<ComplaintResponse>(getContext()) {
                        @Override
                        public void apiSuccess(Response<ComplaintResponse> response) {
                            NotificationUtils.notifyComplaintRegistered(response.body().body);
                        }

                        @Override
                        public void apiFail(Throwable t) {

                        }
                    });
                }

            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    private boolean validate() {
        title = titleEditText.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            titleEditText.setError("Enter title");
            return false;
        }
        description = descEditText.getText().toString().trim();
        if (TextUtils.isEmpty(description)) {
            descEditText.setError("Enter description");
            return false;
        }

        return true;
    }

    public interface IComplaintAdd {

        void onComplaintAdded(Complaint complaint);
    }
}
