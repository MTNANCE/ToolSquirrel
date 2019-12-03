package no.purplecloud.toolsquirrel.ui.borrowing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.network.VolleySingleton;

public class ReturnFragment extends Fragment {

    private EditText input;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_return_tool, container, false);
        this.input = root.findViewById(R.id.return_tool_input);
        this.button = root.findViewById(R.id.return_tool_button);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.button.setOnClickListener(l -> {
            String mInput = this.input.getText().toString();
            if (mInput.isEmpty()) {
                this.input.setError("");
            } else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("tool_id", mInput);
                    VolleySingleton.getInstance(getContext()).postRequest(Endpoints.URL + "/returnTool", jsonObject,
                            response -> {
                                Toast.makeText(getContext(), "Return success", Toast.LENGTH_LONG).show();
                            }, error -> {
                                Toast.makeText(getContext(), "Something went wrong..", Toast.LENGTH_LONG).show();
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
