package co.mw.gf_dashboard_service.service;

import co.mw.gf_dashboard_service.model.HistoryData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing history data for the case tracker.
 * <p>
 * The {@link HistoryData} model is mapped to the {@code history_data}
 * collection in MongoDB via the {@code @Document} annotation. Extending
 * {@link MongoRepository} provides basic CRUD operations which can be used by
 * the service layer.
 */
@Repository
public interface CaseTrackerRepository extends MongoRepository<HistoryData, String> {
}
