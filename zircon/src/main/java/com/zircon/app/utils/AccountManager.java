package com.zircon.app.utils;

import com.zircon.app.model.Society;
import com.zircon.app.model.User;

/**
 * Created by jikoobaruah on 10/04/17.
 */

public class AccountManager {

    private static AccountManager instance;
    private User user;
    private Society society;
    private String token;
    private AccountManager() {
    }

    public static AccountManager getInstance() {
        if (instance == null)
            instance = new AccountManager();
        return instance;
    }

    public User getloggedInUser() {
        if (user == null) {
            user = AccountUtils.getLoggedInUser();
        }
        return user;
    }

    public Society getloggedInSociety() {
        if (society == null) {
            society = AccountUtils.getLoggedInSociety();
        }
        return society;
    }

    public String getToken() {
        if (token == null) {
            token = AccountUtils.getToken();
        }
        return token;
    }

    public void logout() {
        user = null;
        society = null;
        token = null;
        AccountUtils.logout();
    }
}
