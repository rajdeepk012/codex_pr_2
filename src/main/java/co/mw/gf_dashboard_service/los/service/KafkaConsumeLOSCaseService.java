package co.mw.gf_dashboard_service.los.service;

import co.mw.gf_dashboard_service.client.SalesforceClient;
import co.mw.gf_dashboard_service.common.LoggerUtil;

import co.mw.gf_dashboard_service.los.model.LosCaseInitiationInputModel;
import co.mw.gf_dashboard_service.los.model.LosCaseInitiationSFRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.MapperFeature;



@Component
public class KafkaConsumeLOSCaseService {

    private static final Logger logger = LoggerUtil.getLogger(KafkaConsumeLOSCaseService.class);
    @Autowired
    SalesforceClient salesforceClient;
    @Autowired
    LOSCaseIntegrationService losCaseIntegrationService;
    private final RestTemplate restTemplate;
    @Value("${gf.sf.frontline.services.host}")
    private String gfSfFrontlineUrl;

    public KafkaConsumeLOSCaseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


        @KafkaListener(groupId="${spring.kafka.consumer.los.group-id}",topics="los-data-v1",containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, String> record) {
            logger.info("Received LOS case records with key: {}",record.key());
            String accountId= "";
            try {
                logger.info("Received LOS cases object: {}", record.value());
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                LosCaseInitiationInputModel losCaseInitiationInputModel=objectMapper.readValue(record.value(),LosCaseInitiationInputModel.class);
                        logger.info("mapping values to losCaseInitiationInputModel:{}",new ObjectMapper().writeValueAsString(losCaseInitiationInputModel));
                        LosCaseInitiationSFRequest losCaseInitiationSFRequest=objectMapper.convertValue(losCaseInitiationInputModel, LosCaseInitiationSFRequest.class);

                logger.info("mapping values to losCaseInitiationSFRequest:{}",new ObjectMapper().writeValueAsString(losCaseInitiationSFRequest));
                List<LosCaseInitiationSFRequest.CaseDetail> caseDetailsList=losCaseInitiationSFRequest.getLOSServices().getListCaseDetail();
                if(!caseDetailsList.isEmpty() && caseDetailsList.size()!=0){
                    accountId=caseDetailsList.get(0).getAccountId();
                    caseDetailsList.forEach(caseDetail -> {
                        List<LosCaseInitiationSFRequest.DocumentChecklist> documentCheckLists=caseDetail.getDocumentChecklist();
                        if(!documentCheckLists.isEmpty() && documentCheckLists.size()!=0){
                            documentCheckLists.forEach(documentChecklist -> {
                                String fileName=generateFileNameForS3Upload(caseDetail.getAccountId(),caseDetail.getExternalId(),
                                        documentChecklist.getDocumentName(),documentChecklist.getDocumentFormat());
                              String versionDataS3Link= uploadVersionDataToS3(fileName,documentChecklist.getVersionData());
                                documentChecklist.setVersionData(versionDataS3Link);
                            });
                        }
                    });
                }
                ResponseEntity<String> response=
                        salesforceClient.sendLosCaseRecords(losCaseInitiationSFRequest);
                if(response.getStatusCode().is2xxSuccessful()){
                    losCaseIntegrationService.submitLosCaseResponse(accountId,objectMapper.readValue(response.getBody(),Object.class));
                }
                logger.info("SF response after sending LOS case records: {}",response.getBody());

//                return response.getBody();  //required for local testing

            } catch (Exception e) {
                e.printStackTrace();
                logger.error("error occurred while consuming LOS case records: {}",e.getMessage());
                losCaseIntegrationService.submitLosCaseResponse(accountId,e.getMessage());

//                return null;  //required for local testing
            }
    }

    private String generateFileNameForS3Upload(String accountId, String externalId, String documentName, String documentFormat) {
        String fileName = accountId+"/"+externalId+"/"+documentName.toLowerCase()+"."+documentFormat;
        String fileNameWithoutSpace = fileName.replaceAll("\\s+", "");
        return fileNameWithoutSpace;
    }

    private String uploadVersionDataToS3(String fileName, String versionData) {
        try {
            Map<String, Object> fileUploadRequest = new HashMap<>();
            fileUploadRequest.put("fileName", fileName);
            fileUploadRequest.put("fileStr", versionData); // Example Base64 string
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(fileUploadRequest, headers);
            ResponseEntity<String> response = restTemplate.exchange(gfSfFrontlineUrl+"/v1/s3doc/uploadFile", HttpMethod.POST,
                    entity, String.class);
            logger.info("S3 link after version Data upload : {}", response.getBody());
            return response.getBody().toString();
        }catch (Exception e) {
            logger.error("error occurred while uploading version data to S3: {}",e.getMessage());
            throw e;
        }}
}