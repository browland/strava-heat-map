package net.benrowland.heatmap.client;

import net.benrowland.heatmap.entity.StravaUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class StravaApi {

    private static final Logger logger = LoggerFactory.getLogger(StravaApi.class);

    private final RestTemplate restTemplate;

    StravaApi(@Autowired final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<T> call(final StravaUserEntity stravaUserEntity, final String endpoint, final Class<T> responseType)
        throws StravaApiException {

        verifyStravaUser(stravaUserEntity);

        HttpEntity requestEntity = setUpAuthentication(stravaUserEntity);
        try {
            return restTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, responseType);
        }
        catch(RestClientException e) {
            detectRateLimitingElseRethrow(stravaUserEntity, endpoint, e);
            return null;  // only for the compiler - better way to do this?
        }
    }

    /**
     * We detect cases where we're being rate-limited by the Strava API, and re-throw these as we don't want to try to
     * handle this.
     */
    private void detectRateLimitingElseRethrow(StravaUserEntity stravaUserEntity, String endpoint, RestClientException e)
        throws StravaApiException {

        if(e instanceof HttpStatusCodeException) {
            HttpStatusCodeException httpStatusCodeException = (HttpStatusCodeException)e;
            HttpStatus statusCode = httpStatusCodeException.getStatusCode();
            logger.error("Unexpected response {} when calling {}", httpStatusCodeException.getStatusCode(), endpoint);

            if(HttpStatus.FORBIDDEN.equals(statusCode)) {
                String message = String.format("Got 403 Forbidden from Strava API for strava user %s - possible rate-limiting",
                    stravaUserEntity.getStravaUsername());

                throw new StravaApiException(message);
            }
        }

        // We may still be able to handle the non-success case higher up
        throw e;
    }

    private HttpEntity setUpAuthentication(StravaUserEntity stravaUserEntity) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + stravaUserEntity.getAccessToken());

        return new HttpEntity<>(headers);
    }

    private void verifyStravaUser(StravaUserEntity stravaUserEntity) {
        if(stravaUserEntity == null) {
            throw new IllegalArgumentException("Strava user is null");
        }

        if(stravaUserEntity.getAccessToken() == null) {
            throw new IllegalArgumentException("Strava user has no access token set");
        }
    }
}
