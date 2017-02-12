package net.benrowland.heatmap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.benrowland.heatmap.client.stream.StravaStream;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.entity.StreamEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StreamConverterTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private StreamConverter streamConverter;

    @Test
    public void validLatLngStreamReturnsStreamEntity() throws JsonProcessingException {
        long activityId = 1L;
        StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setStravaUsername("strava_user");

        List<List<Double>> data = new ArrayList<>();
        data.add(Arrays.asList(1D, 2D));
        data.add(Arrays.asList(3D, 4D));

        StravaStream stravaStream = new StravaStream();
        stravaStream.setType("latlng");
        stravaStream.setData(data);
        StravaStream[] stravaStreams = new StravaStream[] {stravaStream};

        when(objectMapper.writeValueAsString(anyList())).thenReturn("[{'lat': 1, 'lng': 2}, {'lat': 3, 'lng': 4}]");

        Optional<StreamEntity> streamEntityOptional = streamConverter.convert(stravaStreams, activityId, stravaUserEntity);
        assertThat(streamEntityOptional).isPresent();

        DocumentContext ctx = JsonPath.parse(streamEntityOptional.get().getLatLngStream());
        assertEquals(Integer.valueOf(1), ctx.<Integer>read(JsonPath.compile("$[0].lat")));
        assertEquals(Integer.valueOf(2), ctx.<Integer>read(JsonPath.compile("$[0].lng")));
        assertEquals(Integer.valueOf(3), ctx.<Integer>read(JsonPath.compile("$[1].lat")));
        assertEquals(Integer.valueOf(4), ctx.<Integer>read(JsonPath.compile("$[1].lng")));

    }

    @Test
    public void noLatLngStreamReturnsEmpty() {
        long activityId = 1L;
        StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setStravaUsername("strava_user");

        StravaStream[] stravaStreams = new StravaStream[] {null};

        assertThat(streamConverter.convert(stravaStreams, activityId, stravaUserEntity)).isEmpty();
    }

    @Test
    public void multipleLatLngStreamReturnsEmpty() {
        long activityId = 1L;
        StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setStravaUsername("strava_user");

        StravaStream[] stravaStreams = new StravaStream[] {null, null};

        assertThat(streamConverter.convert(stravaStreams, activityId, stravaUserEntity)).isEmpty();
    }
}
