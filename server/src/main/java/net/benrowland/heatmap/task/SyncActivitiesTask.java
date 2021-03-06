package net.benrowland.heatmap.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.benrowland.heatmap.client.StravaApiException;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.entity.StreamEntity;
import net.benrowland.heatmap.repository.StravaUserRepository;
import net.benrowland.heatmap.repository.StreamRepository;
import net.benrowland.heatmap.service.RecentActivitiesStreamsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SyncActivitiesTask {
    private static final Logger logger = LoggerFactory.getLogger(SyncActivitiesTask.class);

    @Autowired
    private StravaUserRepository stravaUserRepository;

    @Autowired
    private RecentActivitiesStreamsService recentActivitiesStreamsService;


    @Autowired
    private StreamRepository streamRepository;

    @Scheduled(fixedRate = 5000)
    public void pollForNewUsers() throws StravaApiException, JsonProcessingException {
        List<StravaUserEntity> stravaUsersToSync = stravaUserRepository.findBySyncRequired(true);
        if(stravaUsersToSync.size() > 0) {
            logger.info("Found {} new Strava users to sync", stravaUsersToSync.size());
        }

        for(StravaUserEntity stravaUserEntity : stravaUsersToSync) {
            try {
                List<StreamEntity> streamEntities = recentActivitiesStreamsService.streamsForRecentActivitiesAndUpdateLastActivityDate(stravaUserEntity);

                for (StreamEntity streamEntity : streamEntities) {
                    streamRepository.save(streamEntity);
                }

                logger.info("Processed {} streams for user {}", streamEntities.size(), stravaUserEntity);
            }
            catch(StravaApiException e) {
                logger.error("Could not sync user " + stravaUserEntity.getStravaUsername(), e);
            }

            // Unconditionally mark the user as processed so we don't keep re-trying if sync failed
            stravaUserEntity.setSyncRequired(false);
            stravaUserRepository.save(stravaUserEntity);
        }
    }
}
