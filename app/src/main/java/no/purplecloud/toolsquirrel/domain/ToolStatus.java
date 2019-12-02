package no.purplecloud.toolsquirrel.domain;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolStatus {
    private int id;
    private String name;
    private String location;
    private boolean isAvailable;

    public ToolStatus(JSONObject toolJson) throws JSONException {
        this.id = toolJson.getInt("id");
        this.name = toolJson.getString("name");
        this.location = toolJson.getString("location");
        this.isAvailable = toolJson.getBoolean("available");
    }
}
