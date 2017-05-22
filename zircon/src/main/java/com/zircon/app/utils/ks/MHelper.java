package com.zircon.app.utils.ks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jikoobaruah on 20/05/17.
 */

class MHelper implements KeyStoreHelper {


    private static final String SHARED_PREFENCE_NAME = "jb_ks";
    private static final String ENCRYPTED_KEY = "ek";

    private static final String AES_MODE = "AES/GCM/NoPadding";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public String encrypt(Context context, KeyStore keyStore, String input) {

        try {
            Cipher c = Cipher.getInstance(AES_MODE);
            c.init(Cipher.ENCRYPT_MODE, getSecretKey(context,keyStore));//, new GCMParameterSpec(128, FIXED_IV.getBytes()));
            byte[] encodedBytes = c.doFinal(input.getBytes());
            String encryptedBase64Encoded = Base64.encodeToString(encodedBytes, Base64.DEFAULT);
            return encryptedBase64Encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public String decrypt(Context context, KeyStore keyStore, String input) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void generateKey(Context context, KeyStore keyStore) {
        try {
            if (!keyStore.containsAlias(KEY_ALIAS)) {

                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KeyStore.getDefaultType());
                keyGenerator.init(
                        new KeyGenParameterSpec.Builder(KEY_ALIAS,
                                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                                .setRandomizedEncryptionRequired(false)
                                .build());
                keyGenerator.generateKey();

            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

    }


    private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";

    private byte[] rsaEncrypt(KeyStore keyStore, byte[] secret) throws Exception {
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
        // Encrypt the text
        Cipher inputCipher = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL");
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inputCipher);
        cipherOutputStream.write(secret);
        cipherOutputStream.close();

        byte[] vals = outputStream.toByteArray();
        return vals;
    }

    private byte[] rsaDecrypt(KeyStore keyStore, byte[] encrypted) throws Exception {
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
        Cipher output = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL");
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
        CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(encrypted), output);
        ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte) nextByte);
        }

        byte[] bytes = new byte[values.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i).byteValue();
        }
        return bytes;
    }



    private Key getSecretKey(Context context, KeyStore keyStore) throws Exception {

        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFENCE_NAME, Context.MODE_PRIVATE);
        String enryptedKeyB64 = pref.getString(ENCRYPTED_KEY, null);
        if (enryptedKeyB64 == null) {
            byte[] key = new byte[16];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(key);
            byte[] encryptedKey = rsaEncrypt(keyStore, key);
            enryptedKeyB64 = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(ENCRYPTED_KEY, enryptedKeyB64);
            edit.commit();
        }


        byte[] encryptedKey = Base64.decode(enryptedKeyB64, Base64.DEFAULT);
        byte[] key = rsaDecrypt(keyStore, encryptedKey);
        return new SecretKeySpec(key, "AES");
    }
}
