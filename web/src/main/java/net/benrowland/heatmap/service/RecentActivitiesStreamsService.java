package net.benrowland.heatmap.service;

import net.benrowland.heatmap.client.ActivityClient;
import net.benrowland.heatmap.client.StravaApiException;
import net.benrowland.heatmap.client.StreamClient;
import net.benrowland.heatmap.dto.Activity;
import net.benrowland.heatmap.dto.Stream;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecentActivitiesStreamsService {
    @Autowired
    private ActivityClient activityClient;

    @Autowired
    private StreamClient streamClient;

    public Stream[] streamsForRecentActivities(StravaUserEntity stravaUserEntity) throws StravaApiException {
        Activity[] activities = activityClient.getActivities(stravaUserEntity);

        List<Stream> streams = new ArrayList<>(activities.length);
        for(Activity activity : activities) {
            streams.add(streamClient.getStream(activity.getId()));
        }

        return streams.toArray(new Stream[streams.size()]);
    }
}
