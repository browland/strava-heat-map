package net.benrowland.heatmap.client.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StreamDeserializationTest {
    private static final String LATLNG_STREAM_TYPE = "latlng";
    private static final String DISTANCE_STREAM_TYPE = "distance";

    private JacksonTester<StravaStream[]> jacksonTester;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void testLatLngStreamDeserializes() throws IOException {
        final ClassPathResource cpr = new ClassPathResource("net/benrowland/heatmap/client/stream/stream-latlng.json");
        final String streamJson = IOUtils.toString(cpr.getInputStream());

        StravaStream stream = new StravaStream();
        stream.setType(LATLNG_STREAM_TYPE);

        List<List<Double>> streamData = new ArrayList<>();
        streamData.add(Arrays.asList(0d, 1d));
        streamData.add(Arrays.asList(-2d, 3d));
        stream.setData(streamData);

        final StravaStream[] expectedStreamArray = new StravaStream[1];
        expectedStreamArray[0] = stream;

        assertThat(jacksonTester.parse(streamJson)).isEqualTo(expectedStreamArray);
    }

    @Test
    public void testDistanceStreamDeserializes() throws IOException {
        final ClassPathResource cpr = new ClassPathResource("net/benrowland/heatmap/client/stream/stream-distance.json");
        final String streamJson = IOUtils.toString(cpr.getInputStream());

        StravaStream stream = new StravaStream();
        stream.setType(DISTANCE_STREAM_TYPE);

        final StravaStream[] expectedStreamArray = new StravaStream[1];
        expectedStreamArray[0] = null;

        assertThat(jacksonTester.parse(streamJson)).isEqualTo(expectedStreamArray);
    }

    @Test
    public void testMixedStreamTypesDeserialize() throws IOException {
        final ClassPathResource cpr = new ClassPathResource("net/benrowland/heatmap/client/stream/stream-mixed.json");
        final String streamJson = IOUtils.toString(cpr.getInputStream());

        StravaStream stream = new StravaStream();
        stream.setType(LATLNG_STREAM_TYPE);

        List<List<Double>> streamData = new ArrayList<>();
        streamData.add(Arrays.asList(0d, 1d));
        streamData.add(Arrays.asList(-2d, 3d));
        stream.setData(streamData);

        final StravaStream[] expectedStreamArray = new StravaStream[2];
        expectedStreamArray[0] = stream;
        expectedStreamArray[1] = null;

        assertThat(jacksonTester.parse(streamJson)).isEqualTo(expectedStreamArray);
    }

}
