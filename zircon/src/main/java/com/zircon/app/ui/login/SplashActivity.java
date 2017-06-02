package com.zircon.app.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.zircon.app.R;
import com.zircon.app.model.response.LoginResponse;
import com.zircon.app.ui.fb.FbHomeActivity;
import com.zircon.app.ui.home.HomeActivity;
import com.zircon.app.utils.AccountUtils;
import com.zircon.app.utils.NavigationUtils;
import com.zircon.app.utils.ui.DisplayUtils;
import com.zircon.app.utils.ui.ForceUpdateHelper;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity implements LoginHelper.ILoginHelper {

    LoginHelper loginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        loginHelper = new LoginHelper(this);
        setContentView(R.layout.activity_splash);

        switch (ForceUpdateHelper.checkUpdateStatus(SplashActivity.this)){
            case ForceUpdateHelper.UpdateType.FORCED_UPDATE :
                DisplayUtils.showUpdateDialog(SplashActivity.this,true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavigationUtils.quitApp(SplashActivity.this);
                    }
                });
                break;
            case ForceUpdateHelper.UpdateType.OPTIONAL_UPDATE :
                DisplayUtils.showUpdateDialog(SplashActivity.this, false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       checkAutoLogin();
                    }
                });
                break;
            default:
                checkAutoLogin();
        }

    }

    private void checkAutoLogin() {
        if (AccountUtils.isAutoLogin()){
            if (AccountUtils.isSocietyLogin()){
                loginHelper.loginSociety(SplashActivity.this, AccountUtils.getLoginCredentials());
            }else {
                String token = AccessToken.getCurrentAccessToken().getToken();
                if (!TextUtils.isEmpty(token))
                    loginHelper.fblogin(token);
                else
                    startTimer();
            }

        }else {
            startTimer();
        }

    }

    private void startTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent loginIntent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onLoginSuccess(boolean isSocietyLogin, LoginResponse response) {
        if (isSocietyLogin)
            startActivity(new Intent(this, HomeActivity.class));
        else
            startActivity(new Intent(this, FbHomeActivity.class));
        finish();
    }

    @Override
    public void onLoginFail(boolean isSocietyLogin, Throwable t) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }
}
