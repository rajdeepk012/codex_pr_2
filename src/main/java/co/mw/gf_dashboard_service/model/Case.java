package co.mw.gf_dashboard_service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;


@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class Case {

    @JsonProperty("case_no")
    private String caseNo;
    @JsonProperty("case_name")
    private String caseName;
    @JsonProperty("case_type")
    private String caseType;
    private String status;
    @JsonProperty("case_address")
    private String caseAddress;
    @JsonProperty("case_assigned_at")
    private String caseAssignedAt;
    @JsonProperty("case_completion_at")
    private String caseCompletionAt;

    // Getters and Setters

    public String getCaseAddress() {
        return caseAddress;
    }

    public void setCaseAddress(String caseAddress) {
        this.caseAddress = caseAddress;
    }

    public String getCaseAssignedAt() {
        return caseAssignedAt;
    }

    public void setCaseAssignedAt(String caseAssignedAt) {
        this.caseAssignedAt = caseAssignedAt;
    }

    public String getCaseCompletionAt() {
        return caseCompletionAt;
    }

    public void setCaseCompletionAt(String caseCompletionAt) {
        this.caseCompletionAt = caseCompletionAt;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

