package net.benrowland.heatmap.client.stream;

import net.benrowland.heatmap.client.StravaApi;
import net.benrowland.heatmap.client.StravaApiException;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StreamClientTest {
    private static final String STREAMS_ENDPOINT = "activity=%s";
    private static final Long ACTIVITY_ID = 1L;

    @Mock
    private StravaApi stravaApi;

    private StreamClient streamClient;

    @Before
    public void setUp() {
        streamClient = new StreamClient(STREAMS_ENDPOINT, stravaApi);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void callsStravaApiThenParsesStreamThenReturnsStreams() throws StravaApiException {
        final String ENDPOINT_WITH_ACTIVITY_ID = String.format(STREAMS_ENDPOINT, ACTIVITY_ID);

        StravaStream stravaStream = new StravaStream();
        final ResponseEntity<StravaStream[]> responseEntity = Mockito.mock(ResponseEntity.class);
        final StravaStream[] response = new StravaStream[1];  // generic array creation disallowed
        response[0] = stravaStream;

        final StravaStream[] expectedStreams = new StravaStream[] {stravaStream};
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();

        when(stravaApi.call(eq(stravaUserEntity), eq(ENDPOINT_WITH_ACTIVITY_ID), eq(StravaStream[].class)))
            .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(response);

        StravaStream[] streams = streamClient.getStreams(stravaUserEntity, ACTIVITY_ID);

        assertThat(streams).isEqualTo(expectedStreams);
    }

    @Test
    @SuppressWarnings("unchecked")
    // TODO
    public void stravaApiReturnsNotFoundDoesNotParseStreamReturnsEmptyOptionalStreams() throws StravaApiException {
    }

    @Test
    @SuppressWarnings("unchecked")
    // TODO
    public void stravaApiReturnsHttpErrorOtherThanNotFoundThrowsStravaApiException() throws StravaApiException {
    }
}
