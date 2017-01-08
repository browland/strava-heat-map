package net.benrowland.heatmap.client;

import net.benrowland.heatmap.dto.Stream;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Component
public class StreamClient {
    private static final Logger logger = LoggerFactory.getLogger(StreamClient.class);

    private static final String URL = "https://www.strava.com/api/v3/activities/%s/streams/latlng?resolution=low&types=latlng";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpSession httpSession;

    public Stream getStream(StravaUserEntity stravaUserEntity, long activityId) throws StravaApiException {
        if(stravaUserEntity == null) {
            throw new IllegalArgumentException("Strava user is null");
        }

        logger.info("Retrieving activity {} stream for athlete {}", activityId, stravaUserEntity.getStravaUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + stravaUserEntity.getAccessToken());

        HttpEntity requestEntity = new HttpEntity<>(headers);
        String url = String.format(URL, activityId);
        ResponseEntity<Map[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map[].class);

        if(response.getStatusCode().is2xxSuccessful()) {
            logger.info("Successfully retrieved activity stream for activity {}, athlete {}", activityId, stravaUserEntity.getStravaUsername());
            Map<String,Object>[] responseMaps = response.getBody();
            for(Map<String,Object> responseMap : responseMaps) {
                if("latlng".equals(responseMap.get("type"))) {
                    List<List<Double>> latLngs = (List<List<Double>>)responseMap.get("data");
                    Stream stream = new Stream();
                    stream.setActivityId(activityId);
                    stream.setData(latLngs);
                    return stream;
                }
            }

            throw new IllegalStateException(String.format("Could not decipher stream response for athlete %s, activity %d", stravaUserEntity.getStravaUsername(), activityId));
        }
        else {
            throw new StravaApiException("Unexpected response from stream endpoint " + response.getStatusCode());
        }
    }
}
