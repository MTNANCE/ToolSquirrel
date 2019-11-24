package no.purplecloud.toolsquirrel.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Project {

    private Long projectId;

    private String projectName;

    private String projectDescription;

    private String projectImage;

    private String projectLocation;

    private List<Employee> projectLeaders;

    public Project(Long projectId, String projectName, String projectDescription, String projectImage, String projectLocation) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectImage = projectImage;
        this.projectLocation = projectLocation;
    }

    public Project(JSONObject jsonObject) throws JSONException {
        setProjectId(jsonObject.getLong("id"));
        setProjectName(jsonObject.getString("name"));
        setProjectDescription(jsonObject.getString("description"));
        setProjectLocation(jsonObject.getString("location"));

        // Check if there is an image for the tool
        if (jsonObject.has("image")) {
            this.projectImage = jsonObject.getString("image");
        }
    }

    public void addProjectLeaderToProject(Employee employee) {
        if (projectLeaders == null) {
            this.projectLeaders = new ArrayList<>();
        }
        this.projectLeaders.add(employee);
    }


}
