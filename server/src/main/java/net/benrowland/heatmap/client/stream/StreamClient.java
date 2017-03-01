package net.benrowland.heatmap.client.stream;

import net.benrowland.heatmap.client.StravaApi;
import net.benrowland.heatmap.client.StravaApiException;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Component
public class StreamClient {
    private static final Logger logger = LoggerFactory.getLogger(StreamClient.class);

    private final String getStreamsEndpoint;
    private final StravaApi stravaApi;

    StreamClient(@Value("${strava.api.getStreams.endpoint}") final String getStreamsEndpoint,
                 @Autowired final StravaApi stravaApi) {

        this.getStreamsEndpoint = getStreamsEndpoint;
        this.stravaApi = stravaApi;
    }

    @SuppressWarnings("unchecked")
    public StravaStream[] getStreams(StravaUserEntity stravaUserEntity, long activityId) throws StravaApiException {
        logger.info("Retrieving streams for activity id {} for strava user {}", activityId, stravaUserEntity.getStravaUsername());

        String endpoint = String.format(getStreamsEndpoint, activityId);
        try {
            ResponseEntity<StravaStream[]> response = stravaApi.call(stravaUserEntity, endpoint, StravaStream[].class);

            logger.info("Successfully retrieved streams for activity {}, strava user {}",
                activityId, stravaUserEntity.getStravaUsername());

            return response.getBody();
        }
        catch(RestClientException e) {
            throw new StravaApiException(String.format("When getting stream for activity %d", activityId), e);
        }
    }
}
