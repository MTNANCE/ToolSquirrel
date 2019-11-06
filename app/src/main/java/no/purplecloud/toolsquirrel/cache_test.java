package no.purplecloud.toolsquirrel;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class cache_test extends Fragment {

    private static final String FILE_NAME = "example.txt";

    EditText mEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cache_test, container, false);
        // TODO Add other ref here

        this.mEditText = root.findViewById(R.id.cache_test_text);
        root.findViewById(R.id.cache_test_save).setOnClickListener(event -> {
            String text = mEditText.getText().toString();
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                fileOutputStream.write(text.getBytes());

                mEditText.getText().clear();
                Toast.makeText(getContext(), "Saved to " + getContext().getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
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
        });

        root.findViewById(R.id.cache_test_load).setOnClickListener(event -> {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = getContext().openFileInput(FILE_NAME);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    stringBuilder.append(text).append("\n");
                }

                mEditText.setText(stringBuilder.toString());
            } catch (FileNotFoundException e) {
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
        });

        return root;
    }
}
