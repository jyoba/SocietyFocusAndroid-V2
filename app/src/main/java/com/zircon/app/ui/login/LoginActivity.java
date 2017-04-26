package com.zircon.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zircon.app.R;
import com.zircon.app.model.LoginCredentials;
import com.zircon.app.model.response.LoginResponse;
import com.zircon.app.ui.home.HomeActivity;
import com.zircon.app.utils.ui.Overlay;

public class LoginActivity extends AppCompatActivity implements SocietySelectionFragment.ISocietySelectionListener, LoginHelper.ILoginHelper {

    LoginHelper loginHelper;

    private Overlay overlay;

    private EditText passwordEditText;

    private EditText usernameEditText;

    private EditText societyEditText;

    private Button loginButton;

    private String username;
    private String password;
    private String society;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginHelper = new LoginHelper(this);

        setContentView(R.layout.activity_login);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);



        passwordEditText = (EditText) findViewById(R.id.etPassword);
        usernameEditText = (EditText) findViewById(R.id.etUsername);
        societyEditText = (EditText) findViewById(R.id.etSociety);
        loginButton = (Button) findViewById(R.id.btn_login);


        societyEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSocieties();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validated()){
                    login();
                }
            }
        });

        overlay = (Overlay) findViewById(R.id.overlay);



    }

    @Override
    protected void onStart() {
        super.onStart();
        overlay.drawOverlay();

    }

    private void login() {

        loginHelper.loginSociety(new LoginCredentials(society, username, password));


    }

    private boolean validated() {
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if(TextUtils.isEmpty(username)){
            usernameEditText.setError("Please enter your email");
            return  false;
        }

        if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Please enter your password");
            return  false;
        }

        if(TextUtils.isEmpty(society)){
            societyEditText.setError("Please select your society");
            return false;
        }

        return true;


    }

    private void showSocieties() {
        BottomSheetDialogFragment bottomSheetDialogFragment = new SocietySelectionFragment();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

    }

    @Override
    public void onSocietySelected(String societyName, String societyValue) {
        societyEditText.setText(societyName);
        society = societyValue;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginHelper = null;
    }

    @Override
    public void onLoginSuccess(boolean isSocietyLogin, LoginResponse response) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();

    }

    @Override
    public void onLoginFail(boolean isSocietyLogin, Throwable t) {

    }
}
