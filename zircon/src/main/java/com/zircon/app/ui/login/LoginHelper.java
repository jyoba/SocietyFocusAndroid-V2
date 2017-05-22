package com.zircon.app.ui.login;

import android.content.Context;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.zircon.app.App;
import com.zircon.app.model.LoginCredentials;
import com.zircon.app.model.response.LoginResponse;
import com.zircon.app.utils.API;
import com.zircon.app.utils.AccountUtils;
import com.zircon.app.utils.DeviceUtils;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.ks.KeyStoreUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jikoobaruah on 07/04/17.
 */

class LoginHelper {

    WeakReference<ILoginHelper> callbackWeakReference;

    public LoginHelper(ILoginHelper callback) {
        callbackWeakReference  = new WeakReference<ILoginHelper>(callback);
    }

    public void loginSociety(final Context context,final LoginCredentials loginCredentials) {

        API api = HTTP.getAPI();
        if (api == null)
            return;

        Call<LoginResponse> response = api.login(loginCredentials.society, loginCredentials.username, loginCredentials.password, FirebaseInstanceId.getInstance().getToken(), DeviceUtils.getUniqueDeviceID());
        response.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response) {
                ILoginHelper callback = callbackWeakReference.get();
                if (callback != null){
                    if (response.isSuccess()) {
                        AccountUtils.saveSocietyLogin(loginCredentials.getEncrypted(KeyStoreUtils.getInstance(context)));
                        AccountUtils.saveLoggedInUser(response.body().body.userDetails.user);
                        AccountUtils.saveLoggedInSociety(response.body().body.society);
                        AccountUtils.saveAuthToken(response.body().body.token);
                        callback.onLoginSuccess(true, response.body());
                    }
                    else {
                        try {
                            callback.onLoginFail(true, new Throwable(response.errorBody().string()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Throwable t) {
                ILoginHelper callback = callbackWeakReference.get();
                if (callback != null){
                    callback.onLoginFail(true,t);
                }
            }
        });

    }


    public void setupFbLoginButton(LoginButton loginButton){

        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        loginButton.registerCallback(CallbackManager.Factory.create(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("Temp", "FB GrantedPermission " + loginResult.getRecentlyGrantedPermissions().toString() +
                        " Access Token " + loginResult.getAccessToken().getToken());
                fblogin(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {

                Log.e("FaceBook", "FB login cancelled");

                ILoginHelper callback = callbackWeakReference.get();
                if (callback != null){
                    callback.onLoginFail(true,new Throwable("FB login cancelled"));
                }

            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                Log.e("FaceBook", "FB login onError ");

                ILoginHelper callback = callbackWeakReference.get();
                if (callback != null){
                    callback.onLoginFail(true,new Throwable(error));
                }
            }
        });
    }

    private void fblogin(String token) {
        API api = HTTP.getAPI();
        if (api == null) {
            //TODO handle no inernet scenario;
            return;
        }
//        mFBLoginCall = HTTP.getAPI().fblogin(FirebaseInstanceId.getInstance().getToken()
//                , Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
//                , FBAccessToken);
    }

    public interface ILoginHelper {

        void onLoginSuccess(boolean isSocietyLogin, LoginResponse response);

        void onLoginFail(boolean isSocietyLogin, Throwable t);
    }
}
