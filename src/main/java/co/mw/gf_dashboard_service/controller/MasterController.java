package co.mw.gf_dashboard_service.controller;


import co.mw.gf_dashboard_service.service.MasterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static co.mw.gf_dashboard_service.common.Constants.API_VERSION;

//@RestController
//@RequestMapping(API_VERSION+"/dashboard/master")
@Component
public class MasterController {

    @Autowired
    private MasterService masterService;
    //@GetMapping(value = "/updateMasterData", produces = MediaType.APPLICATION_JSON_VALUE)
    @Scheduled(cron = "${salesforce.data.sync.cron}")
    public void updateMasterData() throws JsonProcessingException {
        masterService.getDetailForMasterData();
    }

    @Scheduled(fixedRate = 600000) // 10 minutes in milliseconds
    public void syncHistoryData() {
        masterService.syncHistoryData();
    }
}
