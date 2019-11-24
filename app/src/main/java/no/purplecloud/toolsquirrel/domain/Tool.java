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

    private String toolDesc;

    private String toolLocation;

    private String toolImage;

    public Tool(JSONObject jsonObject) throws JSONException {
        setId(jsonObject.getLong("id"));
        setToolName(jsonObject.getString("name"));
        setToolDesc(jsonObject.getString("description"));
        setToolLocation(jsonObject.getString("location"));

        // Check if there is an image for the tool
        if (jsonObject.has("image")) {
            this.toolImage = jsonObject.getString("image");
        }
    }

}
