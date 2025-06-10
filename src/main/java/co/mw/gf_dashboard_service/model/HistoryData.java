package co.mw.gf_dashboard_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "history_data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryData {
    @Id
    private String id;

    private LocalDateTime caseCreateDateTime;
    private String name;
    private String reportName;
    private String clientName;
    private String applicantName;
    private String fieldExecutiveName;
    private String status;
    private String drafterName;
    private String caseInitiatedByName;
    private Double distanceFromClientBranch;
    private Double distanceFromNearestGreenfinchBranch;
    private LocalDateTime visitDate;
    private String caseNumber;
    private String coordinatorName;
    private String coordinatorState;
    private String collateralName;
    private String field;
    private String oldValue;
    private String newValue;
    private LocalDateTime createdDate;
    private String createdByName;
    private String historyType; // TECHNICAL, LEGAL, or CREDIT
} 