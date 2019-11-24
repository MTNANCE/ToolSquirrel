package no.purplecloud.toolsquirrel.domain;

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

    private Employee loaner;

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
}
