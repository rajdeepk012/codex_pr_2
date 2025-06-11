package co.mw.gf_dashboard_service.service;

import co.mw.gf_dashboard_service.model.HistoryData;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class CaseTrackerService {
    private final CaseTrackerRepository caseTrackerRepository;

    public CaseTrackerService(CaseTrackerRepository caseTrackerRepository) {
        this.caseTrackerRepository = caseTrackerRepository;
    }

    /**
     * Fetches the most recent 100 history records from the repository.
     *
     * @return list of {@link HistoryData} ordered by {@code createdDate} descending
     */
    public List<HistoryData> getLatestCases() {
        Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "createdDate"));
        return caseTrackerRepository.findAll(pageable).getContent();
    }
}
