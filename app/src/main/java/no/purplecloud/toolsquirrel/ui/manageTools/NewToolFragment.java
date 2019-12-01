package no.purplecloud.toolsquirrel.ui.manageTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Project;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

public class NewToolFragment extends Fragment {

    private AutoCompleteTextView autoCompleteProject;
    private EditText toolName;
    private EditText toolDesc;
    private EditText toolLocation;
    // Image related
    private ImageView imagePreview;
    private Button uploadImageBtn;
    // Submit btn
    private Button submitBtn;

    // Change this to be of type Project
    private String selectedProject;

    private List<String> projectList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_tool, container, false);
        this.autoCompleteProject = rootView.findViewById(R.id.add_tool_project_selector);
        this.toolName = rootView.findViewById(R.id.new_tool_name);
        this.toolDesc = rootView.findViewById(R.id.new_tool_desc);
        this.toolLocation = rootView.findViewById(R.id.new_tool_location);
        this.imagePreview = rootView.findViewById(R.id.new_tool_image_preview);
        this.uploadImageBtn = rootView.findViewById(R.id.new_tool_image_upload_btn);
        this.submitBtn = rootView.findViewById(R.id.add_tool_button);

        // TODO This is some really dumb shit, change this shit to be some callback shit instead
        // Setup Project AutoComplete
        Long employee_id = CacheSingleton.getInstance(getContext()).getAuthenticatedUser().getId();
        VolleySingleton.getInstance(getContext())
                .getListRequest(Endpoints.URL + "/findAllProjectsUserIsLeaderFor/" + employee_id, "project", bar -> {
                    for (Object object : bar) {
                        if (object instanceof Project) {
                            this.projectList.add(((Project) object).getProjectName() + " #" + ((Project) object).getProjectId());
                        }
                    }
                    ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, projectList);
                    // Define the threshold point where it is going to start searching
                    this.autoCompleteProject.setThreshold(1);
                    // Attach adapter
                    this.autoCompleteProject.setAdapter(projectAdapter);
                });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Project auto selector
        this.autoCompleteProject.setOnItemClickListener(((adapterView, view, i, l) -> {
            this.selectedProject = adapterView.getItemAtPosition(i).toString();
            System.out.println("Selected Project: " + this.selectedProject);
        }));
        // Set "select image" event when pressing the upload image button
        this.uploadImageBtn.setOnClickListener(l -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI), 3);
        });
        // Submit button on click listener
        this.submitBtn.setOnClickListener(l -> {
            String mToolName = this.toolName.getText().toString();
            String mToolDesc = this.toolDesc.getText().toString();
            String mToolLocation = this.toolLocation.getText().toString();

            // Do simple validation
            if (mToolName.trim().isEmpty()) {
                this.toolName.setError("Please fill in tool name");
            }
            if (mToolDesc.trim().isEmpty()) {
                this.toolDesc.setError("Please fill in tool description");
            }
            if (mToolLocation.trim().isEmpty()) {
                this.toolLocation.setError("Please fill in tool location");
            }
            if (!mToolName.trim().isEmpty() && !mToolDesc.trim().isEmpty() &&
                    !mToolLocation.trim().isEmpty()) {
                newTool(mToolName, mToolDesc, mToolLocation);
            }
        });
    }

    public void newTool(String name, String desc, String location) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When the client has chosen a picture, update the ImageView
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                imagePreview.setImageURI(data.getData());
            }
        }
    }
}
