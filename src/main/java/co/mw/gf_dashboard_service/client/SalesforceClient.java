package co.mw.gf_dashboard_service.client;

import co.mw.gf_dashboard_service.api.TokenService;
import co.mw.gf_dashboard_service.common.LoggerUtil;
import co.mw.gf_dashboard_service.los.model.LosCaseInitiationSFRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SalesforceClient {
    private static final Logger logger = LoggerUtil.getLogger(SalesforceClient.class);


    @Value("${salesforce.baseurl}")
    private String sfBaseUrl;
    @Value("${salesforce.api.version}")
    private String APIVersion;
    TokenService tokenService;
    ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private static final String AUTHORIZATION="Authorization";
    private static final String RESPONSE_MESSAGE="Request failed with response code : ";

    @Autowired
    public SalesforceClient(RestTemplate restTemplate, TokenService tokenService){
        this.restTemplate=restTemplate;
        this.tokenService=tokenService;
    }


    public ResponseEntity<String> getFieldExecutiveData() {
        logger.info("inside >>>> SalesforceClient >>> getFieldExecutiveData");
        try{
        String adfstoken = tokenService.getBearerToken();

        String queryUrl = sfBaseUrl+"/data/"+APIVersion+"/queryAll?q=SELECT Id,Mobile__c,Name,Pincode_Master__c,Employee_code__c,Professional_Skills__c FROM Field_Executive__c" ; // Replace vXX.0 with the API version you want
            System.out.println(queryUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, "Bearer " + adfstoken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(queryUrl, HttpMethod.GET, entity, String.class);
            logger.debug("Field_Executive__c SF API response: {}",response.getBody());
            return response;
        } catch (Exception e) {
            logger.error("Exception occured in method getFieldExecutiveData : {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String>  getPincodeData(String filterStr) {
        logger.info("inside >>>> SalesforceClient >>> getFieldExecutiveData");
        try{
            String adfstoken = tokenService.getBearerToken();

            String queryUrl = sfBaseUrl+"/data/"+APIVersion+"/queryAll?q=SELECT Id,Pincode__c,City__c,State__c FROM Pincode_Master__c"+filterStr ; // Replace vXX.0 with the API version you want
            logger.debug("Pincode_Master__c SF queryUrl: {}",queryUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, "Bearer " + adfstoken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(queryUrl, HttpMethod.GET, entity, String.class);
            logger.debug("Pincode_Master__c SF API response: {}",response.getBody());
            return response;
        } catch (Exception e) {
            logger.error("Exception occured in method getFieldExecutiveData : {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String>  sendLosCaseRecords(LosCaseInitiationSFRequest losCaseInitiationDetails) {
        logger.info("inside >>>> SalesforceClient >>> sendLosCaseRecords");
        try{
            System.out.println("los request for SF : "+new ObjectMapper().writeValueAsString(losCaseInitiationDetails));
            String adfsToken = tokenService.getBearerToken();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, "Bearer " + adfsToken);
            HttpEntity<Object> entity = new HttpEntity<>(losCaseInitiationDetails,headers);
            ResponseEntity<String> response = restTemplate.exchange(sfBaseUrl+"/apexrest/v1/LOSServicesCreate", HttpMethod.POST, entity, String.class);
            logger.debug(" SF API response for LOS case records: {}",response.getBody());
            return response;
        } catch (Exception e) {
            logger.error("Exception occured in method sendLosCaseRecords : {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> getTechnicalHistory(LocalDateTime lastSyncTimestamp) {
        String query = "SELECT " +
                "Id, Parent.CreatedDate, Parent.Name, Parent.Report_Name__c, Parent.Client_Name__c, " +
                "Parent.Applicant_Details__r.Name, Parent.Field_Executive__r.Name, Parent.Status__c, " +
                "Parent.Drafter__r.Name, Parent.Case_initiated_by__r.Name, " +
                "Parent.Distance_from_Client_Branch__c, Parent.Distance_from_nearest_Greenfinch_Branch__c, " +
                "Parent.Technical_Visit_Date__c, Parent.Case__r.CaseNumber, " +
                "Parent.Coordinator__r.Name, Parent.Coordinator__r.State, Parent.Collateral__r.Name, " +
                "Field, OldValue, NewValue, CreatedDate, CreatedBy.Name " +
                "FROM Technical__History " +
                "WHERE Parent.CreatedDate >= " + lastSyncTimestamp.format(DateTimeFormatter.ISO_DATE_TIME) + "Z " +
                "OR CreatedDate >= " + lastSyncTimestamp.format(DateTimeFormatter.ISO_DATE_TIME) + "Z " +
                "ORDER BY CreatedDate DESC";
        return executeQuery(query);
    }

    public ResponseEntity<String> getLegalHistory(LocalDateTime lastSyncTimestamp) {
        String query = "SELECT " +
                "Id, Parent.CreatedDate, Parent.Name, Parent.Report_Name__c, Parent.Client_Name__c, " +
                "Parent.Applicant_Details__r.Name, Parent.Field_Executive__r.Name, Parent.Status__c, " +
                "Parent.Drafter__r.Name, Parent.Case_initiated_by__r.Name, " +
                "Parent.Distance_from_Client_Branch__c, Parent.Distance_from_nearest_Greenfinch_Branch__c, " +
                "Parent.Visit_Date__c, Parent.Case__r.CaseNumber, " +
                "Parent.Coordinator__r.Name, Parent.Coordinator__r.State, Parent.Collateral__r.Name, " +
                "Field, OldValue, NewValue, CreatedDate, CreatedBy.Name " +
                "FROM Legal__History " +
                "WHERE Parent.CreatedDate >= " + lastSyncTimestamp.format(DateTimeFormatter.ISO_DATE_TIME) + "Z " +
                "OR CreatedDate >= " + lastSyncTimestamp.format(DateTimeFormatter.ISO_DATE_TIME) + "Z " +
                "ORDER BY CreatedDate DESC";
        return executeQuery(query);
    }

    public ResponseEntity<String> getCreditHistory(LocalDateTime lastSyncTimestamp) {
        String query = "SELECT " +
                "Id, Parent.CreatedDate, Parent.Name, Parent.Report_Name__c, Parent.Client_Name__c, " +
                "Parent.Applicant_Details__r.Name, Parent.Field_Executive__r.Name, Parent.Status__c, " +
                "Parent.Drafter__r.Name, " +
                "Parent.Distance_from_Client_Branch__c, Parent.Distance_from_nearest_Greenfinch_Branch__c, " +
                "Parent.Visit_Date__c, Parent.Case__r.CaseNumber, Parent.Case__r.Reference_Number__c," +
                "Parent.Coordinator__r.Name, Parent.Coordinator__r.State, " +
                "Field, OldValue, NewValue, CreatedDate, CreatedBy.Name " +
                "FROM Credit_Verification__History " +
                "WHERE Parent.CreatedDate >= " + lastSyncTimestamp.format(DateTimeFormatter.ISO_DATE_TIME) + "Z " +
                "OR CreatedDate >= " + lastSyncTimestamp.format(DateTimeFormatter.ISO_DATE_TIME) + "Z " +
                "ORDER BY CreatedDate DESC";
        return executeQuery(query);
    }

    private ResponseEntity<String> executeQuery(String query) {
        try {
            String adfstoken = tokenService.getBearerToken();
            String queryUrl = sfBaseUrl + "/data/" + APIVersion + "/queryAll?q=" + query;
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, "Bearer " + adfstoken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(queryUrl, HttpMethod.GET, entity, String.class);
            logger.info("SF API response: {}", response.getBody());
            return response;
        } catch (Exception e) {
            logger.error("Exception occurred in method executeQuery : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}