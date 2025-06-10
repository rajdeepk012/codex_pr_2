package co.mw.gf_dashboard_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Record {
    @JsonProperty
    private String Id;
    @JsonProperty
    private String Mobile__c;
    @JsonProperty
    private String Name;
    @JsonProperty
    private String Pincode_Master__c;
    @JsonProperty
    private String Employee_Code__c;
    @JsonProperty
    private String Professional_Skills__c;

    public Record() {
    }

    public Record(String id, String mobile__c, String name, String pincode_Master__c, String employee_code__c,
                  String professional_skills__c) {
        Id = id;
        Mobile__c = mobile__c;
        Name = name;
        Pincode_Master__c = pincode_Master__c;
        Employee_Code__c = employee_code__c;
        Professional_Skills__c = professional_skills__c;
    }

    public String getEmployee_Code__c() {
        return Employee_Code__c;
    }

    public void setEmployee_Code__c(String employee_Code__c) {
        Employee_Code__c = employee_Code__c;
    }

    public String getProfessional_Skills__c() {
        return Professional_Skills__c;
    }

    public void setProfessional_Skills__c(String professional_Skills__c) {
        Professional_Skills__c = professional_Skills__c;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMobile__c() {
        return Mobile__c;
    }

    public void setMobile__c(String mobile__c) {
        Mobile__c = mobile__c;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPincode_Master__c() {
        return Pincode_Master__c;
    }

    public void setPincode_Master__c(String pincode_Master__c) {
        Pincode_Master__c = pincode_Master__c;
    }
}
