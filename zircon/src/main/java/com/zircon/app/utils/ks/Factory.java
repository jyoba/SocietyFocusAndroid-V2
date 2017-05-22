package com.zircon.app.utils.ks;

/**
 * Created by jikoobaruah on 20/05/17.
 */

public class Factory {
    public static KeyStoreHelper getHelper() {
        return new JBhelper();
    }
}
