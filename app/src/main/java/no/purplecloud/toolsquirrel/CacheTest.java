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

import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

public class CacheTest extends Fragment {

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
            CacheSingleton.getInstance(getContext()).saveToCache("test1", text);
            mEditText.getText().clear();
            System.out.println("Writing to file...");
        });

        root.findViewById(R.id.cache_test_load).setOnClickListener(event -> {
            mEditText.setText(CacheSingleton.getInstance(getContext()).loadFromCache("test1"));
        });

        return root;
    }
}