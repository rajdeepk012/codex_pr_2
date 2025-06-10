package co.mw.gf_dashboard_service.los.service;

import co.mw.gf_dashboard_service.common.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Component
public class LOSCaseIntegrationService {

    private static final Logger logger = LoggerUtil.getLogger(LOSCaseIntegrationService.class);

    private final RestTemplate restTemplate;

    public LOSCaseIntegrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


public ResponseEntity<Object> submitLosCaseResponse(String accountId, Object request) {
            try {
                logger.info("Inside method submitLosCaseResponse for accountId: {}, {}",accountId, request);
            //    String webhookUrl = "https://webhook.site/582eff68-2305-4f87-bb57-bbd5b881b519";
                String webhookUrl = "https://play.svix.com/in/e_2IcYAdFK08hr3nXlu7VKri3fE9x/";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                Map<String, Object> requestObject = new HashMap<>();
                requestObject.put("accountId", accountId);
                requestObject.put("losResponse", request);
                HttpEntity<Object> entity = new HttpEntity<>(requestObject, headers);
                ResponseEntity<String> webhookResponse = restTemplate.exchange(webhookUrl, HttpMethod.POST, entity, String.class);

                return ResponseEntity.ok("LOS caseResponse sent successfully : " + webhookResponse.getBody());
            } catch (Exception e) {
                logger.error("error occurred while calling submitLosCaseResponse : {}",e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("API throwing an exception : "+e.getMessage());
            }
    }

}