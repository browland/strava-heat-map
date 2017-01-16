package net.benrowland.heatmap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.benrowland.heatmap.dto.Stream;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.entity.StreamEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StreamConverter {

    @Autowired
    private ObjectMapper objectMapper;

    public StreamEntity convert(Stream stream, StravaUserEntity stravaUserEntity) throws JsonProcessingException {
        StreamEntity streamEntity = new StreamEntity();
        streamEntity.setActivityId(stream.getActivityId());
        streamEntity.setLatLngStream(objectMapper.writeValueAsString(stream.getData()));
        streamEntity.setStravaUsername(stravaUserEntity.getStravaUsername());

        return streamEntity;
    }
}
