package co.mw.gf_dashboard_service.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailsResponse {

    private int totalAssignedCases;
    private int totalDraftCases;
    private int totalCompletedCases;
    private int recordCount;
    private List<UserDetails> userDetails;

    public UserDetailsResponse(int totalAssignedCases, int totalDraftCases, int totalCompletedCases, int recordCount, List<UserDetails> userDetails) {
        this.totalAssignedCases = totalAssignedCases;
        this.totalDraftCases = totalDraftCases;
        this.totalCompletedCases = totalCompletedCases;
        this.recordCount = recordCount;
        this.userDetails = userDetails;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getTotalAssignedCases() {
        return totalAssignedCases;
    }

    public void setTotalAssignedCases(int totalAssignedCases) {
        this.totalAssignedCases = totalAssignedCases;
    }

    public int getTotalDraftCases() {
        return totalDraftCases;
    }

    public void setTotalDraftCases(int totalDraftCases) {
        this.totalDraftCases = totalDraftCases;
    }

    public int getTotalCompletedCases() {
        return totalCompletedCases;
    }

    public void setTotalCompletedCases(int totalCompletedCases) {
        this.totalCompletedCases = totalCompletedCases;
    }

    public List<UserDetails> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<UserDetails> userDetails) {
        this.userDetails = userDetails;
    }
}
