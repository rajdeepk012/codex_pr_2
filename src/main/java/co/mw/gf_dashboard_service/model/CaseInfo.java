package co.mw.gf_dashboard_service.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CaseInfo {

    @JsonProperty("assigned_case_count")
    private int assignedCaseCount;
    @JsonProperty("draft_case_count")
    private int draftCaseCount;
    @JsonProperty("completed_case_count")
    private int completedCaseCount;
    private List<Case> cases;

    public int getAssignedCaseCount() {
        return assignedCaseCount;
    }

    public void setAssignedCaseCount(int assignedCaseCount) {
        this.assignedCaseCount = assignedCaseCount;
    }

    public int getDraftCaseCount() {
        return draftCaseCount;
    }

    public void setDraftCaseCount(int draftCaseCount) {
        this.draftCaseCount = draftCaseCount;
    }

    public int getCompletedCaseCount() {
        return completedCaseCount;
    }

    public void setCompletedCaseCount(int completedCaseCount) {
        this.completedCaseCount = completedCaseCount;
    }

    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
    }
}
