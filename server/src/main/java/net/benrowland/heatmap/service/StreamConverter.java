package net.benrowland.heatmap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.benrowland.heatmap.client.stream.StravaStream;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.entity.StreamEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
class StreamConverter {
    private static final Logger logger = LoggerFactory.getLogger(StreamConverter.class);
    private static final String LATLNG = "latlng";

    @Autowired
    private ObjectMapper objectMapper;

    /**
     *
     * Returns Optional.empty() if there is no stream containing lat/long data for this activity (e.g. manual upload)
     * and we should continue processing activities for the current Strava user.
     */
    Optional<StreamEntity> convert(StravaStream[] streams, Long activityId, StravaUserEntity stravaUserEntity) {
        StravaStream latLngStream = null;
        for(StravaStream stravaStream : streams) {
            if (stravaStream != null && LATLNG.equals(stravaStream.getType())) {
                latLngStream = stravaStream;
            }
        }

        if(latLngStream == null) {
            logger.warn("Did not find latlng stream in streams from Strava for activity id {}", activityId);
            return Optional.empty();
        }

        StreamEntity streamEntity = new StreamEntity();
        streamEntity.setActivityId(activityId);
        streamEntity.setStravaUsername(stravaUserEntity.getStravaUsername());

        try {
            List<LatLng> latLngList = latLngStream.getData()
                    .stream()
                    .map(latLngPair -> new LatLng(latLngPair.get(0), latLngPair.get(1)))
                    .collect(Collectors.toList());

            String latLngStreamString = objectMapper.writeValueAsString(latLngList);
            streamEntity.setLatLngStream(latLngStreamString);

            return Optional.of(streamEntity);
        }
        catch(JsonProcessingException e) {
            throw new IllegalArgumentException("While serializing lat-lng stream", e);
        }
    }
}
