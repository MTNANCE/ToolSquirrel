package no.purplecloud.toolsquirrel.domain;

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

    private List<Employee> projectLeaders;

    public Project(Long projectId, String projectName, String projectDescription, String projectImage) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectImage = projectImage;
    }

    public void addProjectLeaderToProject(Employee employee) {
        if (projectLeaders == null) {
            this.projectLeaders = new ArrayList<>();
        }
        this.projectLeaders.add(employee);
    }
}
