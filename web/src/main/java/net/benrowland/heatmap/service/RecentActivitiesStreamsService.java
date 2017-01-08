package net.benrowland.heatmap.service;

import net.benrowland.heatmap.client.StravaApiException;
import net.benrowland.heatmap.dto.Stream;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.entity.StreamEntity;
import net.benrowland.heatmap.repository.StreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecentActivitiesStreamsService {
    @Autowired
    private StreamRepository streamRepository;

    @Autowired
    private StreamEntityConverter streamEntityConverter;

    public Stream[] streamsForRecentActivities(StravaUserEntity stravaUserEntity) throws StravaApiException, IOException {
        Iterable<StreamEntity> streamEntities = streamRepository.findAll();
        List<Stream> streams = new ArrayList<>();
        for(StreamEntity streamEntity : streamEntities) {
            streams.add(streamEntityConverter.convert(streamEntity));
        }

        return streams.toArray(new Stream[streams.size()]);
    }
}
