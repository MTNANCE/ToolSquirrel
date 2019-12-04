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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.network.RetroFitApi;
import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NewProjectFragment extends Fragment {

    private TextView projectName;
    private TextView projectDesc;
    private TextView projectLocation;
    private TextView projectStatus;
    private Button submitBtn;
    private Button uploadImageBtn;
    private ImageView uploadedImg;

    private String imgName;
    private String imgType;

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

        try {
            // Get image as bitmap
            Bitmap bitmap = ((BitmapDrawable) uploadedImg.getDrawable()).getBitmap();
            File imgFile = File.createTempFile(imgName, ".".concat(imgType), getContext().getCacheDir());

            // Create an output stream and feed it the content of the image
            OutputStream out = new BufferedOutputStream(new FileOutputStream(imgFile));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();

            // Get the logged in employee
            Employee authenticatedEmployee = CacheSingleton.getInstance(getContext()).getAuthenticatedUser();

            // Create an API client from Retrofit
            // TODO move this class to a singleton
            Retrofit rf = new Retrofit.Builder().baseUrl(Endpoints.URL).addConverterFactory(ScalarsConverterFactory.create()).build();
            RetroFitApi api = rf.create(RetroFitApi.class);

            // Create the body for the request
            RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody descPart = RequestBody.create(MediaType.parse("text/plain"), desc);
            RequestBody locPart = RequestBody.create(MediaType.parse("text/plain"), location);
            RequestBody employeeIdPart = RequestBody.create(MediaType.parse("text/pl ain"), authenticatedEmployee.getId().toString());

            // Get image
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), imgFile);
            MultipartBody.Part imgBody = MultipartBody.Part.createFormData("image", imgFile.getName(), requestFile);

            // Define
            Call<String> call = api.createPost(namePart, descPart, locPart, employeeIdPart, imgBody);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Project added!", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    });
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // When the client has chosen a picture, update the imageview
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                uploadedImg.setImageURI(data.getData());
                imgName = data.getData().getPathSegments().get(6);
                imgType = data.getData().getPathSegments().get(5).split("/")[1];
            }
        }
    }
}
