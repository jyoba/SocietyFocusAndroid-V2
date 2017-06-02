package com.zircon.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zircon.app.App;
import com.zircon.app.model.LoginCredentials;
import com.zircon.app.model.Society;
import com.zircon.app.model.User;
import com.zircon.app.utils.ks.KeyStoreUtils;

import java.util.UUID;

/**
 * Created by jikoobaruah on 07/04/17.
 */

public class AccountUtils {
    private static final String ACCOUNT_PREF_FILE = "account";
    private static final String KEY_IS_AUTO_LOGIN = "auto_login";
    private static final String KEY_IS_SOCIETY_LOGIN = "society_login";
    private static final String KEY_USERNAME = "u";
    private static final String KEY_PASSWORD = "p";
    private static final String KEY_SOCIETY = "s";
    private static final String KEY_LOGGED_IN_USER = "user";
    private static final String KEY_LOGGED_IN_SOCIETY = "society";
    private static final String KEY_TOKEN = "token";

    public static void saveSocietyLogin(LoginCredentials loginCredentials) {
        SharedPreferences pref = App.appInstance.getSharedPreferences(ACCOUNT_PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_IS_AUTO_LOGIN, true);
        editor.putBoolean(KEY_IS_SOCIETY_LOGIN, true);
        editor.putString(KEY_USERNAME, loginCredentials.username);
        editor.putString(KEY_PASSWORD, loginCredentials.password);
        editor.putString(KEY_SOCIETY, loginCredentials.society);
        editor.apply();
    }

    public static void saveFbLogin() {
        SharedPreferences pref = App.appInstance.getSharedPreferences(ACCOUNT_PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_IS_AUTO_LOGIN, true);
        editor.putBoolean(KEY_IS_SOCIETY_LOGIN, false);
        editor.apply();
    }

    public static boolean isAutoLogin(){
        SharedPreferences pref = App.appInstance.getSharedPreferences(ACCOUNT_PREF_FILE, Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_IS_AUTO_LOGIN,false);
    }

    public static boolean isSocietyLogin(){
        SharedPreferences pref = App.appInstance.getSharedPreferences(ACCOUNT_PREF_FILE, Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_IS_SOCIETY_LOGIN,false);
    }

    public static LoginCredentials getLoginCredentials(){
        SharedPreferences pref = App.appInstance.getSharedPreferences(ACCOUNT_PREF_FILE, Context.MODE_PRIVATE);
        return new LoginCredentials(pref.getString(KEY_SOCIETY,null),pref.getString(KEY_USERNAME,null ),pref.getString(KEY_PASSWORD,null)).getDecrypted(KeyStoreUtils.getInstance(App.appInstance));
    }

    public static void saveLoggedInSociety(Society society) {

        SharedPreferences pref = App.appInstance.getSharedPreferences(ACCOUNT_PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_LOGGED_IN_SOCIETY, JsonUtils.getJson(society));
        editor.apply();

    }

    public static void saveLoggedInUser(User user) {
        SharedPreferences pref = App.appInstance.getSharedPreferences(ACCOUNT_PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_LOGGED_IN_USER, JsonUtils.getJson(user));
        editor.apply();

    }

    public static User getLoggedInUser() {
        SharedPreferences pref = App.appInstance.getSharedPreferences(ACCOUNT_PREF_FILE, Context.MODE_PRIVATE);
        return JsonUtils.getObject(pref.getString(KEY_LOGGED_IN_USER,null),User.class);
    }

    public static Society getLoggedInSociety() {
        SharedPreferences pref = App.appInstance.getSharedPreferences(ACCOUNT_PREF_FILE, Context.MODE_PRIVATE);
        return JsonUtils.getObject(pref.getString(KEY_LOGGED_IN_SOCIETY,null),Society.class);
    }

    public static void saveAuthToken(String token) {
        SharedPreferences pref = App.appInstance.getSharedPreferences(ACCOUNT_PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public static String getToken() {
        SharedPreferences pref = App.appInstance.getSharedPreferences(ACCOUNT_PREF_FILE, Context.MODE_PRIVATE);
        return pref.getString(KEY_TOKEN,null);
    }

    public static void logout() {
        SharedPreferences pref = App.appInstance.getSharedPreferences(ACCOUNT_PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}



