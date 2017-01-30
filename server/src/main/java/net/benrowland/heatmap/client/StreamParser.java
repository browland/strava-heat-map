package net.benrowland.heatmap.client;

import net.benrowland.heatmap.dto.Stream;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
class StreamParser {
    private static final Logger logger = LoggerFactory.getLogger(StreamParser.class);

    private static final String LAT_LNG_STREAM_TYPE = "latlng";
    private static final String STREAM_TYPE = "type";
    private static final String DATA_ATTRIBUTE = "data";

    // We get the response as a Map[] because different fields could be present in the response - a better way could be
    // a response object which has all the possible fields?
    @SuppressWarnings("unchecked")
    Optional<Stream> parseStream(StravaUserEntity stravaUserEntity, long activityId, Map<String,Object>[] streamsByType) {
        Map<String, Object> latLongStream = null;

        for(Map<String,Object> streamByType : streamsByType) {
            if(LAT_LNG_STREAM_TYPE.equals(streamByType.get(STREAM_TYPE))) {
                latLongStream = streamByType;
            }
        }

        if(latLongStream == null) {
            logger.error("Received stream response with no latlng stream type - strava_username {}, activity {}, streamsByType {}",
                stravaUserEntity.getStravaUsername(), activityId, streamsByType);
            return Optional.empty();
        }

        List<List<Double>> latLngs = (List<List<Double>>)latLongStream.get(DATA_ATTRIBUTE);

        Stream stream = new Stream();
        stream.setActivityId(activityId);
        stream.setData(latLngs);

        return Optional.of(stream);
    }
}
