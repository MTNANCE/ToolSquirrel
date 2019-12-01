package no.purplecloud.toolsquirrel.ui.project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

public class NewProjectFragment extends Fragment {

    private TextView projectName;
    private TextView projectDesc;
    private TextView projectLocation;
    private TextView projectStatus;
    private Button submitBtn;
    private Button uploadImageBtn;
    private ImageView uploadedImg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_project, container, false);
        this.projectName = root.findViewById(R.id.new_project_name);
        this.projectDesc = root.findViewById(R.id.new_project_desc);
        this.projectLocation = root.findViewById(R.id.new_project_location);
        this.projectStatus = root.findViewById(R.id.new_project_status);
        this.submitBtn = root.findViewById(R.id.new_project_button);

        this.uploadImageBtn = root.findViewById(R.id.uploadImageBtn);
        this.uploadedImg = root.findViewById(R.id.uploadedImageView);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set "select image" event when pressing the upload image button
        uploadImageBtn.setOnClickListener(l -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                    3);
        });

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
        // Check if client has uploaded an image
        if (uploadedImg.getDrawable() instanceof AdaptiveIconDrawable) {
            this.projectStatus.setText("Please select an image.");
            this.projectStatus.setTextColor(Color.parseColor("#e01919"));
            return;
        }

        // Get iamge as bitmap
        Bitmap bitmap = ((BitmapDrawable) uploadedImg.getDrawable()).getBitmap();

        // Create an output stream and feed it the content of the image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        // Get the content as bytes (will be used to send to API)
        byte[] imgBytes = baos.toByteArray();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("desc", desc);
            jsonObject.put("location", location);
            jsonObject.put("image", imgBytes);
            Employee authenticatedEmployee = CacheSingleton.getInstance(getContext()).getAuthenticatedUser();
            jsonObject.put("employee_id", authenticatedEmployee.getId());

            VolleySingleton.getInstance(getContext()).postRequest(Endpoints.URL + "/addNewProject", jsonObject,
                    response -> {
                        this.projectStatus.setText("Project creation success!");
                        this.projectStatus.setTextColor(Color.parseColor("#1fa139"));
                    }, error -> {
                        this.projectStatus.setText("Project creation failed.");
                        this.projectStatus.setTextColor(Color.parseColor("#e01919"));
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // When the client has chosen a picture, update the ImageView
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                uploadedImg.setImageURI(data.getData());
            }
        }
    }
}
