package com.zircon.app.utils.ks;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Created by jikoobaruah on 20/05/17.
 */

public class KeyStoreUtils {


    private static KeyStoreUtils instance;
    private KeyStore keyStore;
    private boolean isKeyStoreInit = false;
    private boolean isKeyGenerated = true;
    private KeyStoreHelper helper;

    private KeyStoreUtils(Context context) {

        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
//            char[] passwordChar = InstanceID.getInstance(context).getId().toCharArray();
//            FileInputStream fis = context.openFileInput("keyStoreName");
            keyStore.load(null);//, passwordChar);
            isKeyStoreInit = true;
            helper = Factory.getHelper();
            helper.generateKey(context, keyStore);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final KeyStoreUtils getInstance(Context context) {
        if (instance == null)
            instance = new KeyStoreUtils(context);
        return instance;
    }

    public String encrypt(Context context, String input) {
        return helper.encrypt(context, keyStore, input);
    }

    public String decrypt(Context context, String input) {
        return helper.decrypt(context, keyStore, input);
    }
}
