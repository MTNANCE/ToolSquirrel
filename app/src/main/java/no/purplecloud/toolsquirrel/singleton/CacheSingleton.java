package no.purplecloud.toolsquirrel.singleton;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;

import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.MasterKeys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;

public class CacheSingleton {

    // Name of the file
    private static final String FILE_NAME = "battery_fresh.txt";

    // Singleton Instance
    private static CacheSingleton instance;
    private final Context context;

    public CacheSingleton(Context context) {
        this.context = context;
    }

    public static synchronized CacheSingleton getInstance(Context context) {
        if (instance == null) {
            context = context.getApplicationContext();
            instance = new CacheSingleton(context);
        }
        return instance;
    }

    public void saveToCache(String textToSave) {
        FileOutputStream fileOutputStream = null;
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
            EncryptedFile encryptedFile = new EncryptedFile.Builder(
                    new File(context.getCacheDir(), FILE_NAME),
                    context,
                    masterKeyAlias,
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build();

            fileOutputStream = encryptedFile.openFileOutput();
            fileOutputStream.write(textToSave.getBytes());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String LoadFromCache() {
        FileInputStream fileInputStream = null;
        StringBuffer stringBuffer = new StringBuffer();
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
            EncryptedFile encryptedFile = new EncryptedFile.Builder(
                    new File(context.getCacheDir(), FILE_NAME),
                    context,
                    masterKeyAlias,
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build();

            fileInputStream = encryptedFile.openFileInput();
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuffer.append(line).append('\n');
                line = bufferedReader.readLine();
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuffer.toString();
    }

}
