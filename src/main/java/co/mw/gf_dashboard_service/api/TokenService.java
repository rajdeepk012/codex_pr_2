package co.mw.gf_dashboard_service.api;

import co.mw.gf_dashboard_service.common.LoggerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@Service
@Scope("singleton")
public class TokenService {
    private String bearerToken;
    private Instant expiryTime;

    @Value("${salesforce.adfs.granttype}")
    private String grantType;
    @Value("${salesforce.adfs.clientid}")
    private String clientId;
    @Value("${salesforce.adfs.clientsecret}")
    private String clientSecret;

    @Value("${salesforce.baseurl}")
    private  String sfBaseUrl;

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerUtil.getLogger(TokenService.class);

    @Autowired
    public TokenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String getBearerToken() {

        if (bearerToken == null || Instant.now().isAfter(expiryTime)) {
            fetchAccessToken();
        }
        return bearerToken;
    }

    private void fetchAccessToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.add("grant_type", grantType);
        formParams.add("client_id", clientId);
        formParams.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formParams, headers);
        String accessToken="";

        try
        {
            System.out.println(sfBaseUrl);
            ResponseEntity<String> response = restTemplate.postForEntity(sfBaseUrl+"/oauth2/token", request, String.class);
            if(response!=null)
            {
                Map<String, Object> responseMap = new ObjectMapper().readValue(response.getBody(), Map.class);
               this.bearerToken = (String) responseMap.get("access_token");
                this.expiryTime = Instant.now().plusSeconds(240);
            }
        }
        catch (Exception e) {
            logger.error("Exception in method fetchAccessToken {}",e.getMessage());
            throw new RuntimeException(e);
        }

    }


}
