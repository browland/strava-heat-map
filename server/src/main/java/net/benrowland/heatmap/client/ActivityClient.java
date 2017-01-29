package net.benrowland.heatmap.client;

import net.benrowland.heatmap.dto.Activity;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Component
public class ActivityClient {
    private static final Logger logger = LoggerFactory.getLogger(ActivityClient.class);

    private final String getActivitiesEndpoint;
    private final StravaApi stravaApi;

    ActivityClient(@Autowired final StravaApi stravaApi,
                   @Value("${strava.api.getActivities.endpoint}") final String getActivitiesEndpoint) {

        this.getActivitiesEndpoint = getActivitiesEndpoint;
        this.stravaApi = stravaApi;
    }

    public Activity[] getActivities(StravaUserEntity stravaUserEntity) throws StravaApiException {
        verifyStravaUser(stravaUserEntity);

        logger.info("Retrieving latest activities for athlete " + stravaUserEntity.getStravaUsername());

        ResponseEntity<Activity[]> response;
        try {
            response = stravaApi.call(stravaUserEntity, getActivitiesEndpoint, Activity[].class);
            logger.info("Successfully retrieved latest activities for athlete " + stravaUserEntity.getStravaUsername());
            return response.getBody();
        }
        catch(RestClientException e) {
            throw new StravaApiException("Error communicating with Strava API when getting activities", e);
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
