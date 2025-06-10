package co.mw.gf_dashboard_service.los.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
public class LosCaseInitiationInputModel {
private LossServices LOSServices;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    @ToString
    public static class LossServices {
        private List<CaseDetail> listCaseDetail;

    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    @ToString
    public static class CaseDetail {
        private String ExternalId;
        private String AdditionalInfo;
        private String Source;
        private String AccountId;
        private String ClientBranchId;
        private String ReferenceNumber;
        private String LoanType;
        private long LoanAmount;
        private String TechnicalReportType;
        private String CreditReportType;
        private String LegalReportType;
        private List<Collateral> listCollateral;
        private List<DocumentChecklist> DocumentChecklist;
        private List<ApplicantDetails> listApplicantDetails;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    @ToString
    public static class Collateral {
        private String Collateraltype;
        private String Addressline1;
        private String Popertytype;
        private String City;
        private String State;
        private String Pincode;
        private List<PropertyOwner> listPropertyOwner;
        private List<Technical> listTechnical;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    @ToString
    public static class PropertyOwner {
        private String Name;
        private String OwnershipType;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    @ToString
    public static class Technical {
        private String ReportType;
        private String CustomerContactNumber;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    @ToString
    public static class DocumentChecklist {
        private String DocumentName;
        private String DocumentFormat;
        private String VersionData;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    @ToString
    public static class ApplicantDetails {
        private String Name;
        private String CustomerType;
        private String ApplicantType;
        private String Pincode;
        private String City;
        private String State;
        private String Address;
        private String PAN;
        private String CIN;
        private String AadhaarCard;
        private String Dateofbirth;
        private String DateofIncorporation;
        private String ElectricityProvider;
        private String ElectricityConsumerNumber;
        private String MaritalStatus;
        private String Gender;
        private String FatherSpouseName;
        private String AlternateMobileNumber;
        private String MobileNumber;
    }
//    @JsonCreator
//    public static LosCaseInitiationInputModel fromString(String value) {
//        // Parse the string and create an instance of LosCaseInitiationInputModel
//        ObjectMapper mapper = new ObjectMapper();
//        try {
////            return mapper.readValue(value, LosCaseInitiationInputModel.class);
//            LosCaseInitiationInputModel model = mapper.readValue(value, LosCaseInitiationInputModel.class);
//            System.out.println("Parsed model: " + model);
//            return model;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}

