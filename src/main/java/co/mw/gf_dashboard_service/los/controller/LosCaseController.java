package co.mw.gf_dashboard_service.los.controller;

import co.mw.gf_dashboard_service.common.LoggerUtil;
import co.mw.gf_dashboard_service.los.service.KafkaConsumeLOSCaseService;
import co.mw.gf_dashboard_service.los.service.LOSCaseIntegrationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static co.mw.gf_dashboard_service.common.Constants.API_VERSION;

@RestController
@RequestMapping(API_VERSION+"/los")
public class LosCaseController {
    @Autowired
    LOSCaseIntegrationService losCaseIntegrationService;
    private static final Logger logger = LoggerUtil.getLogger(LosCaseController.class);

    @PostMapping("/case/{accountId}/submit-response")
    public ResponseEntity<Object> submitLosCaseResponse(@PathVariable("accountId") String accountId,@RequestBody Object request){
        logger.info("inside >>>LosCaseController >>> submitLosCaseResponse method");
        logger.info("submitLosCaseResponse accountId : {}",accountId);
        return losCaseIntegrationService.submitLosCaseResponse(accountId,request);
    }
}
