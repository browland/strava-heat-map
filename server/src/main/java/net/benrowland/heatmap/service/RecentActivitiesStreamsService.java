package net.benrowland.heatmap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.benrowland.heatmap.client.ActivityClient;
import net.benrowland.heatmap.client.StravaApiException;
import net.benrowland.heatmap.client.StreamClient;
import net.benrowland.heatmap.dto.Activity;
import net.benrowland.heatmap.dto.Stream;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.entity.StreamEntity;
import net.benrowland.heatmap.repository.StreamRepository;
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


    public List<Stream> streamsForRecentActivities(StravaUserEntity stravaUserEntity) throws StravaApiException, JsonProcessingException {
        Activity[] activities = activityClient.getActivities(stravaUserEntity);

        List<Stream> streams = new ArrayList<>(activities.length);
        for(Activity activity : activities) {
            Optional<Stream> optionalStream = streamClient.getStream(stravaUserEntity, activity.getId());
            optionalStream.ifPresent(streams::add);
        }

        logger.info("Successfully processed {} streams for user {}", streams.size(), stravaUserEntity.getStravaUsername());

        return streams;
    }
}
