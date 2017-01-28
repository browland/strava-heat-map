package net.benrowland.heatmap.client;

import net.benrowland.heatmap.dto.Activity;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ActivityClient {
    private static final Logger logger = LoggerFactory.getLogger(ActivityClient.class);

    private String getActivitiesEndpoint;
    private RestTemplate restTemplate;

    ActivityClient(@Autowired final RestTemplate restTemplate,
                   @Value("${strava.api.getActivities.endpoint}") final String getActivitiesEndpoint) {

        this.getActivitiesEndpoint = getActivitiesEndpoint;
        this.restTemplate = restTemplate;
    }

    public Activity[] getActivities(StravaUserEntity stravaUserEntity) throws StravaApiException {
        verifyStravaUser(stravaUserEntity);

        logger.info("Retrieving latest activities for athlete " + stravaUserEntity.getStravaUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + stravaUserEntity.getAccessToken());
        HttpEntity requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Activity[]> response;
        try {
            response = restTemplate.exchange(getActivitiesEndpoint, HttpMethod.GET, requestEntity, Activity[].class);
            logger.info("Successfully retrieved latest activities for athlete " + stravaUserEntity.getStravaUsername());
            return response.getBody();
        }
        catch(RestClientException e) {
            if(e instanceof HttpStatusCodeException) {
                HttpStatusCodeException statusCodeException = (HttpStatusCodeException) e;
                throw new StravaApiException("Unexpected response from activity endpoint " + statusCodeException.getStatusCode());
            }
            else {
                throw new StravaApiException("Error communicating with Strava API when getting activities", e);
            }
        }
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
