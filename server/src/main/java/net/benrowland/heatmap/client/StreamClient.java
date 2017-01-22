package net.benrowland.heatmap.client;

import net.benrowland.heatmap.dto.Stream;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class StreamClient {
    private static final Logger logger = LoggerFactory.getLogger(StreamClient.class);

    private static final String URL = "https://www.strava.com/api/v3/activities/%s/streams/latlng?resolution=low&types=latlng";
    private static final String LAT_LNG_STREAM_TYPE = "latlng";
    private static final String STREAM_TYPE = "type";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpSession httpSession;

    public Optional<Stream> getStream(StravaUserEntity stravaUserEntity, long activityId) throws StravaApiException {
        if(stravaUserEntity == null) {
            throw new IllegalArgumentException("Strava user is null");
        }

        logger.info("Retrieving activity {} stream for athlete {}", activityId, stravaUserEntity.getStravaUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + stravaUserEntity.getAccessToken());

        HttpEntity requestEntity = new HttpEntity<>(headers);
        String url = String.format(URL, activityId);

        ResponseEntity<Map[]> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map[].class);
        }
        catch(HttpStatusCodeException e) {
            if(HttpStatus.NOT_FOUND.value() == e.getStatusCode().value()) {
                logger.warn("Ignoring stream for activity id {} as it is probably a manual upload", activityId);
                return Optional.empty();
            }
            else {
                // Could be rate-limiting or any other error
                throw new StravaApiException(String.format("Unexpected response status %d from stream endpoint for activity %d, strava user %s",
                        response.getStatusCode().value(), activityId, stravaUserEntity.getStravaUsername()));
            }
        }

        logger.info("Successfully retrieved activity stream for activity {}, athlete {}", activityId, stravaUserEntity.getStravaUsername());
        return parseStream(stravaUserEntity, activityId, response);
    }

    Optional<Stream> parseStream(StravaUserEntity stravaUserEntity, long activityId, ResponseEntity<Map[]> response) {
        Map<String,Object>[] streamsByType = response.getBody();
        Map<String, Object> latLongStream = null;

        for(Map<String,Object> streamByType : streamsByType) {
            if(LAT_LNG_STREAM_TYPE.equals(streamByType.get(STREAM_TYPE))) {
                latLongStream = streamByType;
            }
        }

        if(latLongStream == null) {
            logger.error("Received stream response with no latlng stream type - strava_username {}, activity {}, body {}",
                stravaUserEntity.getStravaUsername(), activityId, response.getBody());
            return Optional.empty();
        }

        List<List<Double>> latLngs = (List<List<Double>>)latLongStream.get("data");
        Stream stream = new Stream();
        stream.setActivityId(activityId);
        stream.setData(latLngs);

        return Optional.of(stream);
    }
}
