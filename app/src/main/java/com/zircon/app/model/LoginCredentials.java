package com.zircon.app.model;

import android.widget.EditText;

/**
 * Created by jikoobaruah on 07/04/17.
 */

public class LoginCredentials {

    public String society;
    public String username;
    public String password;

    public LoginCredentials(String society, String username, String password) {
        this.society = society;
        this.username = username;
        this.password = password;
    }
}
