package net.benrowland.heatmap.client;

import net.benrowland.heatmap.dto.Stream;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.Map;
import java.util.Optional;

@Component
public class StreamClient {
    private static final Logger logger = LoggerFactory.getLogger(StreamClient.class);

    private final String getStreamsEndpoint;
    private final StreamParser streamParser;
    private final StravaApi stravaApi;

    StreamClient(@Value("${strava.api.getStreams.endpoint}") final String getStreamsEndpoint,
                 @Autowired final StravaApi stravaApi,
                 @Autowired final StreamParser streamParser) {

        this.getStreamsEndpoint = getStreamsEndpoint;
        this.stravaApi = stravaApi;
        this.streamParser = streamParser;
    }

    @SuppressWarnings("unchecked")
    public Optional<Stream> getStream(StravaUserEntity stravaUserEntity, long activityId) throws StravaApiException {
        logger.info("Retrieving stream for activity id {} for strava user {}", activityId, stravaUserEntity.getStravaUsername());

        String endpoint = String.format(getStreamsEndpoint, activityId);
        try {
            ResponseEntity<Map[]> response = stravaApi.call(stravaUserEntity, endpoint, Map[].class);

            logger.info("Successfully retrieved stream for activity {}, strava user {}",
                activityId, stravaUserEntity.getStravaUsername());

            return streamParser.parseStream(stravaUserEntity, activityId, response.getBody());
        }
        catch(RestClientException e) {
            if(e instanceof HttpStatusCodeException) {
                HttpStatusCodeException httpStatusCodeException = (HttpStatusCodeException)e;
                HttpStatus statusCode = httpStatusCodeException.getStatusCode();

                if (HttpStatus.NOT_FOUND.value() == statusCode.value()) {
                    logger.warn("Ignoring stream for activity id {} as it is probably a manual upload", activityId);
                    return Optional.empty();
                }
            }

            throw new StravaApiException(String.format("When getting stream for activity %d", activityId), e);
        }
    }
}
