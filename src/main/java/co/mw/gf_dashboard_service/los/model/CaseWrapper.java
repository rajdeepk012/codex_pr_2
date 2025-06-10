package co.mw.gf_dashboard_service.los.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CaseWrapper {

    private Case Case;

    @JsonProperty("Case")
    public Case getCase() {
        return Case;
    }

    public void setCase(Case aCase) {
        Case = aCase;
    }

    public static class Case {

        @JsonProperty("AccountId")
        private String accountId;

        @JsonProperty("ContactId")
        private String contactId;

        @JsonProperty("Client_Branch__c")
        private String clientBranch;

        @JsonProperty("Loan_Amount__c")
        private Long loanAmount;

        @JsonProperty("Loan_type__c")
        private String loanType;

        @JsonProperty("Client_Id__c")
        private String clientId;

        @JsonProperty("Reference_Number__c")
        private String referenceNumber;

        @JsonProperty("Parent_Product__c")
        private String parentProduct;

        @JsonProperty("Services__c")
        private String services;

        @JsonProperty("Collateral__c")
        private List<Collateral> collateralList;

        @JsonProperty("Applicant_Details__c")
        private List<ApplicantDetails> applicantDetailsList;

        @JsonProperty("Legal__c")
        private List<Legal> legalList;

        @JsonProperty("Technical__c")
        private List<Technical> technicalList;

        @JsonProperty("Credit_Verification__c")
        private List<CreditVerification> creditVerificationList;

        // Getters and setters for all fields
        // ...
    }

    public static class Collateral {

        @JsonProperty("Collateral_ID__c")
        private String collateralId;

        @JsonProperty("Collateral_type__c")
        private String collateralType;

        @JsonProperty("Pincode_Master__c")
        private String pincodeMaster;

        @JsonProperty("City__c")
        private String city;

        @JsonProperty("State__c")
        private String state;

        @JsonProperty("Address_line_1__c")
        private String addressLine1;

        @JsonProperty("Property_Owner__c")
        private List<PropertyOwner> propertyOwnerList;

        // Getters and setters for all fields
        // ...
    }

    public static class PropertyOwner {

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Collateral__c")
        private String collateral;

        @JsonProperty("Applicant_Details__c")
        private String applicantDetails;

        @JsonProperty("Ownership_Type__c")
        private String ownershipType;

        // Getters and setters for all fields
        // ...
    }

    public static class ApplicantDetails {

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Customer_type__c")
        private String customerType;

        @JsonProperty("Applicant_type__c")
        private String applicantType;

        @JsonProperty("Pincode__c")
        private String pincode;

        @JsonProperty("City__c")
        private String city;

        @JsonProperty("State__c")
        private String state;

        @JsonProperty("Address")
        private String address;

        @JsonProperty("PAN__c")
        private String pan;

        @JsonProperty("CIN__c")
        private String cin;

        @JsonProperty("Aadhaar_Card__c")
        private String aadhaarCard;

        @JsonProperty("Date_of_birth__c")
        private String dateOfBirth;

        @JsonProperty("Electricity_Provider__c")
        private String electricityProvider;

        @JsonProperty("Electricity_Consumer_Number__c")
        private String electricityConsumerNumber;

        @JsonProperty("Marital_Status__c")
        private String maritalStatus;

        @JsonProperty("Gender__c")
        private String gender;

        @JsonProperty("Father_Spouse_Name__c")
        private String fatherSpouseName;

        // Getters and setters for all fields
        // ...
    }

    public static class Legal {

        @JsonProperty("Name_of_Source_Bank_Issuing_Bank__c")
        private String issuingBank;

        @JsonProperty("Branch_Address_of_Source_Bank__c")
        private String branchAddressSourceBank;

        @JsonProperty("Name_of_BT_Bank_Depositing_Bank__c")
        private String depositingBank;

        @JsonProperty("Branch_Address_of_BT_Bank__c")
        private String branchAddressDepositingBank;

        @JsonProperty("Legal_Report_Type__c")
        private String legalReportType;

        @JsonProperty("Document_Checklist__c")
        private List<Document> documentChecklist;

        // Getters and setters for all fields
        // ...
    }

    public static class Technical {

        @JsonProperty("Customer_Name__c")
        private String customerName;

        @JsonProperty("collateralId")
        private String collateralId;

        @JsonProperty("Customer_Contact_Number__c")
        private String customerContactNumber;

        @JsonProperty("Technical_Report_Type__c")
        private String technicalReportType;

        @JsonProperty("Status__c")
        private String status;

        @JsonProperty("Document_Checklist__c")
        private List<Document> documentChecklist;

        // Getters and setters for all fields
        // ...
    }

    public static class CreditVerification {

        @JsonProperty("Address_Line_1__c")
        private String addressLine1;

        @JsonProperty("Address_Line_2__c")
        private String addressLine2;

        @JsonProperty("Credit_Report_Type__c")
        private String creditReportType;

        @JsonProperty("Pincode_Master__c")
        private String pincodeMaster;

        @JsonProperty("City__c")
        private String city;

        @JsonProperty("State__c")
        private String state;

        @JsonProperty("Document_Checklist__c")
        private List<Document> documentChecklist;

        @JsonProperty("Customer_Name__c")
        private String customerName;

        @JsonProperty("Customer_Contact_Number__c")
        private String customerContactNumber;

        @JsonProperty("Status__c")
        private String status;

        // Getters and setters for all fields
        // ...
    }

    public static class Document {

        @JsonProperty("Document Name")
        private String documentName;

        @JsonProperty("VersionData")
        private String versionData;

        // Getters and setters for all fields
        // ...
    }
}
