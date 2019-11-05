package no.purplecloud.toolsquirrel.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tool {

    private Long id;

    private String toolName;

    // TODO Change after changing tool on backend
    private String toolDesc;

    private String toolLocation;

    private String toolImage;

    private Employee loaner;

    /**
     * Constructor for creating a dummy tool
     */
    public Tool(Long id, String imageUrl, String toolName, String toolDesc, String toolLocation) {
        this.id = id;
        this.toolImage = imageUrl;
        this.toolName = toolName;
        this.toolDesc = toolDesc;
        this.toolLocation = toolLocation;
    }

    public Tool(JSONObject jsonObject) throws JSONException {
        setId(jsonObject.getLong("id"));
        setToolName(jsonObject.getString("name"));
        setToolDesc(jsonObject.getString("description"));
        setToolLocation(jsonObject.getString("location"));

        // Check if there is an image for the tool
        if (jsonObject.has("image")) {
            this.toolImage = jsonObject.getString("image");
        }

        // Check if the tool is loaned by a user or not
        // TODO double check the string. if the APjsonObject.getString(I returns user, loaner or whatever
        if (jsonObject.has("loaner")) {
            // TODO Add necessary parameters after changing Employee class
            this.loaner = new Employee();
        }
    }

}
