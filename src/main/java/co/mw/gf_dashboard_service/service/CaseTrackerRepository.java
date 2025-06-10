package co.mw.gf_dashboard_service.service;

import co.mw.gf_dashboard_service.model.Case;

import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Repository
public interface CaseTrackerRepository extends MongoRepository<Case, String> {

}
