package no.purplecloud.toolsquirrel.singleton;

import android.content.Context;

import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import no.purplecloud.toolsquirrel.domain.Employee;

public class CacheSingleton {

    // Name of the file
    private final String fileName = "battery_fresh.txt";
    private final String cacheFileName = "cache_file.txt";

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

    /*------------------------------
    Cache storage
    ----------------------------*/

    public void saveToCache(String key, String textToSave) {
        FileOutputStream fileOutputStream = null;
        try {
            File cacheFile = new File(context.getCacheDir(), cacheFileName);
            if (!cacheFile.exists()) {
                cacheFile = new File(context.getCacheDir(), cacheFileName);
            }
            fileOutputStream = new FileOutputStream(cacheFile);
            // Get current data as JSON
            JSONObject data = getFileContentAsJSON(cacheFileName);
            if (data.has(key)) {
                data.remove(key);
            }
            data.put(key, textToSave);
            fileOutputStream.write(String.valueOf(data).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
            JSONObject jsonObject = getFileContentAsJSON(cacheFileName);
            if (jsonObject != null) {
                if (!jsonObject.has(key)) {
                    response = "";
                } else {
                    response = jsonObject.getString(key);
                }
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        return response;
    }

    /*------------------------------
    Data storage
    ----------------------------*/

    public void saveToData(String key, String textToSave) {
        FileOutputStream fileOutputStream = null;
        try {
            JSONObject jsonObject = getFileContentAsJSON(fileName);
            if (jsonObject != null) {
                if (jsonObject.has(key)) {
                    jsonObject.remove(key);
                }
            } else {
                jsonObject = new JSONObject();
            }
            jsonObject.put(key, textToSave);
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
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

    public String loadFromData(String key) {
        String response = "";
        try {
            JSONObject jsonObject = getFileContentAsJSON(fileName);
            if (jsonObject != null) {
                if (!jsonObject.has(key)) {
                    response = "";
                } else {
                    response = jsonObject.getString(key);
                }
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        return response;
    }

    /*------------------------------
    Common method
    ----------------------------*/

    public JSONObject getFileContentAsJSON(String file) {
        FileInputStream fileInputStream = null;
        JSONObject jsonObject = null;
        try {
            switch (file) {
                case fileName:
                    File foundFile = new File(context.getDataDir(), fileName);
                    if (!foundFile.toString().trim().equals("")) {
                        fileInputStream = context.openFileInput(fileName);
                    }
                    break;

                case cacheFileName:
                    File cacheFile = new File(context.getCacheDir(), cacheFileName);
                    if (cacheFile.exists()) {
                        fileInputStream = new FileInputStream(new File(context.getCacheDir(), cacheFileName));
                    }
                    break;
            }
            if (fileInputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    stringBuilder.append(text).append("\n");
                }
                if (stringBuilder.length() == 0) {
                    jsonObject = new JSONObject();
                } else {
                    jsonObject = new JSONObject(stringBuilder.toString());
                }
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

    /*------------------------------
    Token related methods
    ----------------------------*/

    public boolean tokenIsValid(Context context) {
        boolean isValid = false;
        String token = loadFromCache("token");

        if (token != null) {
            if (!tokenHasExpired(token, context)) {
                isValid = true;
                //client = new JWT(token).getClaim("employee").asObject(Employee.class);
            }
        }

        return isValid;
    }

    public boolean tokenHasExpired(String token, Context context) {
        System.out.println(token);
        JWT jwt = null;
        try {
            jwt = new JWT(token);
        } catch (DecodeException de) {
            return true;
        }
        return jwt.isExpired(0);
    }

    public Employee getAuthenticatedUser() {
        return new JWT(loadFromCache("token")).getClaim("employee").asObject(Employee.class);
    }

    public void removeToken() {
        FileOutputStream fileOutputStream = null;
        try {
            File cacheFile = new File(context.getCacheDir(), cacheFileName);
            if (!cacheFile.exists()) {
                cacheFile = new File(context.getCacheDir(), cacheFileName);
            }
            fileOutputStream = new FileOutputStream(cacheFile);
            // Get current data as JSON
            JSONObject data = getFileContentAsJSON(cacheFileName);
            if (data.has("token")) {
                data.remove("token");
            }
            fileOutputStream.write(String.valueOf(data).getBytes());
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

    // TODO If time, create a more secure way

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
