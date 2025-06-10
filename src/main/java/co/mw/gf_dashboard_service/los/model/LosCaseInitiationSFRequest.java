package co.mw.gf_dashboard_service.los.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
public class LosCaseInitiationSFRequest {
    @JsonProperty("LOSServices")
    private LossServices LOSServices;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    @ToString
    public static class LossServices {
        @JsonProperty("listCaseDetail")
        private List<CaseDetail> listCaseDetail;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    @ToString
    public static class CaseDetail {
        @JsonProperty("ExternalId")
        private String externalId;
        @JsonProperty("AdditionalInfo")
        private String additionalInfo;
        @JsonProperty("Source")
        private String Source;
        @JsonProperty("AccountId")
        private String accountId;
        @JsonProperty("ClientBranchId")
        private String clientBranchId;
        @JsonProperty("ReferenceNumber")
        private String referenceNumber;
        @JsonProperty("LoanType")
        private String loanType;
        @JsonProperty("LoanAmount")
        private long loanAmount;
        @JsonProperty("TechnicalReportType")
        private String technicalReportType;
        @JsonProperty("CreditReportType")
        private String creditReportType;
        @JsonProperty("LegalReportType")
        private String legalReportType;
        @JsonProperty("listCollateral")
        private List<Collateral> listCollateral;
        @JsonProperty("DocumentChecklist")
        private List<DocumentChecklist> documentChecklist;
        @JsonProperty("listApplicantDetails")
        private List<ApplicantDetails> listApplicantDetails;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    @ToString
    public static class Collateral {
        @JsonProperty("Collateraltype")
        private String collateraltype;
        @JsonProperty("Addressline1")
        private String addressline1;
        @JsonProperty("Popertytype")
        private String popertytype;
        @JsonProperty("City")
        private String city;
        @JsonProperty("State")
        private String state;
        @JsonProperty("Pincode")
        private String pincode;
        @JsonProperty("listPropertyOwner")
        private List<PropertyOwner> listPropertyOwner;
        @JsonProperty("listTechnical")
        private List<Technical> listTechnical;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    @ToString
    public static class PropertyOwner {
        @JsonProperty("Name")
        private String name;
        @JsonProperty("OwnershipType")
        private String ownershipType;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    @ToString
    public static class Technical {
        @JsonProperty("ReportType")
        private String reportType;
        @JsonProperty("CustomerContactNumber")
        private String customerContactNumber;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    @ToString
    public static class DocumentChecklist {
        @JsonProperty("DocumentName")
        private String documentName;
        @JsonProperty("DocumentFormat")
        private String documentFormat;
        @JsonProperty("VersionData")
        private String versionData;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    @ToString
    public static class ApplicantDetails {
        @JsonProperty("Name")
        private String name;
        @JsonProperty("CustomerType")
        private String customerType;
        @JsonProperty("ApplicantType")
        private String applicantType;
        @JsonProperty("Pincode")
        private String pincode;
        @JsonProperty("City")
        private String city;
        @JsonProperty("State")
        private String state;
        @JsonProperty("Address")
        private String address;
        @JsonProperty("PAN")
        private String pan;
        @JsonProperty("CIN")
        private String cin;
        @JsonProperty("AadhaarCard")
        private String aadhaarCard;
        @JsonProperty("Dateofbirth")
        private String dateofbirth;
        @JsonProperty("DateofIncorporation")
        private String dateofIncorporation;
        @JsonProperty("ElectricityProvider")
        private String electricityProvider;
        @JsonProperty("ElectricityConsumerNumber")
        private String electricityConsumerNumber;
        @JsonProperty("MaritalStatus")
        private String maritalStatus;
        @JsonProperty("Gender")
        private String gender;
        @JsonProperty("FatherSpouseName")
        private String fatherSpouseName;
        @JsonProperty("AlternateMobileNumber")
        private String alternateMobileNumber;
        @JsonProperty("MobileNumber")
        private String mobileNumber;
    }
    }



