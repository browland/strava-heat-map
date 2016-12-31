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
import org.springframework.web.client.RestTemplate;

@Component
public class OAuthClient {
    private static final Logger logger = LoggerFactory.getLogger(OAuthClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${strava.oauth.completion.completionUri}")
    private String completionUri;

    @Value("${strava.oauth.clientSecret}")
    private String clientSecret;

    @Value("${strava.oauth.clientId}")
    private String clientId;

    public OAuthCompletionResponse authorise(final String authorisationCode) throws StravaApiException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("code", authorisationCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<OAuthCompletionResponse> response = restTemplate.postForEntity(completionUri, request, OAuthCompletionResponse.class);

        if(response.getStatusCode().is2xxSuccessful()) {
            logger.info("Successfully called OAuth completion endpoint");
            return response.getBody();
        }
        else {
            throw new StravaApiException("Unexpected response from OAuth completion endpoint " + response.getStatusCode());
        }
    }
}
