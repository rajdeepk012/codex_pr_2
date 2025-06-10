package co.mw.gf_dashboard_service.controller;


import co.mw.gf_dashboard_service.los.service.KafkaConsumeLOSCaseService;
import co.mw.gf_dashboard_service.model.UserDetails;
import co.mw.gf_dashboard_service.model.UserDetailsResponse;
import co.mw.gf_dashboard_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static co.mw.gf_dashboard_service.common.Constants.API_VERSION;


@RestController
@RequestMapping(API_VERSION+"/user-service")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    KafkaConsumeLOSCaseService kafkaConsumeLOSCaseService;
    @GetMapping(value = "/agents", produces = MediaType.APPLICATION_JSON_VALUE)
    public Slice<UserDetailsResponse> getAllAgents(@RequestParam(required = false) String pincode,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20000") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return userService.findAllAgents(pageable,pincode);
    }

//    @PostMapping(value = "/losCase")
//    public String getAllAgents(@RequestBody Object request) {
//        return kafkaConsumeLOSCaseService.listen(request);
//    }
}

