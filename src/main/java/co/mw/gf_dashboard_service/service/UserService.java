package co.mw.gf_dashboard_service.service;

import co.mw.gf_dashboard_service.common.LoggerUtil;
import co.mw.gf_dashboard_service.model.Case;
import co.mw.gf_dashboard_service.model.CaseCounts;
import co.mw.gf_dashboard_service.model.UserDetails;
import co.mw.gf_dashboard_service.model.UserDetailsResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerUtil.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Slice<UserDetailsResponse> findAllAgents(Pageable pageable, String pincode) {
        logger.info(" inside findAllAgents ");
        try {
            Slice<UserDetails> userDetailsList;
            CaseCounts assignAndDraftCaseCounts;
            int completedCaseCounts = 0;

            if(pincode != null) {
                userDetailsList = userRepository.getAllUsersByPincode(pageable, pincode);
                assignAndDraftCaseCounts = userRepository.getAssignAndDraftCaseCountsByPincode(pincode);
                List<Integer> result = userRepository.getCompletedCaseCountsByPincode(pincode);
                completedCaseCounts = result.isEmpty()?0:result.get(0);
            }
            else {
                userDetailsList = userRepository.getAllUsers(pageable);
                assignAndDraftCaseCounts = userRepository.getAssignAndDraftCaseCounts();
                List<Integer> result = userRepository.getCompletedCaseCounts();
                completedCaseCounts = result.isEmpty()?0:result.get(0);
            }
            logger.info("db response received in findAllAgents. total records: {}",userDetailsList.getNumberOfElements());
            List<UserDetailsResponse> l = new ArrayList<>();
            UserDetailsResponse r;
            if(userDetailsList.getContent().isEmpty())
                r = new UserDetailsResponse(0, 0, 0, 0, userDetailsList.getContent());
            else {
                userDetailsList.stream().forEach(userDetails -> {
                    int count = 0;
                    List<Case> toRemoveCases = new ArrayList<>();
                    for(Case aCase: userDetails.getCaseInfo().getCases()) {
                        if (aCase.getCaseCompletionAt() != null && !aCase.getCaseCompletionAt().isEmpty()
                                && LocalDate.parse(aCase.getCaseCompletionAt().substring(0, 10)).equals(LocalDate.now())) {
                            count++;
                        } else if (aCase.getCaseCompletionAt() != null && !aCase.getCaseCompletionAt().isEmpty()) {
                            toRemoveCases.add(aCase);
                        }
                    }
                    userDetails.getCaseInfo().getCases().removeAll(toRemoveCases);
                    userDetails.getCaseInfo().setCompletedCaseCount(count);
                });
                if(assignAndDraftCaseCounts != null )
                    r = new UserDetailsResponse(assignAndDraftCaseCounts.getAssignedCaseCount(), assignAndDraftCaseCounts.getDraftCaseCount(), completedCaseCounts, (int) userRepository.count(), userDetailsList.getContent());
                else
                    r = new UserDetailsResponse(0, 0, completedCaseCounts, (int) userRepository.count(), userDetailsList.getContent());
            }
                l.add(r);
            Slice<UserDetailsResponse> slicedResponse = new SliceImpl<>(l,userDetailsList.getPageable(),userDetailsList.hasNext());
        //    logger.info("case counts-> assigned, draft, completed: {} {} {}",assignAndDraftCaseCounts.getAssignedCaseCount(),assignAndDraftCaseCounts.getDraftCaseCount(),completedCaseCounts);
            logger.info("end findAllAgents");
            return slicedResponse;
        } catch (Exception e) {
            logger.error("Error in findAllAgents", e);
            throw e;
        }
    }
}
