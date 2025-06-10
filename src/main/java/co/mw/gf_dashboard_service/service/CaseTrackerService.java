package co.mw.gf_dashboard_service.service;

import co.mw.gf_dashboard_service.client.SalesforceClient;
import co.mw.gf_dashboard_service.model.Case;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class CaseTrackerService {
    @Autowired
    MongoClient mongoClient;
    @Value("${mongodb.database}")
    private String dbName;
    private final CaseTrackerRepository caseTrackerRepository;

    public CaseTrackerService(CaseTrackerRepository caseTrackerRepository) {
        this.caseTrackerRepository = caseTrackerRepository;
    }

    public MongoCollection<Document> getAllCases() {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> userMasterData = database.getCollection("history_data");

        return userMasterData;
        // For now, return the first 100 cases
        // Pageable pageable = PageRequest.of(0, 100);
        // return caseTrackerRepository.findAll(pageable).getContent();
    }
}
