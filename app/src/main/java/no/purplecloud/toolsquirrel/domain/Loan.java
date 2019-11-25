package no.purplecloud.toolsquirrel.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    private Long toolId;

    private String toolName;

    private String dueDate;

    private Long employee_id;

    /** TODO Remove this when done testing
     * Constructor for creating a dummy/test loan
     * @param toolId
     * @param toolName
     */
    public Loan(Long toolId, String toolName, String dueDate) {
        this.toolId = toolId;
        this.toolName = toolName;
        this.dueDate = dueDate;
    }

    public Loan(JSONObject jsonObject) {
        System.out.println("JSONObject: " + jsonObject);
        try {
            this.toolId = jsonObject.getLong("toolId");
            this.toolName = jsonObject.getString("toolName");
            this.dueDate = jsonObject.getString("dueDate").split(" ")[0];
            this.employee_id = jsonObject.getLong("employee_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
