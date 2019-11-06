package no.purplecloud.toolsquirrel.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    private Long toolId;

    private String toolName;

    private Employee loaner;

    /** TODO Remove this when done testing
     * Constructor for creating a dummy/test loan
     * @param toolId
     * @param toolName
     */
    public Loan(Long toolId, String toolName) {
        this.toolId = toolId;
        this.toolName = toolName;
    }
}
