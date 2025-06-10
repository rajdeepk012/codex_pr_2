package co.mw.gf_dashboard_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class PinRecord {
    @JsonProperty
    private String Id;
    @JsonProperty
    private String Pincode__c;
    @JsonProperty
    private String City__c;
    @JsonProperty
    private String State__c;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPincode__c() {
        return Pincode__c;
    }

    public void setPincode__c(String pincode__c) {
        Pincode__c = pincode__c;
    }

    public String getCity__c() {
        return City__c;
    }

    public void setCity__c(String city__c) {
        City__c = city__c;
    }

    public String getState__c() {
        return State__c;
    }

    public void setState__c(String state__c) {
        State__c = state__c;
    }
}
