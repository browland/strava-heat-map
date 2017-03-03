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
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@Component
public class ActivityClient {
    private static final Logger logger = LoggerFactory.getLogger(ActivityClient.class);
    private static final String PER_PAGE_PARAM_NAME = "per_page";

    private final String getActivitiesEndpoint;
    private final int getActivitiesPerPage;
    private final StravaApi stravaApi;

    ActivityClient(@Autowired final StravaApi stravaApi,
                   @Value("${strava.api.getActivities.endpoint}") final String getActivitiesEndpoint,
                   @Value("${strava.api.getActivities.perPage}") final int getActivitiesPerPage) {

        this.getActivitiesEndpoint = getActivitiesEndpoint;
        this.getActivitiesPerPage = getActivitiesPerPage;
        this.stravaApi = stravaApi;
    }

    public Activity[] getActivities(StravaUserEntity stravaUserEntity) throws StravaApiException {
        return getActivities(stravaUserEntity, null);
    }

    public Activity[] getActivities(StravaUserEntity stravaUserEntity, LocalDateTime afterDate) throws StravaApiException {
        verifyStravaUser(stravaUserEntity);

        logger.info("Retrieving latest activities for athlete {} after date {}", stravaUserEntity.getStravaUsername(), afterDate);

        ResponseEntity<Activity[]> response;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getActivitiesEndpoint).queryParam(PER_PAGE_PARAM_NAME, getActivitiesPerPage);
            final String uriString = builder.build().toUriString();

            response = stravaApi.call(stravaUserEntity, uriString, Activity[].class);
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
