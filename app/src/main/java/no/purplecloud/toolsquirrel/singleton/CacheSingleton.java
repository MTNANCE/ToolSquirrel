package no.purplecloud.toolsquirrel.singleton;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;

import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.MasterKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

    public void saveToCache(String key, String textToSave) {
        FileOutputStream fileOutputStream = null;
        try {
            JSONObject jsonObject = getFileContentAsJSON();
            if (jsonObject != null) {
                if (jsonObject.has(key)) {
                    jsonObject.remove(key);
                }
            } else {
                jsonObject = new JSONObject();
            }
            jsonObject.put(key, textToSave);
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(String.valueOf(jsonObject).getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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

    public String loadFromCache(String key) {
        String response = "";
        try {
            JSONObject jsonObject = getFileContentAsJSON();
            if (jsonObject != null) {
                if (!jsonObject.has(key)) {
                    response = "Couldn't find anything with that key";
                } else {
                    response = jsonObject.getString(key);
                }
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject getFileContentAsJSON() {
        FileInputStream fileInputStream = null;
        JSONObject jsonObject = null;
        try {
            fileInputStream = context.openFileInput(FILE_NAME);
            if (fileInputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    stringBuilder.append(text).append("\n");
                }
                jsonObject = new JSONObject(stringBuilder.toString());
            }
        } catch (IOException | JSONException e) {
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
        return jsonObject;
    }

    // Secure way
    /*public void saveToCache(String textToSave) {
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
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
            outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.append(textToSave);
            outputStreamWriter.close();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    outputStreamWriter.close();
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
            fileInputStream.close();
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
    }*/

}
