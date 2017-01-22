package net.benrowland.heatmap.client;

import net.benrowland.heatmap.dto.Activity;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
public class ActivityClient {
    private static final Logger logger = LoggerFactory.getLogger(ActivityClient.class);

    private static final String URL = "https://www.strava.com/api/v3/athlete/activities?per_page=100";

    @Autowired
    private RestTemplate restTemplate;

    public Activity[] getActivities(StravaUserEntity stravaUserEntity) throws StravaApiException {
        if(stravaUserEntity == null) {
            throw new IllegalArgumentException("Strava user is null");
        }

        logger.info("Retrieving latest activities for athlete " + stravaUserEntity.getStravaUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + stravaUserEntity.getAccessToken());

        HttpEntity requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Activity[]> response = null;
        try {
            response = restTemplate.exchange(URL, HttpMethod.GET, requestEntity, Activity[].class);
            logger.info("Successfully retrieved latest activities for athlete " + stravaUserEntity.getStravaUsername());
            return response.getBody();
        }
        catch(HttpStatusCodeException e) {
            throw new StravaApiException("Unexpected response from activity endpoint " + response.getStatusCode());
        }
    }
}
