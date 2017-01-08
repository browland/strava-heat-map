package net.benrowland.heatmap.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import net.benrowland.heatmap.dto.LatLng;
import net.benrowland.heatmap.dto.Stream;
import net.benrowland.heatmap.entity.StreamEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StreamEntityConverter {

    @Autowired
    private ObjectMapper objectMapper;

    public Stream convert(StreamEntity streamEntity) throws IOException {
        Stream stream = new Stream();
        LatLng[] latLngs = objectMapper.readValue(streamEntity.getLatLngStream(), LatLng[].class);
        stream.setData(latLngs);

        return stream;
    }
}
