package net.benrowland.heatmap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.benrowland.heatmap.client.ActivityClient;
import net.benrowland.heatmap.client.StravaApiException;
import net.benrowland.heatmap.client.stream.StravaStream;
import net.benrowland.heatmap.client.stream.StreamClient;
import net.benrowland.heatmap.dto.Activity;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.entity.StreamEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecentActivitiesStreamsService {
    private static final Logger logger = LoggerFactory.getLogger(RecentActivitiesStreamsService.class);

    @Autowired
    private ActivityClient activityClient;

    @Autowired
    private StreamClient streamClient;

    @Autowired
    private StreamConverter streamConverter;

    public List<StreamEntity> streamsForRecentActivities(StravaUserEntity stravaUserEntity) throws StravaApiException, JsonProcessingException {
        Activity[] activities = activityClient.getActivities(stravaUserEntity);

        List<StreamEntity> streamEntities = new ArrayList<>(activities.length);
        for(Activity activity : activities) {
            Optional<StravaStream[]> optionalStravaStreams = streamClient.getStreams(stravaUserEntity, activity.getId());

            optionalStravaStreams.ifPresent(stravaStreams ->
                    streamConverter.convert(stravaStreams, activity.getId(), stravaUserEntity)
                            .ifPresent(streamEntities::add));
        }

        logger.info("Successfully processed {} streams for user {}", streamEntities.size(), stravaUserEntity.getStravaUsername());

        return streamEntities;
    }
}
