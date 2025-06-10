package co.mw.gf_dashboard_service.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CaseCounts {
    int assignedCaseCount;
    int draftCaseCount;
    int completedCaseCount;

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
}
