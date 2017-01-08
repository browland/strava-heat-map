package net.benrowland.heatmap.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StreamDeserializationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Ignore
    public void testStreamDeserializes() throws IOException {
        final String streamJson = "[{\"data\": [[1,2], [-3,4]] }]";

        Stream[] s = objectMapper.readValue(streamJson, Stream[].class);

        assertThat(s).hasSize(1);
    }
}
