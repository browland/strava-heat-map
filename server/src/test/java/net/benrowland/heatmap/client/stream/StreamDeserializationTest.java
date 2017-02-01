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
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StreamDeserializationTest {
    private static final String LATLNG_STREAM_TYPE = "latlng";

    private JacksonTester<StravaStream[]> jacksonTester;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void testStreamDeserializes() throws IOException {
        final ClassPathResource cpr = new ClassPathResource("net/benrowland/heatmap/dto/stream.json");
        final String streamJson = IOUtils.toString(cpr.getInputStream());

        final StravaStream[] expectedStreamArray = new StravaStream[1];
        StravaStream stream = new StravaStream();
        stream.setType(LATLNG_STREAM_TYPE);
        List<List<Double>> streamData = new ArrayList<>();
        streamData.add(Arrays.asList(0d, 1d));
        streamData.add(Arrays.asList(-2d, 3d));
        stream.setData(streamData);
        expectedStreamArray[0] = stream;

        assertThat(jacksonTester.parse(streamJson)).isEqualTo(expectedStreamArray);
    }

    @Test
    public void test() {
        fail("Add more tests");
    }
}
