package com.zircon.app.model;

import android.widget.EditText;

import com.zircon.app.App;
import com.zircon.app.utils.ks.KeyStoreUtils;

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

    public LoginCredentials getEncrypted(KeyStoreUtils keyStoreUtils) {
//        society = keyStoreUtils.encrypt(App.appInstance,society);
//        username = keyStoreUtils.encrypt(App.appInstance,username);
//        password = keyStoreUtils.encrypt(App.appInstance,password);
        return this;
    }

    public LoginCredentials getDecrypted(KeyStoreUtils keyStoreUtils) {
//        society = keyStoreUtils.decrypt(App.appInstance,society);
//        username = keyStoreUtils.decrypt(App.appInstance,username);
//        password = keyStoreUtils.decrypt(App.appInstance,password);
        return this;
    }
}
