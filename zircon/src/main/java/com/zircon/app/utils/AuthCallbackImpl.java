package com.zircon.app.utils;

import android.content.Context;
import android.widget.Toast;

import com.zircon.app.ui.common.fragment.BaseActivity;

import java.io.IOException;
import java.lang.ref.WeakReference;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jikoobaruah on 20/05/17.
 */

public abstract class AuthCallbackImpl<T> implements Callback<T> {

    private WeakReference<Context> contextWeakReference;


    public AuthCallbackImpl(Context context) {
        contextWeakReference = new WeakReference<Context>(context);
    }

    @Override
    public final void onResponse(Response<T> response) {
        if (response == null) {
            onFailure(new Throwable("Response is null"));
            return;
        }

        if (response.isSuccess()) {
            apiSuccess(response);
            return;
        }

        if (response.code() == 401) {
            apiAuthError();
            return;
        }

        try {
            onFailure(new Throwable(response.errorBody().string()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public final void onFailure(Throwable t) {
        if (contextWeakReference == null || contextWeakReference.get() == null)
            return;

        Context context = contextWeakReference.get();
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();

        apiFail(t);

    }


    public void apiAuthError() {
        if (contextWeakReference == null || contextWeakReference.get() == null)
            return;

        Context context = contextWeakReference.get();
        NavigationUtils.navigateToLoginForResult((BaseActivity) context);
    }

    public abstract void apiSuccess(Response<T> response);

    public abstract void apiFail(Throwable t);


}
