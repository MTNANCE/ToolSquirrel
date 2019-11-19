package no.purplecloud.toolsquirrel.ui.project;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.network.VolleySingleton;

public class NewProjectFragment extends Fragment {

    private TextView projectName;
    private TextView projectDesc;
    private TextView projectLocation;
    private TextView projectStatus;
    private Button submitBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_project, container, false);
        this.projectName = root.findViewById(R.id.new_project_name);
        this.projectDesc = root.findViewById(R.id.new_project_desc);
        this.projectLocation = root.findViewById(R.id.new_project_location);
        this.projectStatus = root.findViewById(R.id.new_project_status);
        this.submitBtn = root.findViewById(R.id.new_project_button);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.submitBtn.setOnClickListener(event -> {
            String mProjectName = this.projectName.getText().toString();
            String mProjectDesc = this.projectDesc.getText().toString();
            String mProjectLocation = this.projectLocation.getText().toString();
            // Do simple validation
            if (mProjectName.trim().isEmpty()) {
                this.projectName.setError("Please fill in project name");
            }
            if (mProjectDesc.trim().isEmpty()) {
                this.projectDesc.setError("Please fill in project description");
            }
            if (mProjectLocation.trim().isEmpty()) {
                this.projectLocation.setError("Please fill in project location");
            }
            if (!mProjectName.trim().isEmpty() && !mProjectDesc.trim().isEmpty() &&
                    !mProjectLocation.trim().isEmpty()) {
                newProject(mProjectName, mProjectDesc, mProjectLocation);
            }
        });
    }

    public void newProject(String name, String desc, String location) {
        String url = "http://localhost:8080/addNewProject";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("desc", desc);
            jsonObject.put("location", location);
            String requestBody = jsonObject.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    this.projectStatus.setText("Project creation success!");
                    this.projectStatus.setTextColor(Color.parseColor("#1fa139"));
                }, error -> {
                    this.projectStatus.setText("Project creation failed.");
                    this.projectStatus.setTextColor(Color.parseColor("#e01919"));
                }
            ) {
                @Override
                public String getBodyContentType() {
                    return String.format("application/json; charset=utf-8");
                }
                @Override
                public byte[] getBody() {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                requestBody, "utf-8");
                        return null;
                    }
                }
            };
            VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
