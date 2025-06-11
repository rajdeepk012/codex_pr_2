package co.mw.gf_dashboard_service.controller;

import co.mw.gf_dashboard_service.model.HistoryData;
import co.mw.gf_dashboard_service.service.CaseTrackerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/casetracker")
public class CaseTrackerController {

    private final CaseTrackerService caseTrackerService;

    public CaseTrackerController(CaseTrackerService caseTrackerService) {
        this.caseTrackerService = caseTrackerService;
    }

    @GetMapping("/cases")
    public ResponseEntity<List<HistoryData>> getAllCases() {
        List<HistoryData> cases = caseTrackerService.getLatestCases();
        return ResponseEntity.ok(cases);
    }
}


