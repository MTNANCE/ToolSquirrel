package no.purplecloud.toolsquirrel.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private Long id;
    private String name;
    private String username;
    private String email;
    private int phone;

    // Image url
    private String image;

    // Security
    private List<String> rolesList;
    private List<String> permissionsList;

    // Project leader specific
    private String position;

    public Employee(JSONObject jsonObject) throws JSONException {
        setId(jsonObject.getLong("id"));
        setName(jsonObject.getString("name"));
        setUsername(jsonObject.getString("username"));
        setEmail(jsonObject.getString("email"));
        setPhone(jsonObject.getInt("phone"));
        // Check if there is an image
        if (jsonObject.has("image")) {
            this.image = jsonObject.getString("image");
        }
        JSONArray rolesArr = jsonObject.getJSONArray("rolesList");
        for (int i = 0; i < rolesArr.length(); i++) {
            this.rolesList.add(rolesArr.getString(i));
        }
        JSONArray permissionArr = jsonObject.getJSONArray("permissionsList");
        for (int i = 0; i < permissionArr.length(); i++) {
            this.permissionsList.add(permissionArr.getString(i));
        }
    }

}
