package net.benrowland.heatmap.client;

import net.benrowland.heatmap.dto.Stream;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StreamClientTest {
    private static final String STREAMS_ENDPOINT = "activity=%s";
    private static final String ACCESS_TOKEN = "access_token";
    private static final Long ACTIVITY_ID = 1L;

    @Mock
    private StravaApi stravaApi;

    @Mock
    private StreamParser streamParser;

    private StreamClient streamClient;

    @Before
    public void setUp() {
        streamClient = new StreamClient(STREAMS_ENDPOINT, stravaApi, streamParser);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void callsStravaApiThenParsesStreamThenReturnsOptionalStream() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        final String ENDPOINT_WITH_ACTIVITY_ID = String.format(STREAMS_ENDPOINT, ACTIVITY_ID);

        final ResponseEntity<Map[]> responseEntity = Mockito.mock(ResponseEntity.class);
        final Map<String,Object>[] responseMap = new HashMap[0];  // generic array creation disallowed
        final Optional<Stream> parsedStream = Optional.of(new Stream());

        when(stravaApi.call(eq(stravaUserEntity), eq(ENDPOINT_WITH_ACTIVITY_ID), eq(Map[].class)))
            .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(responseMap);
        when((streamParser.parseStream(eq(stravaUserEntity), eq(ACTIVITY_ID), eq(responseMap))))
            .thenReturn(parsedStream);

        assertThat(streamClient.getStream(stravaUserEntity, ACTIVITY_ID)).isEqualTo(parsedStream);

        verify(streamParser).parseStream(eq(stravaUserEntity), eq(ACTIVITY_ID), eq(responseMap));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stravaApiReturnsNotFoundDoesNotParseStreamReturnsEmptyOptionalStream() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        final String ENDPOINT_WITH_ACTIVITY_ID = String.format(STREAMS_ENDPOINT, ACTIVITY_ID);

        final ResponseEntity<Map[]> responseEntity = Mockito.mock(ResponseEntity.class);

        when(stravaApi.call(eq(stravaUserEntity), eq(ENDPOINT_WITH_ACTIVITY_ID), eq(Map[].class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThat(streamClient.getStream(stravaUserEntity, ACTIVITY_ID)).isEqualTo(Optional.empty());

        verify(responseEntity, never()).getBody();
        verify(streamParser, never()).parseStream(any(StravaUserEntity.class), anyLong(), any(Map[].class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stravaApiReturnsHttpErrorOtherThanNotFoundDoesNotParseStreamReturnsEmptyOptionalStream() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        final String ENDPOINT_WITH_ACTIVITY_ID = String.format(STREAMS_ENDPOINT, ACTIVITY_ID);

        final ResponseEntity<Map[]> responseEntity = Mockito.mock(ResponseEntity.class);

        when(stravaApi.call(eq(stravaUserEntity), eq(ENDPOINT_WITH_ACTIVITY_ID), eq(Map[].class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThatThrownBy(() -> streamClient.getStream(stravaUserEntity, ACTIVITY_ID))
            .isInstanceOf(StravaApiException.class);

        verify(responseEntity, never()).getBody();
        verify(streamParser, never()).parseStream(any(StravaUserEntity.class), anyLong(), any(Map[].class));
    }
}
