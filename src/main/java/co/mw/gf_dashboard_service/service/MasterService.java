package co.mw.gf_dashboard_service.service;

import co.mw.gf_dashboard_service.client.SalesforceClient;
import co.mw.gf_dashboard_service.common.LoggerUtil;
import co.mw.gf_dashboard_service.model.*;
import co.mw.gf_dashboard_service.model.Record;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;


@Service
public class MasterService {

    private static final DateTimeFormatter SALESFORCE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final String SYNC_TIMESTAMPS_COLLECTION = "sync_timestamps";

    @Autowired
    SalesforceClient salesforceClient;
    @Autowired
    MongoClient mongoClient;
    @Value("${mongodb.database}")
    private String dbName;
    private static final Logger logger = LoggerUtil.getLogger(MasterService.class);


    public void getDetailForMasterData() {
        logger.info("inside getFieldExecutionData ");
        try{
           ResponseEntity<String> fieldExecutionData = salesforceClient.getFieldExecutiveData();
            ObjectMapper objectMapper = new ObjectMapper();
            FieldExecutionData fieldExecutionDataModel = objectMapper.readValue(fieldExecutionData.getBody(), FieldExecutionData.class);
            logger.info("fieldExecutionDataModel size : {}",fieldExecutionDataModel.getRecords().size());
            Set<String> pins = new HashSet<>();
            for(Record data: fieldExecutionDataModel.getRecords())
                pins.add(data.getPincode_Master__c());
            pins.removeIf(Objects::isNull);
            String filterStr = " where id in ('";
            filterStr+= String.join("','", pins);
            filterStr+="')";
            ResponseEntity<String> pincodeData=salesforceClient.getPincodeData(filterStr);
            PinMasterData pinMasterData=objectMapper.readValue(pincodeData.getBody(), PinMasterData.class);
            logger.info("pinMasterData size : {}",pinMasterData.getRecords().size());
            List<MasterData> masterDataList=masterDataMapping(fieldExecutionDataModel,pinMasterData);
            logger.info("masterDataList size : {}",masterDataList.size());
           if(!masterDataList.isEmpty()){
               MongoDatabase database = mongoClient.getDatabase(dbName);
               MongoCollection<Document> userMasterData = database.getCollection("field_executive_master_data");

               for (MasterData record : masterDataList) {

                   Bson filter = Filters.eq("mobile", record.getMobile());
                   Bson update = Updates.combine(Updates.set("name", record.getName()),
                           Updates.set("pincode", record.getPincode()),
                           Updates.set("city", record.getCity()),
                           Updates.set("state", record.getState()),
                           Updates.set("pinMasterId", record.getPinMasterId()),
                           Updates.set("mobile", record.getMobile()),
                           Updates.set("employeeCode", record.getEmployeeCode()),
                           Updates.set("functionalArea", record.getFunctionalArea()));
                   UpdateOptions options = new UpdateOptions().upsert(true);

                   logger.info("performing upsert: {}",userMasterData.updateOne(filter, update, options));
               }
           }
        //    return ResponseEntity.ok("{ \"message\": \"This is a temporary endpoint to sync master data from SF to documentDB. It will be converted into scheduled job after testing.\" }");
         }catch (Exception e) {
            logger.error("Error in getDetailForMasterData", e);
        //    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal service error : " + e.getMessage());
        }}

    private List<MasterData> masterDataMapping(FieldExecutionData fieldExecutionData, PinMasterData pinMasterData) {
        try {
            Map<String, PinRecord> pincodeMasterMap = pinMasterData.getRecords().stream()
                    .collect(Collectors.toMap(PinRecord::getId, pincodeMaster -> pincodeMaster));
            //            System.out.println(pinRecord.getCity__c());
            // If no match is found, return null (or handle as needed)
            // Filter out null records (where no match was found)
            return fieldExecutionData.getRecords().stream()
                    .map(fieldExecutive -> {
                        PinRecord pinRecord = pincodeMasterMap.get(fieldExecutive.getPincode_Master__c());
                        logger.debug("employee code: {}, name: {}, mobile: {}",fieldExecutive.getEmployee_Code__c(),fieldExecutive.getName(),fieldExecutive.getMobile__c());
                        MasterData masterData = new MasterData();
                        masterData.setName(fieldExecutive.getId());
                        masterData.setMobile(fieldExecutive.getMobile__c());
                        masterData.setName(fieldExecutive.getName());
                        masterData.setPinMasterId(fieldExecutive.getPincode_Master__c());
                        masterData.setEmployeeCode(fieldExecutive.getEmployee_Code__c());
                        masterData.setFunctionalArea(fieldExecutive.getProfessional_Skills__c());
                        if (pinRecord != null) {
                            masterData.setPincode(pinRecord.getPincode__c());
                            masterData.setCity(pinRecord.getCity__c());
                            masterData.setState(pinRecord.getState__c());
                        } else {
                            masterData.setPincode("");
                            masterData.setCity("");
                            masterData.setState("");
                        }
                        // If no match is found, return null (or handle as needed)
                        return masterData;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error in masterDataMapping", e);
            throw e;
        }
    }

    public void syncHistoryData() {
        try {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> syncTimestampsCollection = database.getCollection(SYNC_TIMESTAMPS_COLLECTION);

            // Get last sync timestamps
            LocalDateTime technicalLastSync = getLastSyncTimestamp(syncTimestampsCollection, "TECHNICAL");
            LocalDateTime legalLastSync = getLastSyncTimestamp(syncTimestampsCollection, "LEGAL");
            LocalDateTime creditLastSync = getLastSyncTimestamp(syncTimestampsCollection, "CREDIT");

            CompletableFuture<List<HistoryData>> technicalFuture = getTechnicalHistory(technicalLastSync);
            CompletableFuture<List<HistoryData>> legalFuture = getLegalHistory(legalLastSync);
            CompletableFuture<List<HistoryData>> creditFuture = getCreditHistory(creditLastSync);

            CompletableFuture.allOf(technicalFuture, legalFuture, creditFuture)
                    .thenAccept(v -> {
                        List<HistoryData> allHistory = new ArrayList<>();
                        allHistory.addAll(technicalFuture.join());
                        allHistory.addAll(legalFuture.join());
                        allHistory.addAll(creditFuture.join());

                        if (!allHistory.isEmpty()) {
                            updateMongoDb(allHistory);
                            
                            // Update sync timestamps
                            LocalDateTime now = LocalDateTime.now();
                            updateSyncTimestamp(syncTimestampsCollection, "TECHNICAL", now);
                            updateSyncTimestamp(syncTimestampsCollection, "LEGAL", now);
                            updateSyncTimestamp(syncTimestampsCollection, "CREDIT", now);
                        }
                    })
                    .exceptionally(e -> {
                        logger.error("Error in history sync", e);
                        return null;
                    });
        } catch (Exception e) {
            logger.error("Error in syncHistoryData", e);
        }
    }

    private LocalDateTime getLastSyncTimestamp(MongoCollection<Document> collection, String category) {
        Document doc = collection.find(Filters.eq("category", category)).first();
        if (doc != null && doc.get("lastSyncTimestamp") != null) {
            return LocalDateTime.parse(doc.getString("lastSyncTimestamp"));
        }
        // Return a default date if no sync has happened yet
        return LocalDateTime.of(2025, 6, 1, 6, 15, 0);
    }

    private void updateSyncTimestamp(MongoCollection<Document> collection, String category, LocalDateTime timestamp) {
        Document filter = new Document("category", category);
        Document update = new Document("$set", new Document("lastSyncTimestamp", timestamp.toString()));
        collection.updateOne(filter, update, new UpdateOptions().upsert(true));
    }

    @Async
    public CompletableFuture<List<HistoryData>> getTechnicalHistory(LocalDateTime lastSyncTimestamp) {
        try {
            ResponseEntity<String> response = salesforceClient.getTechnicalHistory(lastSyncTimestamp);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode records = root.get("records");
            List<HistoryData> historyDataList = new ArrayList<>();
            
            for (JsonNode record : records) {
                HistoryData historyData = new HistoryData();
                JsonNode parent = record.get("Parent");
                if (parent == null) continue;

                String casecreatedDate = getTextSafely(parent, "CreatedDate");
                if (casecreatedDate != null && !casecreatedDate.equals("null")) {
                    try {
                        historyData.setCaseCreateDateTime(LocalDateTime.parse(casecreatedDate, SALESFORCE_DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        logger.warn("Failed to parse created date: {}", casecreatedDate);
                    }
                }

                historyData.setId(getTextSafely(parent, "Name"));
                historyData.setName(getTextSafely(parent, "Name"));
                historyData.setReportName(getTextSafely(parent, "Report_Name__c"));
                historyData.setClientName(getTextSafely(parent, "Client_Name__c"));
                
                JsonNode applicantDetails = parent.get("Applicant_Details__r");
                historyData.setApplicantName(applicantDetails != null ? getTextSafely(applicantDetails, "Name") : null);
                
                JsonNode fieldExecutive = parent.get("Field_Executive__r");
                historyData.setFieldExecutiveName(fieldExecutive != null ? getTextSafely(fieldExecutive, "Name") : null);
                
                historyData.setStatus(getTextSafely(parent, "Status__c"));
                
                JsonNode drafter = parent.get("Drafter__r");
                historyData.setDrafterName(drafter != null ? getTextSafely(drafter, "Name") : null);
                
                JsonNode caseInitiatedBy = parent.get("Case_initiated_by__r");
                historyData.setCaseInitiatedByName(caseInitiatedBy != null ? getTextSafely(caseInitiatedBy, "Name") : null);
                
                historyData.setDistanceFromClientBranch(getDoubleSafely(parent, "Distance_from_Client_Branch__c"));
                historyData.setDistanceFromNearestGreenfinchBranch(getDoubleSafely(parent, "Distance_from_nearest_Greenfinch_Branch__c"));
                
                String visitDate = getTextSafely(parent, "Technical_Visit_Date__c");
                if (visitDate != null && !visitDate.equals("null")) {
                    try {
                        historyData.setVisitDate(LocalDateTime.parse(visitDate, SALESFORCE_DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        logger.warn("Failed to parse visit date: {}", visitDate);
                    }
                }
                
                JsonNode caseNode = parent.get("Case__r");
                historyData.setCaseNumber(caseNode != null ? getTextSafely(caseNode, "CaseNumber") : null);
                
                JsonNode coordinator = parent.get("Coordinator__r");
                if (coordinator != null) {
                    historyData.setCoordinatorName(getTextSafely(coordinator, "Name"));
                    historyData.setCoordinatorState(getTextSafely(coordinator, "State"));
                }
                
                JsonNode collateral = parent.get("Collateral__r");
                historyData.setCollateralName(collateral != null ? getTextSafely(collateral, "Name") : null);
                
                historyData.setField(getTextSafely(record, "Field"));
                historyData.setOldValue(getTextSafely(record, "OldValue"));
                historyData.setNewValue(getTextSafely(record, "NewValue"));
                
                String createdDate = getTextSafely(record, "CreatedDate");
                if (createdDate != null && !createdDate.equals("null")) {
                    try {
                        historyData.setCreatedDate(LocalDateTime.parse(createdDate, SALESFORCE_DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        logger.warn("Failed to parse created date: {}", createdDate);
                    }
                }
                
                JsonNode createdBy = record.get("CreatedBy");
                historyData.setCreatedByName(createdBy != null ? getTextSafely(createdBy, "Name") : null);
                
                historyData.setHistoryType("TECHNICAL");
                
                historyDataList.add(historyData);
            }
            
            return CompletableFuture.completedFuture(historyDataList);
        } catch (Exception e) {
            logger.error("Error fetching technical history", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<List<HistoryData>> getLegalHistory(LocalDateTime lastSyncTimestamp) {
        try {
            ResponseEntity<String> response = salesforceClient.getLegalHistory(lastSyncTimestamp);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode records = root.get("records");
            List<HistoryData> historyDataList = new ArrayList<>();
            
            for (JsonNode record : records) {
                HistoryData historyData = new HistoryData();
                JsonNode parent = record.get("Parent");
                if (parent == null) continue;

                String casecreatedDate = getTextSafely(parent, "CreatedDate");
                if (casecreatedDate != null && !casecreatedDate.equals("null")) {
                    try {
                        historyData.setCaseCreateDateTime(LocalDateTime.parse(casecreatedDate, SALESFORCE_DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        logger.warn("Failed to parse created date: {}", casecreatedDate);
                    }
                }

                historyData.setId(getTextSafely(parent, "Name"));
                historyData.setName(getTextSafely(parent, "Name"));
                historyData.setReportName(getTextSafely(parent, "Report_Name__c"));
                historyData.setClientName(getTextSafely(parent, "Client_Name__c"));
                
                JsonNode applicantDetails = parent.get("Applicant_Details__r");
                historyData.setApplicantName(applicantDetails != null ? getTextSafely(applicantDetails, "Name") : null);
                
                JsonNode fieldExecutive = parent.get("Field_Executive__r");
                historyData.setFieldExecutiveName(fieldExecutive != null ? getTextSafely(fieldExecutive, "Name") : null);
                
                historyData.setStatus(getTextSafely(parent, "Status__c"));
                
                JsonNode drafter = parent.get("Drafter__r");
                historyData.setDrafterName(drafter != null ? getTextSafely(drafter, "Name") : null);
                
                JsonNode caseInitiatedBy = parent.get("Case_initiated_by__r");
                historyData.setCaseInitiatedByName(caseInitiatedBy != null ? getTextSafely(caseInitiatedBy, "Name") : null);
                
                historyData.setDistanceFromClientBranch(getDoubleSafely(parent, "Distance_from_Client_Branch__c"));
                historyData.setDistanceFromNearestGreenfinchBranch(getDoubleSafely(parent, "Distance_from_nearest_Greenfinch_Branch__c"));
                
                String visitDate = getTextSafely(parent, "Visit_Date__c");
                if (visitDate != null && !visitDate.equals("null")) {
                    try {
                        historyData.setVisitDate(LocalDateTime.parse(visitDate, SALESFORCE_DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        logger.warn("Failed to parse visit date: {}", visitDate);
                    }
                }
                
                JsonNode caseNode = parent.get("Case__r");
                historyData.setCaseNumber(caseNode != null ? getTextSafely(caseNode, "CaseNumber") : null);
                
                JsonNode coordinator = parent.get("Coordinator__r");
                if (coordinator != null) {
                    historyData.setCoordinatorName(getTextSafely(coordinator, "Name"));
                    historyData.setCoordinatorState(getTextSafely(coordinator, "State"));
                }
                
                JsonNode collateral = parent.get("Collateral__r");
                historyData.setCollateralName(collateral != null ? getTextSafely(collateral, "Name") : null);
                
                historyData.setField(getTextSafely(record, "Field"));
                historyData.setOldValue(getTextSafely(record, "OldValue"));
                historyData.setNewValue(getTextSafely(record, "NewValue"));
                
                String createdDate = getTextSafely(record, "CreatedDate");
                if (createdDate != null && !createdDate.equals("null")) {
                    try {
                        historyData.setCreatedDate(LocalDateTime.parse(createdDate, SALESFORCE_DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        logger.warn("Failed to parse created date: {}", createdDate);
                    }
                }
                
                JsonNode createdBy = record.get("CreatedBy");
                historyData.setCreatedByName(createdBy != null ? getTextSafely(createdBy, "Name") : null);
                
                historyData.setHistoryType("LEGAL");
                
                historyDataList.add(historyData);
            }
            
            return CompletableFuture.completedFuture(historyDataList);
        } catch (Exception e) {
            logger.error("Error fetching legal history", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<List<HistoryData>> getCreditHistory(LocalDateTime lastSyncTimestamp) {
        try {
            ResponseEntity<String> response = salesforceClient.getCreditHistory(lastSyncTimestamp);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode records = root.get("records");
            List<HistoryData> historyDataList = new ArrayList<>();
            
            for (JsonNode record : records) {
                HistoryData historyData = new HistoryData();
                JsonNode parent = record.get("Parent");
                if (parent == null) continue;

                String casecreatedDate = getTextSafely(parent, "CreatedDate");
                if (casecreatedDate != null && !casecreatedDate.equals("null")) {
                    try {
                        historyData.setCaseCreateDateTime(LocalDateTime.parse(casecreatedDate, SALESFORCE_DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        logger.warn("Failed to parse created date: {}", casecreatedDate);
                    }
                }

                historyData.setId(getTextSafely(parent, "Name"));
                historyData.setName(getTextSafely(parent, "Name"));
                historyData.setReportName(getTextSafely(parent, "Report_Name__c"));
                historyData.setClientName(getTextSafely(parent, "Client_Name__c"));
                
                JsonNode applicantDetails = parent.get("Applicant_Details__r");
                historyData.setApplicantName(applicantDetails != null ? getTextSafely(applicantDetails, "Name") : null);
                
                JsonNode fieldExecutive = parent.get("Field_Executive__r");
                historyData.setFieldExecutiveName(fieldExecutive != null ? getTextSafely(fieldExecutive, "Name") : null);
                
                historyData.setStatus(getTextSafely(parent, "Status__c"));
                
                JsonNode drafter = parent.get("Drafter__r");
                historyData.setDrafterName(drafter != null ? getTextSafely(drafter, "Name") : null);
                
                historyData.setDistanceFromClientBranch(getDoubleSafely(parent, "Distance_from_Client_Branch__c"));
                historyData.setDistanceFromNearestGreenfinchBranch(getDoubleSafely(parent, "Distance_from_nearest_Greenfinch_Branch__c"));
                
                String visitDate = getTextSafely(parent, "Visit_Date__c");
                if (visitDate != null && !visitDate.equals("null")) {
                    try {
                        historyData.setVisitDate(LocalDateTime.parse(visitDate, SALESFORCE_DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        logger.warn("Failed to parse visit date: {}", visitDate);
                    }
                }
                
                JsonNode caseNode = parent.get("Case__r");
                historyData.setCaseNumber(caseNode != null ? getTextSafely(caseNode, "CaseNumber") : null);
                
                JsonNode coordinator = parent.get("Coordinator__r");
                if (coordinator != null) {
                    historyData.setCoordinatorName(getTextSafely(coordinator, "Name"));
                    historyData.setCoordinatorState(getTextSafely(coordinator, "State"));
                }
                
                historyData.setField(getTextSafely(record, "Field"));
                historyData.setOldValue(getTextSafely(record, "OldValue"));
                historyData.setNewValue(getTextSafely(record, "NewValue"));
                
                String createdDate = getTextSafely(record, "CreatedDate");
                if (createdDate != null && !createdDate.equals("null")) {
                    try {
                        historyData.setCreatedDate(LocalDateTime.parse(createdDate, SALESFORCE_DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        logger.warn("Failed to parse created date: {}", createdDate);
                    }
                }
                
                JsonNode createdBy = record.get("CreatedBy");
                historyData.setCreatedByName(createdBy != null ? getTextSafely(createdBy, "Name") : null);
                
                historyData.setHistoryType("CREDIT");
                
                historyDataList.add(historyData);
            }
            
            return CompletableFuture.completedFuture(historyDataList);
        } catch (Exception e) {
            logger.error("Error fetching credit history", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    private String getTextSafely(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        return fieldNode != null ? fieldNode.asText() : null;
    }

    private Double getDoubleSafely(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        return fieldNode != null ? fieldNode.asDouble() : null;
    }

    private void updateMongoDb(List<HistoryData> historyDataList) {
        if (historyDataList.isEmpty())
                return;

        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> historyCollection = database.getCollection("history_data");

        List<WriteModel<Document>> bulkOperations = historyDataList.stream()
            .map(history -> {
                Document document = convertToDocument(history);
                // Add a unique identifier combining id and createdDate to ensure uniqueness
                document.append("_id", history.getId() + "_" + history.getCreatedDate());
                return new InsertOneModel<>(document);
            })
            .collect(Collectors.toList());

        if (!bulkOperations.isEmpty()) {
            try {
                BulkWriteResult result = historyCollection.bulkWrite(bulkOperations, new BulkWriteOptions().ordered(false));
                logger.info("Bulk insert completed. Inserted: {}", result.getInsertedCount());
            } catch (MongoBulkWriteException e) {
                // Log duplicate key errors but continue processing
                logger.warn("Some records were duplicates and were skipped: {}", e.getMessage());
            }
        }
    }

    private Document convertToDocument(HistoryData history) {
        Document doc = new Document();
        doc.append("id", history.getId());
        doc.append("caseCreateDateTime", history.getCaseCreateDateTime());
        doc.append("name", history.getName());
        doc.append("reportName", history.getReportName());
        doc.append("clientName", history.getClientName());
        doc.append("applicantName", history.getApplicantName());
        doc.append("fieldExecutiveName", history.getFieldExecutiveName());
        doc.append("status", history.getStatus());
        doc.append("drafterName", history.getDrafterName());
        doc.append("caseInitiatedByName", history.getCaseInitiatedByName());
        doc.append("distanceFromClientBranch", history.getDistanceFromClientBranch());
        doc.append("distanceFromNearestGreenfinchBranch", history.getDistanceFromNearestGreenfinchBranch());
        doc.append("visitDate", history.getVisitDate());
        doc.append("caseNumber", history.getCaseNumber());
        doc.append("coordinatorName", history.getCoordinatorName());
        doc.append("coordinatorState", history.getCoordinatorState());
        doc.append("collateralName", history.getCollateralName());
        doc.append("field", history.getField());
        doc.append("oldValue", history.getOldValue());
        doc.append("newValue", history.getNewValue());
        doc.append("createdDate", history.getCreatedDate());
        doc.append("createdByName", history.getCreatedByName());
        doc.append("historyType", history.getHistoryType());
        return doc;
    }
}
