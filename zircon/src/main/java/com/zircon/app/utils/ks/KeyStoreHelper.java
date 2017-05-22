package com.zircon.app.utils.ks;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.security.KeyStore;

/**
 * Created by jikoobaruah on 20/05/17.
 */

interface KeyStoreHelper {

    String KEY_ALIAS = "zircon";

    String encrypt(Context context, KeyStore keyStore, String input);

    String decrypt(Context context, KeyStore keyStore, String input);

    void generateKey(Context context, KeyStore keyStore);
}
