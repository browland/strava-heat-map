package net.benrowland.heatmap.client;

import net.benrowland.heatmap.dto.OAuthCompletionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class OAuthClient {
    private static final Logger logger = LoggerFactory.getLogger(OAuthClient.class);

    private RestTemplate restTemplate;
    private String completionUri;
    private String clientSecret;
    private String clientId;

    public OAuthClient(@Autowired RestTemplate restTemplate,
                       @Value("${strava.oauth.completionUri}") String completionUri,
                       @Value("${strava.client.clientSecret}") String clientSecret,
                       @Value("${strava.client.clientId}") String clientId) {
        this.restTemplate = restTemplate;
        this.completionUri = completionUri;
        this.clientSecret = clientSecret;
        this.clientId = clientId;
    }

    public OAuthCompletionResponse authorise(final String authorisationCode) throws StravaApiException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", authorisationCode);

        HttpEntity<MultiValueMap<String, String>> requestParamsAndHeaders = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<OAuthCompletionResponse> responseEntity =
                restTemplate.postForEntity(completionUri, requestParamsAndHeaders, OAuthCompletionResponse.class);

            logger.info("Successfully called OAuth completion endpoint");
            return responseEntity.getBody();
        }
        catch(RestClientException e) {
            logger.error("Unexpected response from OAuth completion endpoint", e);
            throw new StravaApiException("Unexpected response from OAuth completion endpoint", e);
        }
    }
}
