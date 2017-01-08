package net.benrowland.heatmap.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.benrowland.heatmap.client.StravaApiException;
import net.benrowland.heatmap.dto.Stream;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.entity.StreamEntity;
import net.benrowland.heatmap.repository.StravaUserRepository;
import net.benrowland.heatmap.repository.StreamRepository;
import net.benrowland.heatmap.service.RecentActivitiesStreamsService;
import net.benrowland.heatmap.service.StreamConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewUserTask {
    private static final Logger logger = LoggerFactory.getLogger(NewUserTask.class);

    @Autowired
    private StravaUserRepository stravaUserRepository;

    @Autowired
    private RecentActivitiesStreamsService recentActivitiesStreamsService;

    @Autowired
    private StreamConverter streamConverter;

    @Autowired
    private StreamRepository streamRepository;

    @Scheduled(fixedRate = 5000)
    public void pollForNewUsers() throws StravaApiException, JsonProcessingException {
        List<StravaUserEntity> stravaUsersToSync = stravaUserRepository.findBySyncRequired(true);
        if(stravaUsersToSync.size() > 0) {
            logger.info("Found {} new Strava users to sync", stravaUsersToSync.size());
        }

        for(StravaUserEntity stravaUserEntity : stravaUsersToSync) {
            List<Stream> streams = recentActivitiesStreamsService.streamsForRecentActivities(stravaUserEntity);

            for(Stream stream : streams) {
                StreamEntity streamEntity = streamConverter.convert(stream);
                streamRepository.save(streamEntity);
            }

            logger.info("Processed {} streams for user {}", streams.size(), stravaUserEntity);

            stravaUserEntity.setSyncRequired(false);
            stravaUserRepository.save(stravaUserEntity);
        }
    }
}
