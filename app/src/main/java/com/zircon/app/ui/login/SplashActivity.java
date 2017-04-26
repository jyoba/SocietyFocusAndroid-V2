package com.zircon.app.ui.login;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zircon.app.R;
import com.zircon.app.model.response.LoginResponse;
import com.zircon.app.ui.home.HomeActivity;
import com.zircon.app.utils.AccountUtils;

public class SplashActivity extends AppCompatActivity implements LoginHelper.ILoginHelper {

    LoginHelper loginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginHelper = new LoginHelper(this);
        setContentView(R.layout.activity_splash);

        if (AccountUtils.isAutoLogin()){
            if (AccountUtils.isSocietyLogin()){
                loginHelper.loginSociety(AccountUtils.getLoginCredentials());
            }

        }else {
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


    }

    @Override
    public void onLoginSuccess(boolean isSocietyLogin, LoginResponse response) {
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    public void onLoginFail(boolean isSocietyLogin, Throwable t) {
        startActivity(new Intent(this, LoginActivity.class));

    }
}
