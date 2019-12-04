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
import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

public class RentFragment extends Fragment {

    private EditText input;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rent_tool, container, false);
        this.input = root.findViewById(R.id.rent_tool_input);
        this.button = root.findViewById(R.id.rent_tool_button);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.button.setOnClickListener(l -> {
            String mInput = this.input.getText().toString();
            Employee employee = CacheSingleton.getInstance(getContext()).getAuthenticatedUser();
            if (mInput.trim().equals("")) {
                this.input.setError("Please fill in tool id");
            } else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("employee_id", employee.getId());
                    jsonObject.put("tool_id", mInput);
                    VolleySingleton.getInstance(getContext()).postRequest(Endpoints.URL + "/rentTool", jsonObject,
                            response -> {
                                Toast.makeText(getContext(), "Loan success", Toast.LENGTH_LONG).show();
                            }, error -> {
                                Toast.makeText(getContext(), new String(error.networkResponse.data), Toast.LENGTH_LONG).show();
                            }
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}